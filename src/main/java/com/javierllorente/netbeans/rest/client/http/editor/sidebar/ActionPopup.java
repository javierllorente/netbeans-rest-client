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
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openide.util.ImageUtilities;
import org.openide.util.RequestProcessor;

/**
 * Popup menu for running and debugging tasks.
 */
public class ActionPopup {

    private final IRequestProcessor requestProcessor;
    private static final RequestProcessor RP = new RequestProcessor("RequestProcessor", 1, true);

    public ActionPopup(IRequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    public JPopupMenu createPopupMenu(Request request) {
        Icon runIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/project/ui/resources/runProject.png", false);
//        Icon debugIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/debugger/resources/debugProject.png", false);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem runItem = new JMenuItem("Call '" + request.getRequestLineText(), runIcon);
//        JMenuItem debugItem = new JMenuItem("Debug '" + request.getName() + "' task", debugIcon);

        // Verwende executeItem für die Ausführung von "Run" und "Debug"
        runItem.addActionListener((ActionEvent e) -> {
            System.out.println("Call " + request.getRequestLineText());
            executeItem(requestProcessor, request.getrequestContext(), false);  // Run task
        });

//        debugItem.addActionListener((ActionEvent e) -> {
//            System.out.println("Debugging task: " + task.getName());
//            executeItem(requestProcessor, task.getName(), true);  // Debug task
//        });
        popupMenu.add(runItem);
//        popupMenu.add(debugItem);

        return popupMenu;
    }

    public static void executeItem(final IRequestProcessor taskProcessor, final HTTPParser.RequestContext requestContext, final boolean debug) {
        taskProcessor.callRequest(requestContext);
    }
}
