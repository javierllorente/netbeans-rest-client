/*
 * Copyright (C) 2025 Christian Lenz <christian.lenz@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javierllorente.netbeans.rest.client.ui.actions;

import com.javierllorente.netbeans.rest.client.UserAgent;
import com.javierllorente.netbeans.rest.client.util.HttpFileUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Tools",
    id = "com.javierllorente.netbeans.rest.client.ui.actions.OpenInEditorAction"
)
@ActionRegistration(
    displayName = "#CTL_OpenInEditorAction",
    iconBase = "com/javierllorente/netbeans/rest/client/http/editor/http.png"
)
@ActionReference(path = "Menu/Tools/RestClient", position = 200)
@Messages("CTL_OpenInEditorAction=Open in Editor")
public final class OpenInEditorAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Create example HTTP request content
            String httpContent = "### GET request to example petstore swagger server\n"
                    + "GET https://petstore.swagger.io/v2/pet/findByStatus?status=available\n"
                    + "User-Agent: " + UserAgent.FULL + "\n"
                    + "\n"
                    + "###\n";
            HttpFileUtils.createAndOpenHttpFile(httpContent, 0);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
