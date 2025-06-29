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
package com.javierllorente.netbeans.rest.client.http.editor.syntax;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParserBaseListener;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.netbeans.modules.csl.api.Severity;
import org.netbeans.modules.csl.spi.DefaultError;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Christian Lenz <christian.lenz@gmx.net>
 */
public class HTTPLangParserResult extends ParserResult {

    private final List<DefaultError> errors = new ArrayList<>();
    private boolean finished = false;

    // Store all requestLine contexts found in the file
    private final List<HTTPParser.RequestLineContext> requestLineContexts = new ArrayList<>();

    public HTTPLangParserResult(Snapshot snapshot) {
        super(snapshot);
    }

    public HTTPLangParserResult parse() {
        if (!finished) {
            String text = getSnapshot().getText().toString();

            HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(text));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            HTTPParser parser = new HTTPParser(tokenStream);

            parser.removeErrorListeners();
            parser.addErrorListener(createErrorListener());
            parser.addParseListener(createFoldListener());

            HTTPParser.HttpRequestsFileContext fileCtx = parser.httpRequestsFile();

            if (fileCtx != null) {
                System.out.println("Reqeustsfile: " + fileCtx.getText());
                System.out.println("fileCtx: " + fileCtx.requestBlockWithSeparator().size());

                // Iterate over all request nodes in the file
                for (HTTPParser.RequestBlockWithSeparatorContext requestBlockWithSeparatorContext : fileCtx.requestBlockWithSeparator()) {
                    System.out.println("RequestWithSeparator: " + requestBlockWithSeparatorContext.getText());
                    HTTPParser.RequestContext req = requestBlockWithSeparatorContext.request();

                    if (req.requestLine() != null) {
                        requestLineContexts.add(req.requestLine());
                    }
                }

                HTTPParser.FirstRequestContext firstRequest = fileCtx.firstRequest();

                if (firstRequest != null) {
                    HTTPParser.RequestContext request = firstRequest.request();
                    if (request != null) {
                        requestLineContexts.add(0, request.requestLine());
                    }
                }

            }

            finished = true;
        }
        return this;
    }

    @Override
    public List<? extends org.netbeans.modules.csl.api.Error> getDiagnostics() {
        return errors;
    }

    /**
     * Returns the list of all requestLine contexts found.
     */
    public List<HTTPParser.RequestLineContext> getRequestLineContexts() {
        return requestLineContexts;
    }

    @Override
    protected void invalidate() {
        // Optionally clear internal data
    }

    @Override
    protected boolean processingFinished() {
        return finished;
    }

    private ParseTreeListener createFoldListener() {
        return new HTTPParserBaseListener() {
            // Implement code folding if needed
        };
    }

    private ANTLRErrorListener createErrorListener() {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e) {
                int errorPosition = 0;
                if (offendingSymbol instanceof Token) {
                    Token token = (Token) offendingSymbol;
                    errorPosition = token.getStartIndex();
                }
                errors.add(new DefaultError(
                    "http-error",
                    "Invalid content found: " + msg,
                    null,
                    getFileObject(),
                    errorPosition,
                    errorPosition,
                    Severity.ERROR));
            }
        };
    }

    protected final FileObject getFileObject() {
        return getSnapshot().getSource().getFileObject();
    }
}
