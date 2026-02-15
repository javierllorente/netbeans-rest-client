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

import com.javierllorente.netbeans.rest.client.ResponseModel;
import com.javierllorente.netbeans.rest.util.FormatUtils;
import com.javierllorente.netbeans.rest.client.editor.RestMediaType;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.ws.rs.core.MediaType;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLayer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import org.openide.text.CloneableEditorSupport;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class ResponsePanel extends JPanel {

    private final JTabbedPane responseTabbedPane;
    private final JEditorPane responseEditorPane;
    private final JToggleButton prettyButton;
    private final TablePanel responseHeadersTable;
    private final StatusLabel statusLabel;
    private final LineNumberComponent lineNumberComponent;
    private final JButton previewButton;
    private String mimePath = MediaType.TEXT_PLAIN;
    private ResponseModel response;

    public ResponsePanel() {
        super(new BorderLayout());

        responseTabbedPane = new JTabbedPane();
        responseTabbedPane.setPreferredSize(new Dimension(705, 150));

        responseEditorPane = new JEditorPane();
        responseEditorPane.setEditable(false);
        responseEditorPane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        responseEditorPane.setAutoscrolls(true);
        JScrollPane responseScrollPane = new JScrollPane();
        responseScrollPane.setViewportView(responseEditorPane);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        prettyButton = new JToggleButton("Pretty", true);
        JToggleButton rawButton = new JToggleButton("Raw", false);
        prettyButton.addItemListener((ie) -> {
            renderResponse();
        });

        ButtonGroup formatGroup = new ButtonGroup();
        formatGroup.add(prettyButton);
        formatGroup.add(rawButton);
        topPanel.add(prettyButton);
        topPanel.add(rawButton);

        previewButton = new JButton("Preview");
        previewButton.setToolTipText("Open HTML response in browser");
        previewButton.setVisible(false); // Initially hidden, shown only for HTML responses
        previewButton.addActionListener((ae) -> {
            renderHtml();
        });
        topPanel.add(previewButton);

        responseScrollPane = new JScrollPane();

        JPanel responseBodyPanel = new JPanel();
        responseBodyPanel.setLayout(new BorderLayout());
        responseBodyPanel.add(topPanel, BorderLayout.NORTH);
        responseBodyPanel.add(responseScrollPane, BorderLayout.CENTER);

        responseEditorPane.setEditorKit(CloneableEditorSupport.getEditorKit(mimePath));
        responseScrollPane.setViewportView(responseEditorPane);

        responseTabbedPane.addTab("Body", responseBodyPanel);
        responseHeadersTable = new TablePanel();
        responseHeadersTable.setReadOnly();
        responseTabbedPane.addTab("Headers", responseHeadersTable);

        statusLabel = new StatusLabel();
        JLayer<JComponent> decoratedPane = new JLayer<>(responseTabbedPane, statusLabel);
        add(decoratedPane);

        lineNumberComponent = new LineNumberComponent(responseEditorPane);

        responseScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> lineNumberComponent.repaint());
        responseScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> lineNumberComponent.repaint());
        responseScrollPane.setRowHeaderView(lineNumberComponent);

    }

    private void setContentType(String contentType) {
        mimePath = MediaType.TEXT_PLAIN;
        if (contentType.startsWith(MediaType.APPLICATION_XML)) {
            mimePath = RestMediaType.XML;
        } else if (contentType.startsWith(MediaType.APPLICATION_JSON)
            || contentType.startsWith("application/javascript")) {
            mimePath = RestMediaType.JSON;
        } else if (contentType.startsWith(MediaType.TEXT_HTML)) {
            mimePath = MediaType.TEXT_HTML;
        }
        responseEditorPane.setEditorKit(CloneableEditorSupport.getEditorKit(mimePath));

        // Show Preview button only for HTML responses
        previewButton.setVisible(mimePath.equals(MediaType.TEXT_HTML));
    }

    public void showResponse(ResponseModel response) {
        this.response = response;
        setContentType(response.getContentType());
        if (response.getStatus() != null) {
            setStatus("Status: " + response.getStatus() + " " + response.getStatusText()
                    + "  Time: " + response.getElapsedTimeMs()+ " ms");
        } else {
            clearStatus();
        }
        populateHeaders();
        renderResponse();
    }

    private String formatResponse() {
        String prettyOrNotResponse = response.getBody();
        if (prettyButton.isSelected()) {
            switch (mimePath) {
                case RestMediaType.JSON:
                    try (JsonReader jsonReader = Json.createReader(new StringReader(prettyOrNotResponse))) {
                        prettyOrNotResponse = FormatUtils.jsonPrettyFormat(jsonReader.read());
                    }
                    break;
                case RestMediaType.XML:
                    prettyOrNotResponse = FormatUtils.xmlPrettyFormat(prettyOrNotResponse);
                    break;
                case MediaType.TEXT_HTML:
                    prettyOrNotResponse = FormatUtils.htmlPrettyFormat(prettyOrNotResponse);
                    break;
            }
        }

        lineNumberComponent.repaint();

        return prettyOrNotResponse;
    }

    private void renderResponse() {
        String prettyOrNotResponse = formatResponse();
        responseEditorPane.setText(prettyOrNotResponse);
        responseEditorPane.setCaretPosition(0);
    }
    
    /**
     * Render HTML response in system browser.
     */
    private void renderHtml() {
        if (response == null || response.getBody().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No HTML content to render", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Create temporary HTML file
            File tempFile = File.createTempFile("rest-response-", ".html");
            tempFile.deleteOnExit();
            Files.writeString(tempFile.toPath(), response.getBody());

            // Open in default browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(tempFile.toURI());
            } else {
                JOptionPane.showMessageDialog(this, "Desktop not supported - cannot open browser", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to render HTML: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearResponse() {
        responseEditorPane.setText("");
        responseEditorPane.setCaretPosition(0);
    }

    private void clearHeadersTable() {
        responseHeadersTable.removeAllRows();
    }

    private void setStatus(String status) {
        statusLabel.setText(status);
        revalidate();
        repaint();
    }
    
    private void populateHeaders() {
        responseHeadersTable.removeAllRows();
        for (Map.Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            responseHeadersTable.addRow(key, value);
        }
    }

    private void clearStatus() {
        statusLabel.setText("");
        revalidate();
        repaint();
    }

    public void clear() {
        previewButton.setVisible(false);
        clearResponse();
        clearStatus();
        clearHeadersTable();
    }

}
