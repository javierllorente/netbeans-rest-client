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

import com.javierllorente.netbeans.rest.client.ui.ResponsePanel;
import jakarta.ws.rs.core.MultivaluedMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * Sidebar panel for displaying HTTP response content.
 *
 * @author Christian Lenz
 */
public class ResponseSidebarPanel extends JPanel {

    private final ResponsePanel responsePanel;
    private final JTextComponent textComponent;
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
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Use the ResponsePanel component which already has all the functionality
        responsePanel = new ResponsePanel();

        add(headerPanel, BorderLayout.NORTH);
        add(responsePanel, BorderLayout.CENTER);

        // Initially hidden
        setPreferredSize(new Dimension(0, 0));
    }

    /**
     * Set the content type and update the editor kit. Maps HTTP content types
     * to NetBeans MIME types like ResponsePanel does.
     */
    public void setContentType(String contentType) {
        responsePanel.setContentType(contentType);
    }

    /**
     * Set response headers for display.
     */
    public void setResponseHeaders(MultivaluedMap<String, Object> headers) {
        if (headers != null) {
            for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue().toString();
                responsePanel.addHeader(key, val);
            }
        }
    }

    /**
     * Set the response content (raw).
     */
    public void setResponse(String response) {
        responsePanel.setResponse(response);
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
            responsePanel.clear();
            setContentType(contentType);
            setResponse(response);
            setResponseHeaders(headers);
            responsePanel.showResponse();
            setVisible(true);
            revalidate();
            repaint();
        } catch (Exception e) {
            System.err.println("Error showing response: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
