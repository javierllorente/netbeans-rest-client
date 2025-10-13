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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(HTTPLangParserResult.class.getName());

    private final List<DefaultError> errors = new ArrayList<>();
    private boolean finished = false;
    private CommonTokenStream tokenStream;

    // Store all requestLine contexts found in the file
    private final List<HTTPParser.RequestLineContext> requestLineContexts = new ArrayList<>();
    // Store all requests contexts found in the file
    private final List<HTTPParser.RequestContext> requestContexts = new ArrayList<>();

    public HTTPLangParserResult(Snapshot snapshot) {
        super(snapshot);
    }

    public CommonTokenStream getTokenStream() {
        return tokenStream;
    }

    public HTTPLangParserResult parse() {
        if (!finished) {
            try {
                String text = getSnapshot().getText().toString();

                HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(text));
                this.tokenStream = new CommonTokenStream(lexer);
                HTTPParser parser = new HTTPParser(this.tokenStream);

                parser.removeErrorListeners();
                parser.addErrorListener(createErrorListener());
                parser.addParseListener(createFoldListener());

                HTTPParser.HttpRequestsFileContext fileCtx = parser.httpRequestsFile();

                if (fileCtx != null) {
                    LOGGER.log(Level.FINE, "Parsed requests file with {0} request blocks",
                        fileCtx.requestBlock().size());

                    // Iterate over all request blocks in the file
                    try {
                        for (HTTPParser.RequestBlockContext blockCtx : fileCtx.requestBlock()) {
                            if (blockCtx == null) {
                                continue;
                            }

                            HTTPParser.RequestContext req = blockCtx.request();
                            if (req != null) {
                                requestContexts.add(req);

                                if (req.requestLine() != null) {
                                    requestLineContexts.add(req.requestLine());
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error processing request blocks", e);
                    }

                    // Validate request bodies (blank line requirement)
                    try {
                        validateRequestBodies(fileCtx);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error validating request bodies", e);
                    }
                }
            } catch (RecognitionException e) {
                LOGGER.log(Level.SEVERE, "Fatal error during parsing", e);
                errors.add(new DefaultError(
                    "http-error",
                    "Fatal parsing error: " + e.getMessage(),
                    null,
                    getFileObject(),
                    0,
                    0,
                    Severity.ERROR));
            } finally {
                finished = true;
            }
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

    /**
     * Returns the list of all requests contexts found.
     */
    public List<HTTPParser.RequestContext> getRequestContexts() {
        return requestContexts;
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

    /**
     * Validates request bodies to ensure they have a blank line before them.
     * Adds errors for bodies that don't follow the blank line requirement.
     */
    private void validateRequestBodies(HTTPParser.HttpRequestsFileContext fileCtx) {
        if (fileCtx == null || tokenStream == null) {
            return;
        }

        for (HTTPParser.RequestBlockContext blockCtx : fileCtx.requestBlock()) {
            if (blockCtx == null) {
                continue;
            }

            HTTPParser.RequestContext req = blockCtx.request();
            if (req == null) {
                continue;
            }

            // Check requestLineWithBody (body directly after request line without blank)
            HTTPParser.RequestLineWithBodyContext reqLineWithBody = req.requestLineWithBody();
            if (reqLineWithBody != null && reqLineWithBody.start != null) {
                // This is always an error - body directly after request line
                int errorPosition = reqLineWithBody.start.getStartIndex();
                errors.add(new DefaultError(
                    "http-error",
                    "Invalid content found: Unknown HTTP header",
                    null,
                    getFileObject(),
                    errorPosition,
                    errorPosition,
                    Severity.ERROR));
                continue;
            }

            // Check regular requestBody
            HTTPParser.RequestBodySectionContext bodySection = req.requestBodySection();
            if (bodySection == null) {
                continue;
            }

            HTTPParser.RequestBodyContext body = bodySection.requestBody();
            if (body == null || body.start == null) {
                continue;
            }

            int bodyTokenType = body.start.getType();

            // BODY_START_NO_BLANK is always an error
            if (bodyTokenType == HTTPLexer.BODY_START_NO_BLANK) {
                int errorPosition = body.start.getStartIndex();
                errors.add(new DefaultError(
                    "http-error",
                    "Invalid content found: Unknown HTTP header",
                    null,
                    getFileObject(),
                    errorPosition,
                    errorPosition,
                    Severity.ERROR));
            } // BODY_START_WITH_BLANK needs blank line validation
            else if (bodyTokenType == HTTPLexer.BODY_START_WITH_BLANK) {
                if (!hasBlankLineBefore(body.start)) {
                    int errorPosition = body.start.getStartIndex();
                    errors.add(new DefaultError(
                        "http-error",
                        "Invalid content found: Unknown HTTP header",
                        null,
                        getFileObject(),
                        errorPosition,
                        errorPosition,
                        Severity.ERROR));
                }
            }
        }
    }

    /**
     * Checks if there's a blank line before BODY_START_WITH_BLANK token. A
     * blank line means having a NEWLINE token before the BODY_START_WITH_BLANK
     * (which already includes a NEWLINE itself).
     */
    private boolean hasBlankLineBefore(Token bodyToken) {
        if (bodyToken == null || bodyToken.getType() != HTTPLexer.BODY_START_WITH_BLANK) {
            return true;
        }

        if (tokenStream == null) {
            return false;
        }

        int idx = bodyToken.getTokenIndex() - 1;

        // Skip only WS tokens (not COMMENT)
        while (idx >= 0 && tokenStream.get(idx).getType() == HTTPLexer.WS) {
            idx--;
        }

        if (idx < 0) {
            return false;
        }

        Token prev = tokenStream.get(idx);

        // If previous token (after skipping WS) is NEWLINE, we have a blank line
        // This works because BODY_START_WITH_BLANK already includes a NEWLINE,
        // so having another NEWLINE before it means two consecutive newlines = blank line
        if (prev.getType() == HTTPLexer.NEWLINE) {
            return true;
        }

        // If previous token is COMMENT, we need TWO newlines before body:
        // one after request line, then blank, then comment (Test 12 case)
        // Skip back past COMMENT to check
        if (prev.getType() == HTTPLexer.COMMENT) {
            idx--;

            // Skip WS
            while (idx >= 0 && tokenStream.get(idx).getType() == HTTPLexer.WS) {
                idx--;
            }

            // Check if there are TWO NEWLINEs before the COMMENT
            // (We need at least 2 because: NEWLINE ends request, NEWLINE creates blank line)
            if (idx >= 0 && tokenStream.get(idx).getType() == HTTPLexer.NEWLINE) {
                idx--;

                while (idx >= 0 && tokenStream.get(idx).getType() == HTTPLexer.WS) {
                    idx--;
                }

                if (idx >= 0 && tokenStream.get(idx).getType() == HTTPLexer.NEWLINE) {
                    return true;
                }
            }
        }

        return false;
    }

    protected final FileObject getFileObject() {
        return getSnapshot().getSource().getFileObject();
    }
}
