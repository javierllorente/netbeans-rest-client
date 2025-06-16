package com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring;

import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author clenz
 */
public enum HTTPTokenId implements TokenId {

    METHOD("method"),
    URL("url"),
    DIGITS("number"),
    REQUEST_SEPARATOR("request_separator"),
    NONE("none"),
    COMMENT("comment"),
    WS("whitespace"),
    HTTP_PROTOCOL("http_protocol"),
    HTTP_VERSION("http_version"),
    HEADER_KEY("header_key"),
    HEADER_VALUE("header_value");

    private final String primaryCategory;

    HTTPTokenId(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    public static final String MIME_TYPE = "text/x-http"; // NOI18N
}
