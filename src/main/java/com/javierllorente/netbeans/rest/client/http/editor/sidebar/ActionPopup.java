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

import com.javierllorente.netbeans.rest.client.http.editor.sidebar.request.IRequestProcessor;
import com.javierllorente.netbeans.rest.client.http.editor.sidebar.request.Request;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import com.javierllorente.netbeans.rest.client.ui.RestClientTopComponent;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.openide.util.ImageUtilities;
import org.openide.windows.WindowManager;

/**
 * Popup menu for running and debugging tasks.
 */
public class ActionPopup {

    private final IRequestProcessor requestProcessor;

    public ActionPopup(IRequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    public JPopupMenu createPopupMenu(Request request) {
        Icon runIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/project/ui/resources/runProject.png", false);
        Icon uiIcon = ImageUtilities.loadImageIcon("com/javierllorente/netbeans/rest/client/http/editor/restservice.png", false);

        JPopupMenu popupMenu = new JPopupMenu();

        // Call action
        JMenuItem runItem = new JMenuItem("Call '" + request.getRequestLineText(), runIcon);
        runItem.addActionListener((ActionEvent e) -> {
            System.out.println("Call " + request.getRequestLineText());
            requestProcessor.callRequest(request.getrequestContext());
        });
        popupMenu.add(runItem);

        // Open in UI action
        JMenuItem openInUIItem = new JMenuItem("Open in UI", uiIcon);
        openInUIItem.addActionListener((ActionEvent e) -> {
            openInUI(request.getrequestContext());
        });
        popupMenu.add(openInUIItem);

        return popupMenu;
    }

        return popupMenu;
    }

    /**
     * Opens the RestClientTopComponent and populates it with data from the HTTP request.
     */
    private void openInUI(HTTPParser.RequestContext requestContext) {
        if (requestContext == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            RestClientTopComponent tc = (RestClientTopComponent) WindowManager.getDefault().findTopComponent("RestClientTopComponent");
            if (tc != null) {
                tc.open();
                tc.requestActive();

                // Extract request data
                HTTPParser.RequestLineContext requestLine = requestContext.requestLine();
                if (requestLine != null) {
                    // Extract method
                    String method = "GET"; // Default
                    if (requestLine.METHOD() != null) {
                        method = requestLine.METHOD().getText();
                    }

                    // Extract URL/target
                    String url = "";
                    HTTPParser.RequestTargetContext target = requestLine.requestTarget();
                    if (target != null) {
                        url = target.getText();
                    }

                    // TODO: Extract headers from requestContext.requestHeaders()
                    // TODO: Extract body from requestContext.requestBodySection()

                    // Populate the UI
                    tc.setRequestMethod(method);
                    tc.setUrl(url);
                }
            }
        });
    }
}
