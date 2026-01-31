/*
 * Copyright 2025 Christian Lenz <christian.lenz@gmx.net>.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.javierllorente.netbeans.rest.client.http.editor.sidebar.request;

import com.javierllorente.netbeans.rest.client.RestClient;
import com.javierllorente.netbeans.rest.client.RestClientInitializer;
import com.javierllorente.netbeans.rest.client.http.editor.sidebar.ResponseSidebarManager;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import com.javierllorente.netbeans.rest.client.ui.RestClientTopComponent;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.text.NbDocument;

/**
 *
 * @author Christian Lenz
 */
public class RequestProcessor implements IRequestProcessor {

    public final List<Request> currentRequests;
    private final StyledDocument document;
    private final RestClient restClient;
    private final org.openide.util.RequestProcessor processor;
    private JTextComponent textComponent;
    private RestClientTopComponent restClientUi;

    public RequestProcessor(StyledDocument document) {
        this.currentRequests = new ArrayList<>();
        this.document = document;

        this.restClient = RestClientInitializer.createClient();
        this.processor = new org.openide.util.RequestProcessor(RequestProcessor.class);
    }

    /**
     * Set the text component for this processor. This is used to show responses
     * in the sidebar.
     */
    public void setTextComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    @Override
    public List<Request> getRequests() {
        return currentRequests;
    }

    @Override
    public void updateRequestsList(String text) {
        currentRequests.clear();

        HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HTTPParser parser = new HTTPParser(tokens);

        HTTPParser.HttpRequestsFileContext fileCtx = parser.httpRequestsFile();

        if (fileCtx == null) {
            return;
        }

        List<HTTPParser.RequestBlockContext> requestBlock = fileCtx.requestBlock();

        for (HTTPParser.RequestBlockContext requestBlockContext : requestBlock) {
            HTTPParser.RequestContext request = requestBlockContext.request();

            if (request == null) {
                return;
            }

            HTTPParser.RequestLineContext requestLine = request.requestLine();

            if (requestLine == null) {
                return;
            }

            currentRequests.add(new Request(request, requestLine.getText(), NbDocument.findLineNumber(document, requestLine.start.getStartIndex())));
        }
    }

    @Override
    public void callRequest(HTTPParser.RequestContext requestContext) {
        if (requestContext == null) {
            return;
        }

        HTTPParser.RequestLineContext requestLine = requestContext.requestLine();

        if (requestLine == null) {
            return;
        }

        HTTPParser.RequestTargetContext requestTarget = requestLine.requestTarget();

        if (requestTarget == null) {
            return;
        }

        // Extract values before async execution (parser context may not be thread-safe)
        final String url = requestTarget.getText();
        final String method = requestLine.METHOD() == null ? "GET" : requestLine.METHOD().getText();
        final MultivaluedMap<String, String> headers = getHeaders(requestContext.requestHeaders());
        final String body = getBody(requestContext.requestBodySection());
        final JTextComponent tc = this.textComponent;

        // Execute request asynchronously to avoid blocking the EDT
        processor.post(() -> {
            ProgressHandle progressHandle = ProgressHandle.createHandle("Sending request: " + method + " " + url);
            progressHandle.start();

            try {
                this.restClient.setHeaders(headers);
                this.restClient.setBody(body);

                String response = this.restClient.request(url, method);

                // Show response in sidebar if textComponent is available (update on EDT)
                if (tc != null && response != null) {
                    String contentType = "";
                    var responseHeaders = this.restClient.getResponseHeaders();

                    if (responseHeaders != null && responseHeaders.containsKey("content-type")
                        && !responseHeaders.get("content-type").isEmpty()) {
                        contentType = (String) responseHeaders.get("content-type").get(0);
                    }

                    // Capture for lambda
                    final String finalContentType = contentType;
                    final String finalResponse = response;
                    final var finalResponseHeaders = responseHeaders;

                    SwingUtilities.invokeLater(() -> {
                        ResponseSidebarManager.getInstance().showResponse(tc, finalResponse, finalContentType, finalResponseHeaders);
                    });
                }
            } catch (ProcessingException ex) {
                SwingUtilities.invokeLater(() -> {
                    ResponseSidebarManager.getInstance().showResponse(tc, ex.getMessage(), "", null);
                });
            } finally {
                progressHandle.finish();
            }
        });
    }

    @Override
    public void openRequestInUi(HTTPParser.RequestContext requestContext) {
        if (requestContext == null) {
            return;
        }

        HTTPParser.RequestLineContext requestLine = requestContext.requestLine();

        if (requestLine == null) {
            return;
        }

        HTTPParser.RequestTargetContext requestTarget = requestLine.requestTarget();

        if (requestTarget == null) {
            return;
        }

        String method = "GET"; // Default
        if (requestLine.METHOD() != null) {
            method = requestLine.METHOD().getText();
        }

        this.restClient.setMethod(method);
        this.restClient.setUri(requestTarget.getText());
        this.restClient.setHeaders(getHeaders(requestContext.requestHeaders()));

        String body = getBody(requestContext.requestBodySection());

        if (body != null) {
            restClient.setBodyType("Text");
            this.restClient.setBody(body);
        }

        restClientUi = new RestClientTopComponent(this.restClient);
        restClientUi.open();
        restClientUi.requestActive();
    }

    private MultivaluedMap<String, String> getHeaders(HTTPParser.RequestHeadersContext requestHeadersContext) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

        if (requestHeadersContext == null) {
            return headers;
        }

        List<HTTPParser.HeaderLineContext> headerLine = requestHeadersContext.headerLine();

        for (HTTPParser.HeaderLineContext headerLineContext : headerLine) {
            HTTPParser.HeaderContext header = headerLineContext.header();

            if (header == null) {
                break;
            }

            HTTPParser.HeaderFieldContext headerField = header.headerField();

            if (headerField == null) {
                break;
            }

            HTTPParser.HeaderFieldNameContext headerFieldNameContext = headerField.headerFieldName();
            HTTPParser.HeaderFieldValueContext headerFieldValueContext = headerField.headerFieldValue();

            if (headerFieldNameContext == null || headerFieldValueContext == null) {
                break;
            }

            String key = headerFieldNameContext.getText();
            String value = headerFieldValueContext.getText();

            if (!headers.containsKey(key) || !headers.get(key).contains(value)) {
                headers.add(key, value);
            }
        }

        return headers;
    }

    private String getBody(HTTPParser.RequestBodySectionContext requestBodySectionContext) {
        if (requestBodySectionContext == null) {
            return "";
        }

        HTTPParser.RequestBodyContext requestBody = requestBodySectionContext.requestBody();

        if (requestBody == null) {
            return "";
        }

        if (requestBody.bodyWithStarter() != null) {
            HTTPParser.BodyContentContext bodyContent = requestBody.bodyWithStarter().bodyContent();
            return bodyContent != null ? bodyContent.getText() : "";
        } else if (requestBody.directBodyContent() != null) {
            return requestBody.directBodyContent().getText();
        }

        return "";
    }
}
