/*
 * Copyright 2025 Christian Lenz <christian.lenz@gmx.net>.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.javierllorente.netbeans.rest.client.http.editor.sidebar;

import com.javierllorente.netbeans.rest.client.Utils;
import com.javierllorente.netbeans.rest.client.editor.RestMediaType;
import com.javierllorente.netbeans.rest.client.ui.LineNumberComponent;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.openide.text.CloneableEditorSupport;

/**
 * Sidebar panel for displaying HTTP response content.
 *
 * @author Christian Lenz
 */
public class ResponseSidebarPanel extends JPanel {

    private final JEditorPane responseEditor;
    private final JTextComponent textComponent;
    private final LineNumberComponent lineNumberComponent;
    private final JButton htmlRenderButton;
    private final JButton headersButton;
    private String mimePath = MediaType.TEXT_PLAIN;
    private String response;
    private MultivaluedMap<String, Object> responseHeaders;
    private boolean visible;
    private int calculatedWidth = -1; // Cache the width once calculated

    public ResponseSidebarPanel(JTextComponent target) {
        super(new BorderLayout());
        this.textComponent = target;
        this.visible = false;

        // Create header panel with close button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        // Title label
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setBackground(new Color(240, 240, 240));
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("Response");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titlePanel.add(titleLabel);

        // Center panel with action buttons
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        centerButtonPanel.setBackground(new Color(240, 240, 240));

        // HTML Render button
        htmlRenderButton = new JButton("Render HTML");
        htmlRenderButton.setFont(htmlRenderButton.getFont().deriveFont(10f));
        htmlRenderButton.setToolTipText("Open HTML response in browser");
        htmlRenderButton.setVisible(false); // Initially hidden, shown only for HTML responses
        htmlRenderButton.addActionListener(e -> renderHtml());
        centerButtonPanel.add(htmlRenderButton);

        // Headers button
        headersButton = new JButton("Headers");
        headersButton.setFont(headersButton.getFont().deriveFont(10f));
        headersButton.setToolTipText("Show response headers");
        headersButton.addActionListener(e -> showHeaders());
        centerButtonPanel.add(headersButton);

        // Close button (simple X)
        JButton closeButton = new JButton("\u00D7");
        closeButton.setFont(closeButton.getFont().deriveFont(Font.BOLD, 16f));
        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setToolTipText("Close response view");
        closeButton.addActionListener(e -> {
            setVisible(false);
            revalidate();
            repaint();
        });

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(centerButtonPanel, BorderLayout.CENTER);
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Create readonly editor pane with syntax highlighting
        responseEditor = new JEditorPane();
        responseEditor.setEditable(false);
        responseEditor.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        responseEditor.setAutoscrolls(true);
        responseEditor.setEditorKit(CloneableEditorSupport.getEditorKit(mimePath));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(responseEditor);
        // Don't set fixed size here - let setVisible() handle it dynamically

        // Add line numbers
        lineNumberComponent = new LineNumberComponent(responseEditor);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> lineNumberComponent.repaint());
        scrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> lineNumberComponent.repaint());
        scrollPane.setRowHeaderView(lineNumberComponent);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Initially hidden
        setPreferredSize(new Dimension(0, 0));
    }

    /**
     * Set the content type and update the editor kit. Maps HTTP content types
     * to NetBeans MIME types like ResponsePanel does.
     */
    public void setContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            mimePath = MediaType.TEXT_PLAIN;
        } else if (contentType.startsWith(MediaType.APPLICATION_XML)) {
            mimePath = RestMediaType.XML;
        } else if (contentType.startsWith(MediaType.APPLICATION_JSON)
            || contentType.startsWith("application/javascript")) {
            mimePath = RestMediaType.JSON;
        } else if (contentType.startsWith(MediaType.TEXT_HTML)) {
            mimePath = MediaType.TEXT_HTML;
        } else {
            mimePath = MediaType.TEXT_PLAIN;
        }
        responseEditor.setEditorKit(CloneableEditorSupport.getEditorKit(mimePath));

        // Show HTML render button only for HTML responses
        htmlRenderButton.setVisible(mimePath.equals(MediaType.TEXT_HTML));
    }

    /**
     * Set response headers for display.
     */
    public void setResponseHeaders(MultivaluedMap<String, Object> headers) {
        this.responseHeaders = headers;
    }

    /**
     * Set the response content (raw).
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Format the response based on MIME type with pretty printing.
     */
    private String formatResponse() {
        String prettyResponse = response;
        if (response != null && !response.isEmpty()) {
            try {
                switch (mimePath) {
                    case RestMediaType.JSON:
                        try (JsonReader jsonReader = Json.createReader(new StringReader(prettyResponse))) {
                            prettyResponse = Utils.jsonPrettyFormat(jsonReader.read());
                        }
                        break;
                    case RestMediaType.XML:
                        prettyResponse = Utils.xmlPrettyFormat(prettyResponse);
                        break;
                    case MediaType.TEXT_HTML:
                        prettyResponse = Utils.htmlPrettyFormat(prettyResponse);
                        break;
                }
            } catch (Exception e) {
                // Keep original response if formatting fails
                System.err.println("Failed to format response: " + e.getMessage());
            }
        }

        lineNumberComponent.repaint();
        return prettyResponse;
    }

    /**
     * Show the formatted response in the editor.
     */
    private void showFormattedResponse() {
        if (responseEditor == null) {
            return;
        }

        try {
            String formatted = formatResponse();
            responseEditor.setText(formatted != null ? formatted : "");
            responseEditor.setCaretPosition(0);
        } catch (Exception e) {
            System.err.println("Error showing response: " + e.getMessage());
            try {
                responseEditor.setText(response != null ? response : "No response");
                responseEditor.setCaretPosition(0);
            } catch (Exception ex) {
                // Ignore
            }
        }
    }

    @Override
    public void setVisible(boolean visible) {
        try {
            this.visible = visible;
            if (visible) {
                // Calculate width only once when first shown, or use cached value
                if (calculatedWidth == -1) {
                    // Use invokeLater to ensure component is fully laid out
                    SwingUtilities.invokeLater(() -> {
                        int editorWidth = textComponent != null && textComponent.getParent() != null
                            ? textComponent.getParent().getWidth() : 800;
                        calculatedWidth = editorWidth / 2; // Always 50%
                        setPreferredSize(new Dimension(calculatedWidth, 0));
                        setSize(new Dimension(calculatedWidth, getHeight()));
                        if (getParent() != null) {
                            getParent().revalidate();
                            getParent().repaint();
                        }
                    });
                } else {
                    // Use cached width for consistent sizing
                    setPreferredSize(new Dimension(calculatedWidth, 0));
                    setSize(new Dimension(calculatedWidth, getHeight()));
                }
            } else {
                setPreferredSize(new Dimension(0, 0));
            }

            super.setVisible(visible);

            // Force parent container to relayout
            if (getParent() != null) {
                getParent().revalidate();
                getParent().repaint();
            }
        } catch (Exception e) {
            System.err.println("Error setting sidebar visibility: " + e.getMessage());
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     * Show the sidebar with the given response.
     */
    public void showResponse(String response, String contentType) {
        showResponse(response, contentType, null);
    }

    /**
     * Show the sidebar with the given response and headers.
     */
    public void showResponse(String response, String contentType, MultivaluedMap<String, Object> headers) {
        try {
            setContentType(contentType);
            setResponse(response);
            setResponseHeaders(headers);
            showFormattedResponse();
            setVisible(true);
            revalidate();
            repaint();
        } catch (Exception e) {
            System.err.println("Error showing response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Render HTML response in system browser.
     */
    private void renderHtml() {
        if (response == null || response.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No HTML content to render", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Create temporary HTML file
            File tempFile = File.createTempFile("rest-response-", ".html");
            tempFile.deleteOnExit();
            Files.writeString(tempFile.toPath(), response);

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

    /**
     * Show response headers in a dialog.
     */
    private void showHeaders() {
        if (responseHeaders == null || responseHeaders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No response headers available", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Build headers text
        StringBuilder headersText = new StringBuilder();
        headersText.append("Response Headers:\n\n");
        for (Map.Entry<String, List<Object>> entry : responseHeaders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            // Remove brackets from value list
            value = value.substring(1, value.length() - 1);
            headersText.append(key).append(": ").append(value).append("\n");
        }

        // Show in scrollable text area dialog
        JTextArea textArea = new javax.swing.JTextArea(headersText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setRows(15);
        textArea.setColumns(50);

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Response Headers", JOptionPane.INFORMATION_MESSAGE);
    }
}
