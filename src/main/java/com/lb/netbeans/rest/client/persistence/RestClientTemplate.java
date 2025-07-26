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

import com.lb.netbeans.rest.client.RestClient;
import com.lb.netbeans.rest.client.ui.RestClientTopComponent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.netbeans.api.templates.CreateDescriptor;
import org.netbeans.api.templates.CreateFromTemplateHandler;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */

@TemplateRegistration(
    folder = "Other",
    displayName = "REST Client File",
    description = "template-rest-client.html",
    content = "template-rest-client.rstcli",
    iconBase = "com/lb/netbeans/rest/client/restservice.png"
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