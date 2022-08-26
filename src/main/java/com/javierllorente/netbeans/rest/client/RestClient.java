/*
 * Copyright 2022 Javier Llorente <javier@opensuse.org>.
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

import jakarta.json.JsonObject;
import jakarta.json.stream.JsonGenerator;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class RestClient {
    public static final String NO_AUTH = "No Auth";
    public static final String BEARER_TOKEN = "Bearer Token";
    public static final String BASIC_AUTH = "Basic Auth";
    
    private static final Logger logger = Logger.getLogger(RestClient.class.getName());
    private final Client client;
    private String authType;
    private String username;
    private String password;
    private MultivaluedMap<String, String> headers;
    private String body;
    private String bodyType;
    private MultivaluedMap<String, Object> responseHeaders;
    private int status;
    private String statusText;
    private long elapsedTime;

    public RestClient() {

        client = ClientBuilder.newClient(new ClientConfig()
                .property(ClientProperties.CONNECT_TIMEOUT, 20000)
                .property(ClientProperties.FOLLOW_REDIRECTS, true)
                .property(JsonGenerator.PRETTY_PRINTING, true))
                .register(new LoggingFeature(logger,
                        Level.INFO,
                        LoggingFeature.Verbosity.HEADERS_ONLY,
                        8192));
        authType = NO_AUTH;
        body = "";
        bodyType = "None";
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
    
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public MultivaluedMap<String, Object> getResponseHeaders() {
        return responseHeaders;
    }
    
    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    private String getConnectionInfo(URI uri, int status, long time) {
        return "URL: " + uri.toString() + ", status: " + status + ", time: " + time + " ms";
    }

    public String request(String resource, String method)
            throws ClientErrorException, ServerErrorException, ProcessingException {
        long startTime = System.currentTimeMillis();
        WebTarget target = client.target(resource);

        switch (authType) {
            case NO_AUTH:
            case BEARER_TOKEN:
                // Token must be added to the headers
                break;
            case BASIC_AUTH:
                HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
                target.register(feature);
                break;
            default:
                throw new AssertionError("Unknown auth type " + authType);
        }
        
        Invocation.Builder invocationBuilder = target.request();
        setMediaTypeAccepted(invocationBuilder);

        if (headers != null) {
            setRequestHeaders(invocationBuilder);
        }    
        
        String str;
        try (Response response = invoke(invocationBuilder, method)) {
            long endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            status = response.getStatus();
            statusText = response.getStatusInfo().toEnum().toString();
            logger.info(getConnectionInfo(target.getUri(), status, elapsedTime));

            responseHeaders = response.getHeaders();
            response.bufferEntity();

            if (response.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)) {
                JsonObject jsonObject = response.readEntity(JsonObject.class);
                str = Utils.jsonPrettyFormat(jsonObject);
            } else {
                str = response.readEntity(String.class);
            }
        }
        
        return str;
    }
    
    private Response invoke(Invocation.Builder invocationBuilder, String method) {
        switch (method) {
            case HttpMethod.GET:
                return invocationBuilder.get();
            case HttpMethod.POST:
                return invocationBuilder.post(Entity.entity(body, getBodyMediaType()));
            case HttpMethod.PUT:
                return invocationBuilder.put(Entity.entity(body, getBodyMediaType()));
            case HttpMethod.PATCH:
                return invocationBuilder.build("PATCH", Entity.entity(body, getBodyMediaType(method))).invoke();
            case HttpMethod.DELETE:
                return invocationBuilder.delete();
            default:
                throw new AssertionError("Unknown request method " + method);   
        }
    }

    private void setMediaTypeAccepted(Invocation.Builder invocationBuilder) {
        invocationBuilder.accept(MediaType.APPLICATION_JSON_TYPE,
                MediaType.APPLICATION_XML_TYPE, MediaType.TEXT_PLAIN_TYPE);
    }

    private void setRequestHeaders(Invocation.Builder invocationBuilder) {

        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String key = header.getKey();
            List<String> values = header.getValue();
            for (String value : values) {
                invocationBuilder.header(key, value);
            }
        }
    }
    
    private MediaType getBodyMediaType() {
        return getBodyMediaType("");
    }
    
    private MediaType getBodyMediaType(String method) {
        MediaType mediaType = null;

        switch (bodyType) {
            case "None":
            case "Text":
                mediaType = MediaType.TEXT_PLAIN_TYPE;
                break;
            case "JSON":
                mediaType = method.equals(HttpMethod.PATCH)
                        ? MediaType.APPLICATION_JSON_PATCH_JSON_TYPE
                        : MediaType.APPLICATION_JSON_TYPE;
                break;
            case "XML":
                mediaType = MediaType.APPLICATION_XML_TYPE;
                break;
            default:
                throw new AssertionError("Unknown body type " + bodyType);
        }

        return mediaType;
    }

}
