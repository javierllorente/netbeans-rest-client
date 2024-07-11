/*
 * Copyright 2024 Javier Llorente <javier@opensuse.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javierllorente.netbeans.rest.client.parsers;

import com.javierllorente.netbeans.rest.client.Utils;
import com.javierllorente.netbeans.rest.client.ui.RestClientTopComponent;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class PostmanUtilities {
    
    private static final String SCHEMA = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

    private PostmanUtilities() {
    }
    
    public static int importRequests(Path path) throws IOException {
        int imported = 0;
        String json = Files.readString(path);

        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            
            JsonObject collection = reader.readObject();
            JsonArray itemArray = collection.getJsonArray("item");
            
            if (itemArray != null) {
                for (JsonValue jsonValue : itemArray) {
                    JsonObject itemObject = jsonValue.asJsonObject();
                    JsonObject requestObject = itemObject.getJsonObject("request");

                    SwingUtilities.invokeLater(() -> {
                        RestClientTopComponent component = new RestClientTopComponent();
                        component.open();
                        component.setUrl(requestObject.getString("url"));
                        component.setRequestMethod(requestObject.getString("method"));
                        component.requestActive();
                    });
                    
                    ++imported;
                }
            }
        }
        
        return imported;
    }
    
    public static int exportRequests(Path path) throws IOException {
        int exported = 0;
        JsonArrayBuilder items = Json.createArrayBuilder();

        for (var topComponent : TopComponent.getRegistry().getOpened()) {
            if (topComponent instanceof RestClientTopComponent) {
                RestClientTopComponent restClientTopComponent = (RestClientTopComponent) topComponent;

                JsonObject item = Json.createObjectBuilder()
                        .add("name", restClientTopComponent.getDisplayUrl())
                        .add("request", Json.createObjectBuilder()
                                .add("method", restClientTopComponent.getRequestMethod())
                                .add("url", restClientTopComponent.getUrl())
                                .build())
                        .build();
                items.add(item);
                ++exported;
            }
        }

        JsonObject json = Json.createObjectBuilder()
                .add("info", Json.createObjectBuilder()
                        .add("name", "NetBeans REST Client")
                        .add("schema", SCHEMA)
                        .build())
                .add("item", items.build())
                .build();
        Files.writeString(path, Utils.jsonPrettyFormat(json));

        return exported;
    }
    
}
