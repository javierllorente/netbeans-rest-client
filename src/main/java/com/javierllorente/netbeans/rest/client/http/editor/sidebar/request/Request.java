package com.javierllorente.netbeans.rest.client.http.editor.sidebar.request;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;

/**
 *
 * @author Christian Lenz
 */
public class Request {
    private final HTTPParser.RequestContext requestContext;
    private final String requestLineText;
    private final int lineNumber;

    public Request(HTTPParser.RequestContext requestContext, String requestLineText, int lineNumber) {
        this.requestContext = requestContext;
        this.requestLineText = requestLineText;
        this.lineNumber = lineNumber;
    }

    public HTTPParser.RequestContext getrequestContext() {
        return requestContext;
    }
    
    public String getRequestLineText() {
        return requestLineText;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}

