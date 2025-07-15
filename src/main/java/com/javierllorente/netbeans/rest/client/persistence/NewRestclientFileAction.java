package com.javierllorente.netbeans.rest.client.persistence;
// 5. Azione per creare un nuovo file .rstcli dal menu File
// ------------------------------------------------------

import com.javierllorente.netbeans.rest.client.ui.RestClientTopComponent;
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


// 5. Azione per creare un nuovo file .rstcli dal menu File
// ------------------------------------------------------

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
