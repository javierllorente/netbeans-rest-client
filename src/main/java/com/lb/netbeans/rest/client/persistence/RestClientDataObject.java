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
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Properties;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.lib.editor.util.PriorityListenerList;
import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */

@NbBundle.Messages({
    "LB_REST_FILE_LOADER=Files of RestClient"
})
// 2. DataObject e Registrazione MIME
// -----------------------------------

@MultiDataObject.Registration(
    displayName = "File REST Client",
    iconBase = "com/lb/netbeans/rest/client/restservice.png",
    mimeType = "application/x-restclient",
    position = 300
)
@MIMEResolver.Registration(
    displayName = "REST Client Resolver",
    resource = "rest-mime-resolver.xml",
    position = 100
)

@ActionReferences({
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 150,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/application/x-restclient/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class RestClientDataObject extends MultiDataObject {
    
    @MultiViewElement.Registration(
            displayName = "#LB_REST_FILE_EDITOR",
            iconBase = "com/lb/netbeans/rest/client/restservice.png",
            mimeType = "application/x-restclient",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "RSTCLI",
            position = 2000)
    @NbBundle.Messages("LB_REST_FILE_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
    
    
    private final UndoRedo.Manager undoRedoManager;
    private final DocumentListener documentListener;
    private RestClientTopComponent restClientTopComponent;

    public RestClientDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        undoRedoManager = new UndoRedo.Manager() {
            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                updateView();
            }

            @Override
            protected void undoTo(UndoableEdit edit) throws CannotUndoException {
                super.undoTo(edit);
                updateView();
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                updateView();
            }

            @Override
            protected void redoTo(UndoableEdit edit) throws CannotRedoException {
                super.redoTo(edit);
                updateView();
            }

            @Override
            public void undoOrRedo() throws CannotRedoException, CannotUndoException {
                super.undoOrRedo();
                updateView();
            }

            private void updateView() {
                if (restClientTopComponent != null && restClientTopComponent.isVisible()) {
                    //restClientTopComponent.updateTable();
                }
            }
        };
        documentListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateView();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateView();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateView();
            }

            private void updateView() {
                if (restClientTopComponent != null && restClientTopComponent.isVisible()) {
                    //restClientTopComponent.updateTable();
                }
            }
        };
        // Registra l'editor per apertura con doppio click
        registerEditor("application/x-restclient", true);

        this.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if (evt.getPropertyName().equals(DataObject.PROP_MODIFIED) && ((Boolean) evt.getNewValue())) {
                initDocument();
            }
        });
        
    }

    
    @Override
    protected int associateLookup() {
        // Permette di associare il DataObject alla Lookup per SaveCookie, ecc.
        return super.associateLookup();
    }

    public void setRestClientTopComponent(RestClientTopComponent restClientTopComponent) {
        this.restClientTopComponent = restClientTopComponent;
        getCookieSet().add(restClientTopComponent);
    }
    
    /**
     * Init document listeners
     */
    public void initDocument() {
        Lookup lookup = getCookieSet().getLookup();
        DataEditorSupport dataEditorSupport = lookup.lookup(DataEditorSupport.class);
        NbEditorDocument document = null;
        if (dataEditorSupport.isDocumentLoaded()) {
            document = (NbEditorDocument) dataEditorSupport.getDocument();
        } else {
            try {
                document = (NbEditorDocument) dataEditorSupport.openDocument();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        if (document != null)
            initDocument(document);
    }
    
    private void initDocument(NbEditorDocument document) {
        UndoableEditListener[] undoableEditListeners = document.getUndoableEditListeners();
        boolean found = false;
        if (undoableEditListeners.length > 0) {
            for (UndoableEditListener uel : undoableEditListeners) {
                if (uel.equals(undoRedoManager)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            document.addUndoableEditListener(undoRedoManager);
        }

        DocumentListener[] documentListeners = document.getDocumentListeners();
        found = false;
        if (documentListeners.length > 0) {
            loopDocumentListener:
            for (DocumentListener dl : documentListeners) {
                if (dl.equals(documentListener)) {
                    found = true;
                    break;
                } else if (dl instanceof PriorityListenerList pll) {
                    EventListener[][] listenersArray = pll.getListenersArray();
                    for (EventListener[] row : listenersArray) {
                        for (EventListener el : row) {
                            if (el.equals(documentListener)) {
                                found = true;
                                break loopDocumentListener;
                            }
                        }
                    }
                }
            }
        }
        if (!found) {
            document.addDocumentListener(documentListener);
        }
    }
    
    public void readFile() {
        try {
            Lookup lookup = getCookieSet().getLookup();
            DataEditorSupport dataEditorSupport = lookup.lookup(DataEditorSupport.class);
            NbEditorDocument document = null;
            if (dataEditorSupport.isDocumentLoaded()) {
                document = (NbEditorDocument) dataEditorSupport.getDocument();
            } else {
                try {
                    document = (NbEditorDocument) dataEditorSupport.openDocument();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (document != null) {
                initDocument(document);

                int length = document.getLength();
                if (length > 0) {
                    try {
                        String text = document.getText(0, length);
                        StringReader sr = new StringReader(text);
                        Properties props = new Properties();

                        props.load(sr);
                        
                        
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                }
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
}