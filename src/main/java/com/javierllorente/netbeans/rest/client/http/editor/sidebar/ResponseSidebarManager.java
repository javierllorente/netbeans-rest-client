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

import com.javierllorente.netbeans.rest.client.ResponseModel;
import java.util.WeakHashMap;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * Manager for controlling the response sidebar programmatically.
 * This singleton allows RequestProcessor and other classes to show responses
 * in the sidebar.
 *
 * @author Christian Lenz
 */
public class ResponseSidebarManager {

    private static final ResponseSidebarManager INSTANCE = new ResponseSidebarManager();
    private final WeakHashMap<JTextComponent, ResponseSidebarPanel> sidebars;

    private ResponseSidebarManager() {
        this.sidebars = new WeakHashMap<>();
    }

    public static ResponseSidebarManager getInstance() {
        return INSTANCE;
    }

    /**
     * Register a sidebar panel for a specific text component.
     * Called by ResponseSideBarFactory when creating sidebars.
     */
    public void registerSidebar(JTextComponent textComponent, ResponseSidebarPanel panel) {
        sidebars.put(textComponent, panel);
    }

    /**
     * Show a response in the sidebar for the given text component with headers.
     *
     * @param textComponent The editor component
     * @param response The response content
     */
    public void showResponse(JTextComponent textComponent, ResponseModel response) {
        if (textComponent == null) {
            return;
        }

        // Ensure UI updates happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ResponseSidebarPanel panel = sidebars.get(textComponent);
            if (panel != null) {
                try {
                    panel.showResponse(response);
                } catch (Exception e) {
                    // Log but don't propagate exceptions
                    System.err.println("Error showing response in sidebar: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Hide the sidebar for the given text component.
     */
    public void hideSidebar(JTextComponent textComponent) {
        if (textComponent == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ResponseSidebarPanel panel = sidebars.get(textComponent);
            if (panel != null) {
                try {
                    panel.setVisible(false);
                } catch (Exception e) {
                    System.err.println("Error hiding sidebar: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
