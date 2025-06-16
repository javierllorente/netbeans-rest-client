/*
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
package com.javierllorente.netbeans.rest.client.http.editor.syntax;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring.HTTPTokenId;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.antlr4.AbstractAntlrLexerBridge;

/**
 *
 * @author christiann lenz
 */
public final class HTTPLangLexer extends AbstractAntlrLexerBridge<HTTPLexer, HTTPTokenId> {

    public HTTPLangLexer(LexerRestartInfo<HTTPTokenId> info) {
        super(info, HTTPLexer::new);
    }

    @Override
    protected Token<HTTPTokenId> mapToken(org.antlr.v4.runtime.Token antlrToken) {
        switch (antlrToken.getType()) {
            case HTTPLexer.METHOD:
                return token(HTTPTokenId.METHOD);
            case HTTPLexer.COMMENT:
                return token(HTTPTokenId.COMMENT);
            case HTTPLexer.REQUEST_SEPARATOR:
                return token(HTTPTokenId.REQUEST_SEPARATOR);
            case HTTPLexer.DIGITS:
                return token(HTTPTokenId.DIGITS);
            case HTTPLexer.WS:
                return token(HTTPTokenId.WS);
            case HTTPLexer.HTTP_PROTOCOL:
                return token(HTTPTokenId.HTTP_PROTOCOL);

            default:
                return token(HTTPTokenId.NONE);
        }
    }
}
