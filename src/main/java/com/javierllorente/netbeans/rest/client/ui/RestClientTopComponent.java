/*
 * Copyright 2022-2026 Javier Llorente <javier@opensuse.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javierllorente.netbeans.rest.client.ui;

import com.javierllorente.netbeans.rest.util.ExceptionUtils;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.Mnemonics;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;

import com.javierllorente.netbeans.rest.client.RestClient;
import com.javierllorente.netbeans.rest.client.ResponseModel;
import com.javierllorente.netbeans.rest.client.UserAgent;
import com.javierllorente.netbeans.rest.client.event.CellDocumentListener;
import com.javierllorente.netbeans.rest.client.event.TabChangeListener;
import com.javierllorente.netbeans.rest.client.event.TableParamsListener;
import com.javierllorente.netbeans.rest.client.event.TokenDocumentListener;
import com.javierllorente.netbeans.rest.client.event.UrlDocumentListener;
import com.javierllorente.netbeans.rest.client.parsers.CellParamsParser;
import com.javierllorente.netbeans.rest.client.util.HttpFileUtils;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//com.javierllorente.netbeans.rest.client//RestClient//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "RestClientTopComponent",
    iconBase = "com/javierllorente/netbeans/rest/client/restservice.png",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@Messages({
    "CTL_RestClientAction=REST Client",
    "CTL_RestClientTopComponent=REST Client",
    "HINT_RestClientTopComponent="
})
public class RestClientTopComponent extends TopComponent {

    private static final Logger logger = Logger.getLogger(RestClientTopComponent.class.getName());

    private static final String VERSION_PROPERTY = "version";
    private static final String URL_PROPERTY = "url";
    private static final String REQUEST_METHOD_PROPERTY = "request_method";
    private static final String AUTH_TYPE_PROPERTY = "auth_type";
    private static final String USERNAME_PROPERTY = "username";
    private static final String HEADERS_PROPERTY = "headers";

    private RestClient client = null;
    private final RequestProcessor processor;
    private final TableParamsListener tableParamsListener;

    public RestClientTopComponent() {
        this(null);
    }

    public RestClientTopComponent(RestClient client) {
        this.client = client;

        initComponents();

        setName(Bundle.CTL_RestClientTopComponent());
        setToolTipText(Bundle.HINT_RestClientTopComponent());

        paramsPanel.hideEnableColumn();

        if (this.client == null) {
            this.client = new RestClient();
        }

        UrlDocumentListener urlDocumentListener = new UrlDocumentListener(paramsPanel);

        CellParamsParser cellParamsParser = new CellParamsParser(paramsPanel, urlPanel, urlDocumentListener);
        DocumentListener cellDocumentListener = new CellDocumentListener(cellParamsParser);

        tableParamsListener = new TableParamsListener(paramsPanel, urlPanel, urlDocumentListener);
        paramsPanel.addTableModelListener(tableParamsListener);

        urlPanel.setRequestMethod(this.client.getMethod());
        urlPanel.setUrl(this.client.getUri());

        urlPanel.addUrlDocumentListener(urlDocumentListener);
        paramsPanel.addDocumentListener(cellDocumentListener);

        urlPanel.addUrlFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                paramsPanel.removeTableModelListener(tableParamsListener);
            }

            @Override
            public void focusLost(FocusEvent e) {
                paramsPanel.addTableModelListener(tableParamsListener);
            }
        });

        urlPanel.addComboBoxActionListener((ae) -> {
            boolean enableComboBox = !(urlPanel.getRequestMethod().equals(HttpMethod.GET)
                || urlPanel.getRequestMethod().equals(HttpMethod.DELETE));
            if (!enableComboBox) {
                bodyPanel.setBodyType("None");
            }
            bodyPanel.setComboBoxEnabled(enableComboBox);
        });

        urlPanel.addSendButtonActionListener(ae -> sendRequest());

        openInEditorBtn.addActionListener(ae -> openInEditor());

        urlPanel.addUrlKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendRequest();
                }
            }
        });

        paramsPanel.addPropertyChangeListener("tableCellEditor", (PropertyChangeEvent pce) -> {
            logger.info("ParamsPanel editing " + pce.getNewValue() == null ? "stopped" : "started");
        });

        KeyListener escapeKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    String oldValue = (paramsPanel.getSelectedColumn() == 1
                        ? paramsPanel.getKey(paramsPanel.getSelectedRow())
                        : paramsPanel.getValue(paramsPanel.getSelectedRow()));
                    cellParamsParser.processChanges(oldValue);
                }
            }
        };
        paramsPanel.addCellKeyListener(escapeKeyListener);
        paramsPanel.addTableKeyListener(escapeKeyListener);

        DocumentListener tokenDocumentListener = new TokenDocumentListener(headersPanel);
        authPanel.addTokenDocumentListener(tokenDocumentListener);
        authPanel.addComboBoxListener((ActionEvent ae) -> {
            if (authPanel.getAuthType().equals("No Auth")) {
                int index = headersPanel.containsKey("Authorization");
                if (index != -1 && headersPanel.getValue(index).startsWith("Bearer")) {
                    headersPanel.removeRow(index);
                }
            }
        });
        TabChangeListener tabChangeListener = new TabChangeListener(headersPanel, authPanel, tokenDocumentListener);
        requestTabbedPane.addChangeListener(tabChangeListener);

        headersPanel.addRow("User-Agent", UserAgent.FULL);

        MultivaluedMap<String, String> headers = this.client.getHeaders();

        if (headers != null) {
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();

                // Skip if header already exists
                if (headersPanel.containsKey(key) != -1) {
                    continue;
                }

                String value = entry.getValue().get(0);
                headersPanel.addRow(key, value);
            }
        }

        bodyPanel.setBodyType(this.client.getBodyType());
        bodyPanel.setBody(this.client.getBody());

        processor = new RequestProcessor(RestClientTopComponent.class);
    }

    @Override
    protected void componentOpened() {
        urlPanel.requestUrlFocus();
    }

    @Override
    protected void componentActivated() {

    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {

        p.setProperty(VERSION_PROPERTY, "1.0");
        p.setProperty(REQUEST_METHOD_PROPERTY, urlPanel.getRequestMethod());
        p.setProperty(URL_PROPERTY, urlPanel.getUrl());
        p.setProperty(AUTH_TYPE_PROPERTY, authPanel.getAuthType());
        p.setProperty(USERNAME_PROPERTY, authPanel.getUsername());
        p.setProperty(HEADERS_PROPERTY, headersPanel.getValuesString());
    }

    void readProperties(java.util.Properties p) {
//        String version = p.getProperty(VERSION_PROPERTY);
        String url = p.getProperty(URL_PROPERTY);

        if (!url.isEmpty()) {
            setUrl(url);
        }

        String requestMethod = p.getProperty(REQUEST_METHOD_PROPERTY);
        if (!requestMethod.isEmpty()) {
            urlPanel.setRequestMethod(requestMethod);
        }

        String username = p.getProperty(USERNAME_PROPERTY);
        if (!username.isEmpty()) {
            authPanel.setUsername(username);
        }

        String authType = p.getProperty(AUTH_TYPE_PROPERTY);
        if (!authType.isEmpty()) {
            authPanel.setAuthType(authType);
        }

        String headers = p.getProperty(HEADERS_PROPERTY);
        headersPanel.setValuesString(headers);

    }

    public String getUrl() {
        return urlPanel.getUrl();
    }

    public void setUrl(String url) {
        paramsPanel.removeTableModelListener(tableParamsListener);
        urlPanel.setUrl(url);
        paramsPanel.addTableModelListener(tableParamsListener);
        SwingUtilities.invokeLater(() -> {
            setDisplayName(urlPanel.getRequestMethod() + " " + urlPanel.getDisplayUrl());
            setToolTipText(urlPanel.getUrl());
        });
        urlPanel.requestUrlFocus();
    }

    public String getDisplayUrl() {
        return urlPanel.getDisplayUrl();
    }

    public String getRequestMethod() {
        return urlPanel.getRequestMethod();
    }

    public void setRequestMethod(String method) {
        urlPanel.setRequestMethod(method);
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headersPanel.getValues();
    }

    public void setHeaders(MultivaluedMap<String, String> headers) {
        headersPanel.setValues(headers);
    }

    public void addHeader(String key, String value) {
        headersPanel.addRow(key, value);
    }

    public void clearHeaders() {
        headersPanel.clearValues();
    }

    private void sendRequest() {
        if (urlPanel.getUrl().isBlank()) {
            return;
        }
        responsePanel.clear();
        setDisplayName(urlPanel.getRequestMethod() + " " + urlPanel.getDisplayUrl());
        setToolTipText(urlPanel.getUrl());
        request();
        logger.log(Level.INFO, "Request method: {0}, Auth type: {1}, Body type: {2}",
            new Object[]{urlPanel.getRequestMethod(), authPanel.getAuthType(), bodyPanel.getBodyType()});
    }

    private void request() {
        logger.log(Level.INFO, "URL: {0}", urlPanel.getUrl());
        processor.post(() -> {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ProgressHandle progressHandle = ProgressHandle.createHandle("Sending request");
            progressHandle.start();

            if (headersPanel.getRowCount() > 0) {
                MultivaluedMap<String, String> headers = headersPanel.getValues();
                client.setHeaders(headers);
            }

            client.setAuthType(authPanel.getAuthType());
            if (authPanel.getAuthType().equals(RestClient.BASIC_AUTH)) {
                client.setCredentials(authPanel.getUsername(), authPanel.getPassword());
            }

            String body = "";
            if (!bodyPanel.getBodyType().equals("None")) {
                body = bodyPanel.getBody();
            }
            client.setBody(body);
            client.setBodyType(bodyPanel.getBodyType());

            try {
                String url = urlPanel.getUrl();
                ResponseModel response = client.request(url, urlPanel.getRequestMethod());
                setUrl(client.getUri());
                urlPanel.moveCaretToEnd();
                showResponse(response);
            } catch (ProcessingException ex) {
                logger.warning(ex.getMessage());
                ExceptionUtils.handleAndDisplayProcessingException(ex, responsePanel);
            } finally {
                progressHandle.finish();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

        });

    }

    private void showResponse(ResponseModel response) {
        SwingUtilities.invokeLater(() -> {
            responsePanel.showResponse(response);
        });
    }

    private void openInEditor() {
        try {
            // Create HTTP request content
            var httpContent = new StringBuilder();
            String method = urlPanel.getRequestMethod();
            String url = urlPanel.getUrl();

            httpContent.append(method != null ? method : "GET").append(" ").append(url != null ? url : "").append("\n");

            // Add headers
            if (headersPanel.getRowCount() > 0) {
                MultivaluedMap<String, String> headers = headersPanel.getValues();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    for (String value : entry.getValue()) {
                        httpContent.append(entry.getKey()).append(": ").append(value).append("\n");
                    }
                }
            }

            // Add body if present
            String bodyType = bodyPanel.getBodyType();
            if (bodyType != null && !bodyType.equals("None")) {
                httpContent.append("\n");
                httpContent.append(bodyPanel.getBody());
            }

            String httpText = httpContent.toString();

            // Calculate caret position (after method and space) if URL is empty
            int caretPosition = -1;
            if (url == null || url.isEmpty()) {
                caretPosition = (method != null ? method.length() : 3) + 1;
            }

            HttpFileUtils.createAndOpenHttpFile(httpText, caretPosition);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new JSplitPane();
        requestPanel = new JPanel();
        requestTabbedPane = new JTabbedPane();
        paramsPanel = new TablePanel();
        authPanel = new AuthPanel();
        headersPanel = new TablePanel();
        bodyPanel = new BodyPanel();
        urlPanel = new UrlPanel();
        responsePanel = new ResponsePanel();
        openInEditorBtn = new JButton();

        setPreferredSize(new Dimension(800, 600));

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        requestPanel.setPreferredSize(new Dimension(800, 240));

        requestTabbedPane.setPreferredSize(new Dimension(705, 150));
        requestTabbedPane.addTab("Params", paramsPanel);
        requestTabbedPane.addTab("Authorisation", authPanel);
        requestTabbedPane.addTab("Headers", headersPanel);
        requestTabbedPane.addTab("Body", bodyPanel);

        Mnemonics.setLocalizedText(openInEditorBtn, "Open in editor");

        GroupLayout requestPanelLayout = new GroupLayout(requestPanel);
        requestPanel.setLayout(requestPanelLayout);
        requestPanelLayout.setHorizontalGroup(
            requestPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(requestPanelLayout.createSequentialGroup()
                .addComponent(requestTabbedPane, GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(requestPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(openInEditorBtn)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(urlPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        requestPanelLayout.setVerticalGroup(
            requestPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(requestPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(requestPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(urlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(openInEditorBtn))
                .addGap(3, 3, 3)
                .addComponent(requestTabbedPane, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
        );

        requestTabbedPane.getAccessibleContext().setAccessibleName("");

        splitPane.setTopComponent(requestPanel);
        splitPane.setBottomComponent(responsePanel);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private AuthPanel authPanel;
    private BodyPanel bodyPanel;
    private TablePanel headersPanel;
    private JButton openInEditorBtn;
    private TablePanel paramsPanel;
    private JPanel requestPanel;
    private JTabbedPane requestTabbedPane;
    private ResponsePanel responsePanel;
    private JSplitPane splitPane;
    private UrlPanel urlPanel;
    // End of variables declaration//GEN-END:variables
}
