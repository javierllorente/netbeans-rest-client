/*
 * Copyright 2022-2023 Javier Llorente <javier@opensuse.org>.
 * Copyright 2025        Luca Bartoli <lbdevweb@gmail.com>
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
package com.lb.netbeans.rest.client;

import com.lb.netbeans.rest.client.ui.AuthPanel;
import com.lb.netbeans.rest.client.ui.RestClientOptionsPanel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
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
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
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
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.swing.JOptionPane;
import org.openide.awt.HtmlBrowser;
import org.openide.util.Exceptions;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javier Llorente
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
public class RestClient {
    public static final String NO_AUTH = "No Auth";
    public static final String BEARER_TOKEN = "Bearer Token";
    public static final String BASIC_AUTH = "Basic Auth";
    
    public static final String DEFAULT_CALLBACK_URL = "http://localhost:8080/";
    
    private static final Logger logger = Logger.getLogger(RestClient.class.getName());
    private Client client;
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
            str = response.readEntity(String.class);
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
    
    public String getClientCredentialsTokenOnlyBody(String tokenUrl, String clientId, 
        String clientSecret, String scope) throws ProcessingException {
    
        WebTarget target = client.target(tokenUrl);

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("scope", scope);

        Response response = target.request()
                .post(Entity.form(formData));

        if(response.getStatus() == 200) {
            JsonObject json = response.readEntity(JsonObject.class);
            return json.getString("access_token");
        } else {
            throw new ProcessingException("Error getting token: " 
                    + response.getStatusInfo().getReasonPhrase());
        }
    }
    
    public String getClientCredentialsTokenHeader(String tokenUrl, String clientId, 
        String clientSecret, String scope) throws ProcessingException {

        WebTarget target = client.target(tokenUrl);

        // Codifica le credenziali in Base64 per l'header Basic Auth
        String credentials = clientId + ":" + clientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("scope", scope);

        Response response = target.request()
                .header("Authorization", "Basic " + base64Credentials)
                .post(Entity.form(formData));

        if(response.getStatus() == Response.Status.OK.getStatusCode()) {
            JsonObject json = response.readEntity(JsonObject.class);
            return json.getString("access_token");
        } else {
            throw new ProcessingException("Error getting token: " 
                    + response.getStatusInfo().getReasonPhrase());
        }
    }
    
    public void handlePKCEFlow(AuthPanel authPanel,String authUrl,String callbackUrl,
            String clientId,String codeVerifier,String codeChallenge,String scope, String accessTokenUrl) {
        
        if (callbackUrl == null || callbackUrl.isEmpty()) {
            callbackUrl = DEFAULT_CALLBACK_URL;
            authPanel.setCallbackUrl(callbackUrl); // Aggiorna l'UI con il valore di default
        }

        try {
            // Apre il browser per l'autenticazione
            String state = UUID.randomUUID().toString();
            String authorizationUrl = authUrl + "?response_type=code"
                    + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&redirect_uri=" + URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8)
                    + "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8)
                    + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8)
                    + "&code_challenge=" + URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8)
                    + "&code_challenge_method=S256";

            HtmlBrowser.URLDisplayer.getDefault().showURL(new URL(authorizationUrl));

            // Avvia un server temporaneo per catturare la callback
            startCallbackServer(authPanel, callbackUrl, state, codeVerifier, clientId, accessTokenUrl);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(authPanel, 
                "Errore durante l'autenticazione PKCE: " + ex.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startCallbackServer(AuthPanel authPanel, String callbackUrl, String state, String codeVerifier, String clientId, String accessTokenUrl) {
        try {
            URI uri = new URI(callbackUrl);
            int port = uri.getPort();
            if (port == -1) port = 80;

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                try {
                    Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());

                    if (!params.getOrDefault("state", "").equals(state)) {
                        sendResponse(exchange, 400, "Invalid state");
                        return;
                    }

                    String code = params.get("code");
                    if (code == null || code.isEmpty()) {
                        sendResponse(exchange, 400, "Code parameter missing");
                        return;
                    }

                    // Ottieni il token
                    String token = exchangeCodeForToken(
                        accessTokenUrl,
                        code,
                        codeVerifier,
                        clientId,
                        callbackUrl
                    );

                    authPanel.setToken(token);
                    sendResponse(exchange, 200, "Authentication successful! You can close this window.");

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    server.stop(0);
                }
            });
            server.start();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public String exchangeCodeForToken(String tokenUrl, String code, String codeVerifier, 
                                  String clientId, String redirectUri) throws Exception {
        WebTarget target = client.target(tokenUrl);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);
        formData.add("client_id", clientId);
        formData.add("code_verifier", codeVerifier);

        Response response = target.request()
                .post(Entity.form(formData));

        if(response.getStatus() == 200) {
            JsonObject json = response.readEntity(JsonObject.class);
            return json.getString("access_token");
        } else {
            throw new ProcessingException("Error getting token: " + 
                response.getStatusInfo().getReasonPhrase());
        }
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? 
                URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? 
                URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }
}
