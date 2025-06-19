/*
 * Copyright 2022-2024 Javier Llorente <javier@opensuse.org>.
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

import com.javierllorente.netbeans.rest.client.event.CellDocumentListener;
import com.javierllorente.netbeans.rest.client.parsers.CellParamsParser;
import com.javierllorente.netbeans.rest.client.RestClient;
import com.javierllorente.netbeans.rest.client.event.TabChangeListener;
import com.javierllorente.netbeans.rest.client.event.TableParamsListener;
import com.javierllorente.netbeans.rest.client.event.TokenDocumentListener;
import com.javierllorente.netbeans.rest.client.event.UrlDocumentListener;
import com.javierllorente.netbeans.rest.client.UserAgent;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MultivaluedMap;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.windows.TopComponent;

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
@ActionID(category = "Window", id = "com.javierllorente.netbeans.rest.client.RestClientTopComponent")
@ActionReference(path = "Menu/Tools", position = 805)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_RestClientTopComponent" /*, 
        preferredID = "RestClientTopComponent" */
)
@Messages({
    "CTL_RestClientAction=REST Client",
    "CTL_RestClientTopComponent=REST Client",
    "HINT_RestClientTopComponent="
})
public class RestClientTopComponent extends TopComponent implements SaveCookie {

    private static final Logger logger = Logger.getLogger(RestClientTopComponent.class.getName());

    public static final String VERSION_PROPERTY = "version";
    public static final String URL_PROPERTY = "url";
    public static final String REQUEST_METHOD_PROPERTY = "request_method";
    public static final String AUTH_TYPE_PROPERTY = "auth_type";
    public static final String USERNAME_PROPERTY = "username";
    public static final String HEADERS_PROPERTY = "headers";

    public static final String BODY_TYPE_PROPERTY = "body_type";
    public static final String BODY_PROPERTY = "body";

    private final RestClient client;
    private final RequestProcessor processor;
    private final TableParamsListener tableParamsListener;

    private FileObject currentFile;
    private boolean modified = false;
    // Aggiungere questa variabile d'istanza
    private SaveCookie saveCookie;

    public RestClientTopComponent(FileObject currentFile) {
        this();
        setFile(currentFile);
        readFile();
    }

    public RestClientTopComponent() {
        initComponents();

        setupModificationListeners();

        setName(Bundle.CTL_RestClientTopComponent());
        setToolTipText(Bundle.HINT_RestClientTopComponent());

        paramsPanel.hideEnableColumn();

        UrlDocumentListener urlDocumentListener = new UrlDocumentListener(paramsPanel);

        CellParamsParser cellParamsParser = new CellParamsParser(paramsPanel, urlPanel, urlDocumentListener);
        DocumentListener cellDocumentListener = new CellDocumentListener(cellParamsParser);

        tableParamsListener = new TableParamsListener(paramsPanel, urlPanel, urlDocumentListener);
        paramsPanel.addTableModelListener(tableParamsListener);

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

        urlPanel.addSendButtonActionListener((ae) -> {
            sendRequest();
        });

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

        client = new RestClient();
        processor = new RequestProcessor(RestClientTopComponent.class);
    }

    private void setModified(boolean modified) {
        if (this.modified != modified) {
            this.modified = modified;
            if (modified) {
                if (saveCookie == null) {
                    saveCookie = (SaveCookie) () -> saveToFile();
                }
                associateLookup(Lookups.fixed(this, saveCookie));
            } else {
                associateLookup(Lookups.fixed(this));
                saveCookie = null;
            }
            firePropertyChange("modified", !modified, modified);
        }
    }

    private void resetModified() {
        modified = false;
        setModified(false);
        associateLookup(Lookups.fixed(this)); // Rimuove il SaveCookie
    }

    private void markAsModified() {
        setModified(true);
    }

    private void markAsUnmodified() {
        setModified(false);
    }

    @Override
    public Lookup getLookup() {
        if (modified && saveCookie != null) {
            return Lookups.fixed(this, saveCookie);
        }
        return Lookups.fixed(this);
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        urlPanel.requestUrlFocus();
        resetModified(); // Resetta lo stato all'apertura
    }

    @Override
    protected void componentActivated() {

    }

    @Override
    public void save() throws IOException {
        if (!modified) {
            return;
        }
        saveToFile();
    }

