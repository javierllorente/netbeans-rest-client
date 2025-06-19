package com.javierllorente.netbeans.rest.client.persistence;
// ------------------------------------------------------------------
// 6. Wizard New File... (File Template)

import com.javierllorente.netbeans.rest.client.RestClient;
import com.javierllorente.netbeans.rest.client.ui.RestClientTopComponent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.netbeans.api.templates.CreateDescriptor;
import org.netbeans.api.templates.CreateFromTemplateHandler;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.filesystems.FileObject;

// ------------------------------------------------------------------

@TemplateRegistration(
    folder = "Other",
    displayName = "REST Client File",
    description = "template-rest-client.html",
    content = "template-rest-client.rstcli",
    iconBase = "com/javierllorente/netbeans/rest/client/restservice.png"
)
public final class RestClientTemplate  extends CreateFromTemplateHandler {

    @Override
    protected boolean accept(CreateDescriptor desc) {
        // Accetta tutti i template con estensione .restclient
        return desc.getTemplate().getExt().equals("rstcli");
    }

    @Override
    protected List<FileObject> createFromTemplate(CreateDescriptor desc) throws IOException {
        // Crea il file dal template
        FileObject createdFile = desc.getTarget().createData(
            desc.getProposedName(),
            "rstcli"
        );
        
        // Aggiungi contenuto iniziale
        try (OutputStream out = createdFile.getOutputStream()) {
            Properties p = new Properties();
            p.setProperty(RestClientTopComponent.VERSION_PROPERTY, "1.0");
            p.setProperty(RestClientTopComponent.URL_PROPERTY, "http://example.com");
            p.setProperty(RestClientTopComponent.REQUEST_METHOD_PROPERTY, "GET");
            p.setProperty(RestClientTopComponent.AUTH_TYPE_PROPERTY, RestClient.NO_AUTH);
            p.setProperty(RestClientTopComponent.USERNAME_PROPERTY, "");
            p.setProperty(RestClientTopComponent.HEADERS_PROPERTY, "");
            p.setProperty(RestClientTopComponent.BODY_TYPE_PROPERTY, "None");
            p.setProperty(RestClientTopComponent.BODY_PROPERTY, "");
            p.store(out, "Rest Client Configuration");
        }
        
        // Restituisce la lista dei file creati
        return Collections.singletonList(createdFile);
    }
}