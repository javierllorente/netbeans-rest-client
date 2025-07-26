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
import java.io.IOException;
import javax.swing.AbstractAction;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 *
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
@ActionID(
    category = "File",
    id = "com.mycompany.testopenmodule.NewRestclientFileAction"
)
@ActionRegistration(
    displayName = "REST Client File"
)
@ActionReferences({
    // Vista Files
    @ActionReference(
        path = "Loaders/folder/Actions/New",
        position = 100
    ),
    // Vista Projects
    @ActionReference(
        path = "Projects/Nodes/folder/Actions/New",
        position = 100
    )
})
public class NewRestclientFileAction extends AbstractAction {

    private static final String BASE_NAME = "NuovoRestClient";
    private static final String EXT = "rstcli";
    

    public NewRestclientFileAction() {
        super("Nuovo REST Client File");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Individua la directory selezionata nel progetto
        FileObject dir = Utilities.actionsGlobalContext().lookup(FileObject.class);

        if (dir == null || !dir.isFolder()) {
            NotifyDescriptor.Message msg = new NotifyDescriptor.Message(
                "Seleziona una cartella valida per creare il file.",
                NotifyDescriptor.WARNING_MESSAGE);
            DialogDisplayer.getDefault().notify(msg);
            return;
        }

        String fileName = BASE_NAME + "." + EXT;
        try {
            FileObject newFile = dir.createData(fileName);
/*
            RestClientTopComponent tc = new RestClientTopComponent(newFile);
            tc.open();
            tc.requestActive();
*/
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
