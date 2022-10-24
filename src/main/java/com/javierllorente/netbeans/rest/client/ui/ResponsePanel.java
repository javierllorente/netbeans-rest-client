/*
 * Copyright 2022 Javier Llorente <javier@opensuse.org>.
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.openide.text.CloneableEditorSupport;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class ResponsePanel extends JPanel {

    private final JTabbedPane responseTabbedPane;
    private final JEditorPane responsePane;
    private final TablePanel responseHeadersTable;
    private final StatusLabel statusLabel;

    public ResponsePanel() {        
        super(new BorderLayout());
        
        responseTabbedPane = new JTabbedPane();
        responseTabbedPane.setPreferredSize(new Dimension(705, 150));
        
        responsePane = new JEditorPane();
        responsePane.setEditable(false);
        responsePane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        JScrollPane responseScrollPane = new JScrollPane();
        responseScrollPane.setViewportView(responsePane);
        responseTabbedPane.addTab("Body", responseScrollPane);
        
        responseHeadersTable = new TablePanel();
        responseHeadersTable.setReadOnly();
        responseTabbedPane.addTab("Headers", responseHeadersTable);

        statusLabel = new StatusLabel();
        JLayer<JComponent> decoratedPane = new JLayer<>(responseTabbedPane, statusLabel);
        add(decoratedPane);
    }
    
    public void setContentType(String contentType) {
        String mimePath = "text/plain";

        if (contentType.startsWith("application/xml")) {
            mimePath = "text/xml";
        } else if (contentType.startsWith("application/json")) {
            mimePath = "text/x-json";
        } else if (contentType.startsWith("application/javascript")) {
            mimePath = "application/json";
        } else if (contentType.startsWith("text/html")) {
            mimePath = "text/html";
        }

        responsePane.setEditorKit(CloneableEditorSupport.getEditorKit(mimePath));
    }
    
    public void setResponse(String response) {
        responsePane.setText(response);
        responsePane.setCaretPosition(0);
    }
    
    public void clearResponse() {
        responsePane.setText("");
        responsePane.setCaretPosition(0);
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
