package com.javierllorente.netbeans.rest.client.persistence;

import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;

// 2. DataObject e Registrazione MIME
// -----------------------------------

@MultiDataObject.Registration(
    displayName = "File REST Client",
    iconBase = "com/javierllorente/netbeans/rest/client/restservice.png",
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

    public RestClientDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        // Registra l'editor per apertura con doppio click
        registerEditor("application/x-restclient", true);
    }
    
    @Override
    protected int associateLookup() {
        // Permette di associare il DataObject alla Lookup per SaveCookie, ecc.
        return 1;
    }
    
    
/*
    @Override
    protected Node createNodeDelegate() {
        Node original = super.createNodeDelegate();
        // Avvolge il nodo originale per aggiungere la nostra azione
        for(Action a : original.getActions(true))
            log.info("[RestClientDataObject] OrAction:" + a.toString());
        // Avvolge il nodo originale per aggiungere la nostra azione extra
        return new FilterNode(original) {
            @Override
            public Action getPreferredAction() {
                // Doppio clic apre il nostro editor custom
                return new OpenRestclientAction(getPrimaryFile());
            }

            @Override
            public Action[] getActions(boolean context) {
                // Raccogli azioni standard escludendo eventuali voci del nostro custom già registrate
                List<Action> list = new ArrayList<>();
                for (Action a : super.getActions(context)) {
                    Object name = a.getValue(Action.NAME);
                    if ("Apri REST Client".equals(name)) {
                        continue; // escludi azioni duplicate
                    }
                    list.add(a);
                }
                // Aggiungi separatore e la nostra azione custom in coda
                list.add(null);
                list.add(new OpenRestclientAction(getPrimaryFile()));
                return list.toArray(new Action[0]);
            }
        };
    }*/
    
    /*
    @Override
    protected Node createNodeDelegate() {
        // Node con azioni standard più la nostra OpenRestclientAction come azione preferita
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
        node.setDisplayName(getPrimaryFile().getNameExt());
        node.setIconBaseWithExtension("com/javierllorente/netbeans/rest/client/restservice.png");
        node.setShortDescription(getPrimaryFile().getPath());
        node.setCookie(new OpenRestclientAction(getPrimaryFile()));
        return new FilterNode(node) {
            @Override
            public Action getPreferredAction() {
                return new OpenRestclientAction(getPrimaryFile());
            }

            @Override
            public Action[] getActions(boolean context) {
                // includi azioni standard
                Action[] std = super.getActions(context);
                List<Action> list = new ArrayList<>(Arrays.asList(std));
                list.add(null); // separator
                list.add(new OpenRestclientAction(getPrimaryFile()));
                return list.toArray(new Action[0]);
            }
        };
    }*/
/*
    @Override
    protected Node createNodeDelegate() {
        System.out.println("[RestClientDataObject] Creazione nodo delegate per: " + getPrimaryFile().getPath());
        return new AbstractNode(Children.LEAF, Lookups.singleton(this)) {
            @Override
            public Action[] getActions(boolean context) {
                System.out.println("[RestClientDataObject] Azioni richieste per: " + getPrimaryFile().getPath());
                return new Action[]{new OpenRestclientAction(getPrimaryFile())};
            }
        };
    }*/
}