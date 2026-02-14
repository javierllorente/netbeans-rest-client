/*
 * Copyright 2022-2026 Javier Llorente <javier@opensuse.org>.
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

import com.javierllorente.netbeans.rest.client.ui.RestClientOptionsPanel;
import jakarta.json.stream.JsonGenerator;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.openide.util.NbPreferences;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class RestClient {
    public static final String NO_AUTH = "No Auth";
    public static final String BEARER_TOKEN = "Bearer Token";
    public static final String BASIC_AUTH = "Basic Auth";
    
    private static final Logger logger = Logger.getLogger(RestClient.class.getName());
    private Client client;
    private String authType;
    private String username;
    private String password;
    private String method;
    private String uri;
    private MultivaluedMap<String, String> headers;
    private String body;
    private String bodyType;

    public RestClient() {
        Preferences preferences = NbPreferences.forModule(RestClientOptionsPanel.class);
        preferences.addPreferenceChangeListener((pce) -> {
            if (pce.getKey().equals(RestClientOptionsPanel.VERIFY_SSL_CERTIFICATES)) {
                buildClient(Boolean.parseBoolean(pce.getNewValue()));
            }
        });
        buildClient(preferences.getBoolean(RestClientOptionsPanel.VERIFY_SSL_CERTIFICATES, true));
        authType = NO_AUTH;
        body = "";
        bodyType = "None";
        method = "GET";
    }

    private void buildClient(boolean verifySslCertificates) {
        ClientConfig config = new ClientConfig()
                .connectorProvider(new ApacheConnectorProvider())
                .property(ClientProperties.CONNECT_TIMEOUT, 20000)
                .property(ClientProperties.FOLLOW_REDIRECTS, true)
                .property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true)
                .property(JsonGenerator.PRETTY_PRINTING, true)
                .register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.HEADERS_ONLY, 8192));
        client = verifySslCertificates
                ? ClientBuilder.newBuilder().withConfig(config).build()
                : ClientBuilder.newBuilder().sslContext(getSslContext()).withConfig(config).build();
    }
    
    private SSLContext getSslContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sslContext;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public void setUri(String target) {
        this.uri = target;
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
    
    public String getBody() {
        return this.body;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBodyType() {
        return this.bodyType;
    }

    private String getConnectionInfo(URI uri, int status, long time) {
        return "URL: " + uri.toString() + ", status: " + status + ", time: " + time + " ms";
    }

    public ResponseModel request(String resource, String method) {
        resource = resource.trim();
        if (resource.matches("^[a-zA-Z]+://.*")) {
            if (!(resource.startsWith("http://") || resource.startsWith("https://"))) {
                return new ResponseModel("Unsupported protocol");
            }
        } else {
            resource = "http://" + resource;
        }
        uri = resource;
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
        
        ResponseModel responseModel;
        try (Response response = invoke(invocationBuilder, method)) {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            int status = response.getStatus();
            String statusText = response.getStatusInfo().toEnum().toString();
            logger.info(getConnectionInfo(target.getUri(), status, elapsedTime));

            MultivaluedMap<String, Object> responseHeaders = response.getHeaders();
            response.bufferEntity();
            String responseBody = response.readEntity(String.class);
            responseModel = new ResponseModel(resource, status, statusText, responseBody, responseHeaders, elapsedTime);
        } catch (ProcessingException ex) {
            logger.warning(ex.getMessage());
            String error = (ex.getMessage().contains("PKIX path building failed"))
                    ? "Could not get response: failed to verify SSL certificate\n"
                    + "SSL certificate verification is enabled. "
                    + "You may disable it under Tools->Options->Miscellaneous->REST Client"
                    : ex.getMessage();
            return new ResponseModel(error);
        }
        
        return responseModel;
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
    
    public String getUri() {
        return uri;
    }

}
