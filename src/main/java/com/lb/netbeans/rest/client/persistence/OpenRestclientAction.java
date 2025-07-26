/*
 * Copyright 2025        Luca Bartoli <lbdevweb@gmail.com>
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
package com.lb.netbeans.rest.client.persistence;

import com.lb.netbeans.rest.client.ui.RestClientTopComponent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.windows.TopComponent;

/**
 *
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
@ActionID(
    category = "File",
    id = "com.mycompany.testopenmodule.OpenRestclientAction"
)
@ActionRegistration(
    displayName = "Apri REST Client"
)
@ActionReference(
    path = "Loaders/application/x-restclient/Actions",
    position = 100
)
public class OpenRestclientAction extends AbstractAction {

    private final FileObject file;

    public OpenRestclientAction(FileObject file) {
        super("Apri REST Client");
        this.file = file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Verifica se esiste già un'istanza aperta
        RestClientTopComponent editor = findExistingEditor(file);
        if(editor == null) {
            editor = new RestClientTopComponent(file);
            editor.open();
        }
        editor.requestActive();
    }

    private RestClientTopComponent findExistingEditor(FileObject file) {
        for(TopComponent tc : TopComponent.getRegistry().getOpened()) {
            if(tc instanceof RestClientTopComponent) {
                RestClientTopComponent rctc = (RestClientTopComponent)tc;
                if(file.equals(rctc.getFile())) {
                    return rctc;
                }
            }
        }
        return null;
    }
}