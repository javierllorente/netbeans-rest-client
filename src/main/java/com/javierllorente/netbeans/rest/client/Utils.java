/*
 * Copyright 2022-2023 Javier Llorente <javier@opensuse.org>.
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
package com.javierllorente.netbeans.rest.client;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.openide.util.Exceptions;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class Utils {

    private Utils() {
    }

    public static String jsonPrettyFormat(JsonStructure jsonStructure) {
        Map<String, Object> map = new HashMap<>();
        map.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(map);
        StringWriter stringWriter = new StringWriter();
        try (final JsonWriter jsonWriter = writerFactory.createWriter(stringWriter)) {            
            if (jsonStructure instanceof JsonObject) {
                jsonWriter.writeObject((JsonObject) jsonStructure);
            } else {
                jsonWriter.writeArray((JsonArray) jsonStructure);
            }
        }
        return stringWriter.toString();
    }

    public static String xmlPrettyFormat(String input) {
        Source xmlInput = new StreamSource(new StringReader(input));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
        return xmlOutput.getWriter().toString();
    }
    
}
