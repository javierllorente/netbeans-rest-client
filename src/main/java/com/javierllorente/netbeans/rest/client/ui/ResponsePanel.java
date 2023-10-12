/*
 * Copyright 2022-2023 Javier Llorente <javier@opensuse.org>.
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

import com.javierllorente.netbeans.rest.client.editor.RestMediaType;
import com.javierllorente.netbeans.rest.client.Utils;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.ws.rs.core.MediaType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.StringReader;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLayer;
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
    private String mimePath;
    private String response;

    public ResponsePanel() {        
        super(new BorderLayout());
        
        responseTabbedPane = new JTabbedPane();
        responseTabbedPane.setPreferredSize(new Dimension(705, 150));
        
        responseEditorPane = new JEditorPane();
        responseEditorPane.setEditable(false);
        responseEditorPane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        JScrollPane responseScrollPane = new JScrollPane();
        responseScrollPane.setViewportView(responseEditorPane);
       
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        prettyButton = new JToggleButton("Pretty", true);
        JToggleButton rawButton = new JToggleButton("Raw", false);
        prettyButton.addItemListener((ie) -> {
            showResponse();
        });
        
        ButtonGroup formatGroup = new ButtonGroup();
        formatGroup.add(prettyButton);
        formatGroup.add(rawButton);
        topPanel.add(prettyButton);
        topPanel.add(rawButton);
        
        JPanel responseBodyPanel = new JPanel();
        responseBodyPanel.setLayout(new BorderLayout());
        responseBodyPanel.add(topPanel, BorderLayout.NORTH);
        responseBodyPanel.add(responseScrollPane, BorderLayout.CENTER);
        
        responseTabbedPane.addTab("Body", responseBodyPanel);        
        responseHeadersTable = new TablePanel();
        responseHeadersTable.setReadOnly();
        responseTabbedPane.addTab("Headers", responseHeadersTable);

        statusLabel = new StatusLabel();
        JLayer<JComponent> decoratedPane = new JLayer<>(responseTabbedPane, statusLabel);
        add(decoratedPane);
        
        mimePath = MediaType.TEXT_PLAIN;
    }
    
    public void setContentType(String contentType) {
        if (contentType.startsWith(MediaType.APPLICATION_XML)) {
            mimePath = RestMediaType.XML;
        } else if (contentType.startsWith(MediaType.APPLICATION_JSON) 
                || contentType.startsWith("application/javascript")) {
            mimePath = RestMediaType.JSON;
        } else if (contentType.startsWith(MediaType.TEXT_HTML)) {
            mimePath = MediaType.TEXT_HTML;
        }
        responseEditorPane.setEditorKit(CloneableEditorSupport.getEditorKit(mimePath));
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    private String formatResponse() {
        String prettyOrNotResponse = response;
        if (prettyButton.isSelected()) {
            switch (mimePath) {
                case RestMediaType.JSON:
                    try (JsonReader jsonReader = Json.createReader(new StringReader(prettyOrNotResponse))) {
                        prettyOrNotResponse = Utils.jsonPrettyFormat(jsonReader.read());
                    }
                break;
                case RestMediaType.XML:
                    prettyOrNotResponse = Utils.xmlPrettyFormat(prettyOrNotResponse);
                    break;
            }
        }
        return prettyOrNotResponse;
    }
    
    public void showResponse() {
        String prettyOrNotResponse = formatResponse();
        responseEditorPane.setText(prettyOrNotResponse);
        responseEditorPane.setCaretPosition(0);
    }
    
    public void clearResponse() {
        responseEditorPane.setText("");
        responseEditorPane.setCaretPosition(0);
    }
    
    public void addHeader(String key, String value) {
        responseHeadersTable.addRow(key, value);
    }
    
    public void clearHeadersTable() {
        responseHeadersTable.removeAllRows();
    }
    
    public void setStatus(String status) {
        statusLabel.setText(status);
        revalidate();
        repaint();
    }
    
    public void clearStatus() {
        statusLabel.setText("");
        revalidate();
        repaint();
    }
    
    public void clear() {
        clearResponse();
        clearStatus();
        clearHeadersTable();
    }

}