    @Override
    public boolean canClose() {
        if (modified) {
            NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(
                    "File modificato. Salvare le modifiche?",
                    "Conferma Salvataggio",
                    NotifyDescriptor.YES_NO_OPTION);

            Object res = DialogDisplayer.getDefault().notify(nd);
            if (res == NotifyDescriptor.YES_OPTION) {
                try {
                    save();
                    return true;
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    public FileObject getFile() {
        return currentFile;
    }

    public void setFile(FileObject file) {
        this.currentFile = file;
        setDisplayName("Editor per: " + file.getNameExt());
    }

    public void saveToFile() throws IOException {
        if (currentFile != null) {
            try (OutputStream out = currentFile.getOutputStream(); FileLock lock = currentFile.lock()) {
                Properties props = new Properties();
                writeProperties(props);
                props.store(out, "Rest Client Configuration");
                markAsUnmodified();
                setDisplayName(currentFile.getNameExt());
            }
        }
    }

    public void readFile() {
        if (currentFile != null) {
            try (InputStream inp = currentFile.getInputStream()) {
                Properties props = new Properties();

                props.load(inp);
                readProperties(props);
                // Aggiorna il nome della finestra
                setDisplayName(currentFile.getNameExt());

                // Mostra conferma
                /*
                String message = "Configuration read successfully";
                NotifyDescriptor nd = new NotifyDescriptor.Message(message, NotifyDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);*/
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void saveAs() {
        FileDialog fd = new FileDialog(new JFrame(), "Save REST Client Config", FileDialog.SAVE);
        fd.setFile("*.restclient");
        fd.setVisible(true);

        if (fd.getFile() != null) {
            String fileName = fd.getFile().endsWith(".restclient")
                    ? fd.getFile() : fd.getFile() + ".restclient";

            try {
                FileObject folder = FileUtil.toFileObject(Paths.get(fd.getDirectory()));
                FileObject file = FileUtil.createData(folder, fileName);
                currentFile = file;
                saveToFile();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /*
    // Modifica la dichiarazione della classe SaveAction aggiungendo 'static'
    @ActionID(category = "File", id = "com.javierllorente.netbeans.rest.client.SaveAction")
    @ActionRegistration(displayName = "Save REST Client Config")
    @ActionReference(path = "Menu/File", position = 1500)
    public static class SaveAction implements ActionListener {  // Aggiunto 'static' qui

        @Override
        public void actionPerformed(ActionEvent e) {
            // Per accedere ai membri non-static della classe esterna
            RestClientTopComponent topComponent
                    = (RestClientTopComponent) WindowManager.getDefault().findTopComponent("RestClientTopComponent");
            if (topComponent != null) {
                topComponent.saveToFile();
            }
        }
    }
     */
    private void setupModificationListeners() {
        urlPanel.addRequestMethodActionListener(e -> markAsModified());
        urlPanel.addUrlDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                markAsModified();
            }
        });
        authPanel.addComboBoxListener(e -> markAsModified());
        authPanel.addTokenDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                markAsModified();
            }
        });

        headersPanel.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                markAsModified();
            }
        });

        // Aggiungere listener per il body panel
        bodyPanel.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                markAsModified();
            }
        });

        // Aggiungere listener per il combo box del body type
        bodyPanel.addBodyTypeChangeListener(e -> markAsModified());

        // Aggiungere listener per i campi di autenticazione
        authPanel.addUsernameDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                markAsModified();
            }
        });

        authPanel.addPasswordDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                markAsModified();
            }
        });
    }

    public void writeProperties(java.util.Properties p) {

        p.setProperty(VERSION_PROPERTY, "1.0");
        p.setProperty(URL_PROPERTY, urlPanel.getUrl());
        p.setProperty(REQUEST_METHOD_PROPERTY, urlPanel.getRequestMethod());
        p.setProperty(AUTH_TYPE_PROPERTY, authPanel.getAuthType());
        p.setProperty(USERNAME_PROPERTY, authPanel.getUsername());
        p.setProperty(HEADERS_PROPERTY, headersPanel.getValuesString());
        p.setProperty(BODY_TYPE_PROPERTY, bodyPanel.getBodyType());
        p.setProperty(BODY_PROPERTY, bodyPanel.getBody());
    }

    public void readProperties(java.util.Properties p) {
//        String version = p.getProperty(VERSION_PROPERTY);
        String url = p.getProperty(URL_PROPERTY);

        if (url != null && !url.isEmpty()) {
            setUrl(url);
        }

        String requestMethod = p.getProperty(REQUEST_METHOD_PROPERTY);
        if (requestMethod != null && !requestMethod.isEmpty()) {
            urlPanel.setRequestMethod(requestMethod);
        }

        String username = p.getProperty(USERNAME_PROPERTY);
        if (username != null && !username.isEmpty()) {
            authPanel.setUsername(username);
        }

        String authType = p.getProperty(AUTH_TYPE_PROPERTY);
        if (authType != null && !authType.isEmpty()) {
            authPanel.setAuthType(authType);
        }

        String headers = p.getProperty(HEADERS_PROPERTY);
        if (headers != null && !headers.isEmpty()) {
            headersPanel.setValuesString(headers);
        }

        String bodyType = p.getProperty(BODY_TYPE_PROPERTY);
        if (bodyType != null && !bodyType.isEmpty()) {
            bodyPanel.setBodyType(bodyType);
        }

        String body = p.getProperty(BODY_PROPERTY);
        if (body != null && !body.isEmpty()) {
            bodyPanel.setBody(body);
        }

    }

    public String getUrl() {
        return urlPanel.getUrl();
    }

    public void setUrl(String url) {
        paramsPanel.removeTableModelListener(tableParamsListener);
        urlPanel.setUrl(url);
        paramsPanel.addTableModelListener(tableParamsListener);
        setDisplayName(urlPanel.getRequestMethod() + " " + urlPanel.getDisplayUrl());
        setToolTipText(urlPanel.getUrl());
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
                if (!url.startsWith("http")) {
                    url = "http://" + urlPanel.getUrl();
                }
                String response = client.request(url, urlPanel.getRequestMethod());
                MultivaluedMap<String, Object> responseHeaders = client.getResponseHeaders();
                updateResponsePanel(response, responseHeaders);
            } catch (ProcessingException ex) {
                logger.warning(ex.getMessage());
                String response = (ex.getMessage().contains("PKIX path building failed"))
                        ? "Could not get response: failed to verify SSL certificate\n"
                        + "SSL certificate verification is enabled. "
                        + "You may disable it under Tools->Options->Miscellaneous->REST Client"
                        : ex.getMessage();
                responsePanel.setResponse(response);
                responsePanel.showResponse();
            } finally {
                progressHandle.finish();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

        });

    }

    private void updateResponsePanel(String response, MultivaluedMap<String, Object> responseHeaders) {
        SwingUtilities.invokeLater(() -> {

            String contentType = "";
            if (responseHeaders.containsKey("content-type") && !responseHeaders.get("content-type").isEmpty()) {
                contentType = (String) responseHeaders.get("content-type").get(0);
            }
            responsePanel.setContentType(contentType);

            responsePanel.setResponse(response);
            responsePanel.showResponse();
            responsePanel.setStatus("Status: " + client.getStatus() + " " + client.getStatusText()
                    + "  Time: " + client.getElapsedTime() + " ms");

            for (Map.Entry<String, List<Object>> entry : responseHeaders.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue().toString();
                responsePanel.addHeader(key, val);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        requestPanel = new javax.swing.JPanel();
        requestTabbedPane = new javax.swing.JTabbedPane();
        paramsPanel = new com.javierllorente.netbeans.rest.client.ui.TablePanel();
        authPanel = new com.javierllorente.netbeans.rest.client.ui.AuthPanel();
        headersPanel = new com.javierllorente.netbeans.rest.client.ui.TablePanel();
        bodyPanel = new com.javierllorente.netbeans.rest.client.ui.BodyPanel();
        urlPanel = new com.javierllorente.netbeans.rest.client.ui.UrlPanel();
        responsePanel = new com.javierllorente.netbeans.rest.client.ui.ResponsePanel();

        setPreferredSize(new java.awt.Dimension(800, 600));

        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        requestPanel.setPreferredSize(new java.awt.Dimension(800, 240));

        requestTabbedPane.setPreferredSize(new java.awt.Dimension(705, 150));
        requestTabbedPane.addTab("Params", paramsPanel);
        requestTabbedPane.addTab("Authorisation", authPanel);
        requestTabbedPane.addTab("Headers", headersPanel);
        requestTabbedPane.addTab("Body", bodyPanel);

        javax.swing.GroupLayout requestPanelLayout = new javax.swing.GroupLayout(requestPanel);
        requestPanel.setLayout(requestPanelLayout);
        requestPanelLayout.setHorizontalGroup(
            requestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requestPanelLayout.createSequentialGroup()
                .addComponent(requestTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(urlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        requestPanelLayout.setVerticalGroup(
            requestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requestPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(urlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(requestTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
        );

        requestTabbedPane.getAccessibleContext().setAccessibleName("");

        splitPane.setTopComponent(requestPanel);
        splitPane.setBottomComponent(responsePanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.javierllorente.netbeans.rest.client.ui.AuthPanel authPanel;
    private com.javierllorente.netbeans.rest.client.ui.BodyPanel bodyPanel;
    private com.javierllorente.netbeans.rest.client.ui.TablePanel headersPanel;
    private com.javierllorente.netbeans.rest.client.ui.TablePanel paramsPanel;
    private javax.swing.JPanel requestPanel;
    private javax.swing.JTabbedPane requestTabbedPane;
    private com.javierllorente.netbeans.rest.client.ui.ResponsePanel responsePanel;
    private javax.swing.JSplitPane splitPane;
    private com.javierllorente.netbeans.rest.client.ui.UrlPanel urlPanel;
    // End of variables declaration//GEN-END:variables
}
