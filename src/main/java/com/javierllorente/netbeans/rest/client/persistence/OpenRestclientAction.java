package com.javierllorente.netbeans.rest.client.persistence;
// 4. Azione per aprire l'editor personalizzato
// --------------------------------------------

import com.javierllorente.netbeans.rest.client.ui.RestClientTopComponent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.windows.TopComponent;


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