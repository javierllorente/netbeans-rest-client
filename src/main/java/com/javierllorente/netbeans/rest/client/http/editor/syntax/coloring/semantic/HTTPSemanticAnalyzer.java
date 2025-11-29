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
package com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring.semantic;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.HTTPLangParserResult;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.netbeans.modules.csl.api.ColoringAttributes;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.SemanticAnalyzer;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;

/**
 *
 * @author Christian Lenz <christian.lenz@gmx.net>
 */
/**
 * Semantic analyzer for HTTP language syntax highlighting. Focuses on providing
 * specialized coloring for HTTP protocol versions within request lines of HTTP
 * request files.
 */
public class HTTPSemanticAnalyzer extends SemanticAnalyzer<HTTPLangParserResult> {

    private static final Logger LOGGER = Logger.getLogger(HTTPSemanticAnalyzer.class.getName());

    /**
     * Flag to track if the current analysis has been cancelled
     */
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * Map to store text ranges and their corresponding syntax highlighting
     * attributes. Key represents the text offset range, value represents syntax
     * coloring attributes.
     */
    private final Map<OffsetRange, Set<ColoringAttributes>> highlights = new HashMap<>();

    /**
     * Retrieves the current set of syntax highlights.
     *
     * @return Map of text offset ranges and their syntax highlighting
     * attributes
     */
    @Override
    public Map<OffsetRange, Set<ColoringAttributes>> getHighlights() {
        return highlights;
    }

    /**
     * Performs semantic analysis on the parsed HTTP language result. Clears
     * previous highlights and processes the request line for HTTP-specific
     * syntax coloring.
     *
     * @param result The parsed result of the HTTP language
     * @param event The scheduling event triggering this analysis
     */
    @Override
    public void run(HTTPLangParserResult result, SchedulerEvent event) {
        if (cancelled.get()) {
            return;
        }

        highlights.clear();

        if (result == null) {
            return; // Nothing to analyze
        }

        try {
            // Ensure parsing has run
            result.parse();

            // Highlight URLs and HTTP versions in request lines
            try {
                for (HTTPParser.RequestLineContext reqLine : result.getRequestLineContexts()) {
                    // Add null check for the request line context itself
                    if (reqLine == null) {
                        continue;
                    }

                    highlightUrl(result, reqLine);
                    highlightHttpVersion(result, reqLine);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error highlighting request lines", e);
            }

            // Highlight headers
            try {
                for (HTTPParser.RequestContext reqContext : result.getRequestContexts()) {
                    if (reqContext == null) {
                        continue;
                    }

                    highlightHeaders(reqContext.requestHeaders());
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error highlighting headers", e);
            }

            // Highlight JSON bodies
            try {
                for (HTTPParser.RequestContext reqContext : result.getRequestContexts()) {
                    if (reqContext == null) {
                        continue;
                    }

                    highlightJson(result, reqContext.requestBodySection());
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error highlighting JSON bodies", e);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fatal error in semantic analysis", e);
        }
    }

    // Include the modified helper methods with null checks as well
    private void highlightUrl(HTTPLangParserResult result, HTTPParser.RequestLineContext requestLine) {
        if (requestLine == null) {
            return; // Should not happen if checked in run, but safe practice
        }

        try {
            HTTPParser.RequestTargetContext url = requestLine.requestTarget();

            // Add null check for url context and its start/stop tokens
            if (url == null || url.start == null || url.stop == null) {
                return; // Cannot determine range if context or tokens are null
            }

            // Null checks for requestLine start/stop
            int lineStart = requestLine.start != null ? requestLine.start.getStartIndex() : -1;
            int lineEnd = requestLine.stop != null ? requestLine.stop.getStopIndex() + 1 : -1; // Use +1 for end offset consistency

            if (lineStart == -1 || lineEnd == -1 || lineStart >= lineEnd) {
                // If line context is broken, don't proceed with highlighting for this line
                return;
            }

            // Check for diagnostics within this line's *parsed* range
            boolean hasErrorInThisLine = checkForErrors(result, lineStart, lineEnd);

            if (hasErrorInThisLine) {
                // Optionally log or decide if partial highlighting is desired despite errors
                return; // Skip highlighting if errors present in the line context
            }

            // Proceed with highlighting only if context and tokens are valid
            int protocolStart = url.start.getStartIndex();
            int protocolStop = url.stop.getStopIndex() + 1; // Use +1 for end offset
            int docProtocolStart = result.getSnapshot().getOriginalOffset(protocolStart);
            int docProtocolStop = result.getSnapshot().getOriginalOffset(protocolStop);

            if (docProtocolStart != -1 && docProtocolStop != -1 && docProtocolStart < docProtocolStop) {
                try {
                    OffsetRange protocolRange = new OffsetRange(docProtocolStart, docProtocolStop);
                    highlights.put(protocolRange, ColoringAttributes.CUSTOM1_SET); // CUSTOM1 used for URL
                } catch (AssertionError e) {
                    LOGGER.log(Level.WARNING, "Invalid offset range for URL: start={0}, end={1}",
                        new Object[]{docProtocolStart, docProtocolStop});
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error highlighting URL", e);
        }
    }

    private void highlightHttpVersion(HTTPLangParserResult result, HTTPParser.RequestLineContext requestLine) {
        if (requestLine == null) {
            return;
        }

        try {
            HTTPParser.HttpVersionContext httpVersion = requestLine.httpVersion();

            // Add null check for httpVersion context and its start/stop tokens
            if (httpVersion == null || httpVersion.start == null || httpVersion.stop == null) {
                return; // Cannot highlight if version context is incomplete/null
            }

            // Null checks for requestLine start/stop (redundant if checked before calling, but safe)
            int lineStart = requestLine.start != null ? requestLine.start.getStartIndex() : -1;
            int lineEnd = requestLine.stop != null ? requestLine.stop.getStopIndex() + 1 : -1;

            if (lineStart == -1 || lineEnd == -1 || lineStart >= lineEnd) {
                return;
            }

            boolean hasErrorInThisLine = checkForErrors(result, lineStart, lineEnd);

            if (hasErrorInThisLine) {
                return; // Skip highlighting if errors present
            }

            // Highlight HTTP_PROTOCOL part (e.g., "HTTP")
            TerminalNode protocolPart = httpVersion.HTTP_PROTOCOL();
            if (protocolPart != null && protocolPart.getSymbol() != null) {
                int protocolStart = protocolPart.getSymbol().getStartIndex();
                // Use symbol's stop index + 1 for end offset
                int protocolStop = protocolPart.getSymbol().getStopIndex() + 1;

                // Validate offsets are valid and in correct order
                if (protocolStart < 0 || protocolStop < 0 || protocolStart >= protocolStop) {
                    return;
                }

                int docProtocolStart = result.getSnapshot().getOriginalOffset(protocolStart);
                int docProtocolStop = result.getSnapshot().getOriginalOffset(protocolStop);

                if (docProtocolStart != -1 && docProtocolStop != -1 && docProtocolStart < docProtocolStop) {
                    try {
                        OffsetRange protocolRange = new OffsetRange(docProtocolStart, docProtocolStop);
                        // Use METHOD_SET for "HTTP" part as an example, adjust if needed
                        highlights.put(protocolRange, ColoringAttributes.METHOD_SET);
                    } catch (AssertionError e) {
                        LOGGER.log(Level.WARNING, "Invalid offset range for HTTP version: start={0}, end={1}",
                            new Object[]{docProtocolStart, docProtocolStop});
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error highlighting HTTP version", e);
        }
    }

    private boolean checkForErrors(HTTPLangParserResult result, int lineStart, int lineEnd) {
        boolean hasErrorInThisLine = false;

        for (org.netbeans.modules.csl.api.Error diagnostic : result.getDiagnostics()) {
            int errorOffset = diagnostic.getStartPosition();

            if (errorOffset >= lineStart && errorOffset < lineEnd) {
                hasErrorInThisLine = true;
                break;
            }
        }
        return hasErrorInThisLine;
    }

    private void highlightHeaders(HTTPParser.RequestHeadersContext requestHeadersContext) {
        if (requestHeadersContext == null) {
            return;
        }

        try {
            // requestHeaders can contain both headerLine (which has header or invalidHeaderLine)
            // We only highlight valid headers
            for (HTTPParser.HeaderLineContext headerLineContext : requestHeadersContext.headerLine()) {
                if (headerLineContext == null) {
                    continue;
                }

                // headerLine can be either header or invalidHeaderLine
                HTTPParser.HeaderContext headerContext = headerLineContext.header();
                if (headerContext == null) {
                    // This is an invalidHeaderLine, skip highlighting
                    continue;
                }

                HTTPParser.HeaderFieldContext headerFieldContext = headerContext.headerField();

                if (headerFieldContext == null) {
                    continue;
                }

                HTTPParser.HeaderFieldNameContext headerFieldNameContext = headerFieldContext.headerFieldName();

                if (headerFieldNameContext == null || headerFieldNameContext.start == null || headerFieldNameContext.stop == null) {
                    continue;
                }

                int headerFieldNameStartIndex = headerFieldNameContext.start.getStartIndex();
                int headerFieldNameEndIndex = headerFieldNameContext.stop.getStopIndex() + 1;

                // Validate offsets are valid and in correct order
                if (headerFieldNameStartIndex >= 0 && headerFieldNameEndIndex >= 0 && headerFieldNameStartIndex < headerFieldNameEndIndex) {
                    try {
                        highlights.put(new OffsetRange(headerFieldNameStartIndex, headerFieldNameEndIndex), ColoringAttributes.FIELD_SET);
                    } catch (AssertionError e) {
                        LOGGER.log(Level.WARNING, "Invalid offset range for header name: start={0}, end={1}",
                            new Object[]{headerFieldNameStartIndex, headerFieldNameEndIndex});
                    }
                }

                HTTPParser.HeaderFieldValueContext headerFieldValueContext = headerFieldContext.headerFieldValue();

                if (headerFieldValueContext == null || headerFieldValueContext.start == null || headerFieldValueContext.stop == null) {
                    continue;
                }

                int headerFieldValueStartIndex = headerFieldValueContext.start.getStartIndex();
                int headerFieldValueEndIndex = headerFieldValueContext.stop.getStopIndex() + 1;

                // Validate offsets are valid and in correct order
                if (headerFieldValueStartIndex >= 0 && headerFieldValueEndIndex >= 0 && headerFieldValueStartIndex < headerFieldValueEndIndex) {
                    try {
                        highlights.put(new OffsetRange(headerFieldValueStartIndex, headerFieldValueEndIndex), ColoringAttributes.CUSTOM1_SET);
                    } catch (AssertionError e) {
                        LOGGER.log(Level.WARNING, "Invalid offset range for header value: start={0}, end={1}",
                            new Object[]{headerFieldValueStartIndex, headerFieldValueEndIndex});
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error highlighting headers", e);
        }
    }

    private void highlightJson(HTTPLangParserResult result, HTTPParser.RequestBodySectionContext requestBodySectionContext) {
        if (requestBodySectionContext == null) {
            return;
        }

        try {
            HTTPParser.RequestBodyContext requestBody = requestBodySectionContext.requestBody();
            if (requestBody == null) {
                return;
            }

            HTTPParser.BodyContentContext bodyContent = null;
            if (requestBody.bodyWithStarter() != null) {
                bodyContent = requestBody.bodyWithStarter().bodyContent();
            }

            if (bodyContent == null || bodyContent.start == null || bodyContent.stop == null) {
                return;
            }

            // Check for errors in this body
            if (requestBodySectionContext.start != null && requestBodySectionContext.stop != null) {
                int bodyStart = requestBodySectionContext.start.getStartIndex();
                int bodyEnd = requestBodySectionContext.stop.getStopIndex() + 1;

                for (org.netbeans.modules.csl.api.Error diagnostic : result.getDiagnostics()) {
                    int errorOffset = diagnostic.getStartPosition();
                    if (errorOffset >= bodyStart && errorOffset < bodyEnd) {
                        return; // Skip highlighting if errors in body
                    }
                }
            }

            // Use token-based highlighting with intelligent context tracking
            // This is necessary because catch-all grammar rules prevent proper parse-tree structure
            highlightJsonTokenBased(result, bodyContent);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error highlighting JSON content", e);
        }
    }

    /**
     * Token-based JSON highlighting with intelligent context tracking. This is
     * required because the grammar's catch-all rules prevent proper parse-tree
     * structure from being created.
     *
     * Intelligently distinguishes keys from values: - Tracks nesting depth
     * (objects vs arrays) - Keys appear after '{' or ',' in object context -
     * Values appear after ':' in object context
     */
    private void highlightJsonTokenBased(HTTPLangParserResult result, HTTPParser.BodyContentContext bodyContent) {
        if (bodyContent == null || bodyContent.start == null || bodyContent.stop == null) {
            return;
        }

        try {
            int contentStart = bodyContent.start.getTokenIndex();
            int contentEnd = bodyContent.stop.getTokenIndex();

            TokenStream tokens = result.getTokenStream();
            if (tokens == null) {
                return;
            }

            // Track nesting context
            int arrayDepth = 0;
            int previousNonWhitespaceTokenType = -1;

            // Initialize previousNonWhitespaceTokenType by checking token before contentStart
            int startIdx = contentStart;
            if (startIdx > 0) {
                int idx = startIdx - 1;

                while (idx >= 0) {
                    Token prevToken = tokens.get(idx);
                    int prevType = prevToken.getType();
                    if (prevType != HTTPLexer.WS
                        && prevType != HTTPLexer.NEWLINE) {
                        previousNonWhitespaceTokenType = prevType;
                        break;
                    }
                    idx--;
                }
            }

            for (int i = contentStart; i <= contentEnd; i++) {
                Token token = tokens.get(i);
                int tokenType = token.getType();

                // Track array nesting
                if (tokenType == HTTPLexer.OPEN_BRAKET) {
                    arrayDepth++;
                } else if (tokenType == HTTPLexer.CLOSE_BRAKET) {
                    arrayDepth--;
                } else if (tokenType == HTTPLexer.STRING) {
                    // Determine if this STRING is a key or value based on context
                    boolean isKey = false;

                    if (arrayDepth > 0) {
                        // Inside an array - all strings are values
                        isKey = false;
                    } else {
                        // In object context - check previous token
                        // Keys appear after: { or , or BODY_START tokens
                        // Values appear after: :
                        if (previousNonWhitespaceTokenType == HTTPLexer.OPEN_BLOCK_BRAKET
                            || previousNonWhitespaceTokenType == HTTPLexer.COMMA
                            || previousNonWhitespaceTokenType == HTTPLexer.BODY_START_WITH_BLANK
                            || previousNonWhitespaceTokenType == HTTPLexer.BODY_START_NO_BLANK
                            || previousNonWhitespaceTokenType == -1) {
                            isKey = true;
                        } else if (previousNonWhitespaceTokenType == HTTPLexer.COLON) {
                            isKey = false;
                        }
                    }

                    // Highlight the string
                    int tokenStart = token.getStartIndex();
                    int tokenEnd = token.getStopIndex() + 1;

                    int docStart = result.getSnapshot().getOriginalOffset(tokenStart);
                    int docEnd = result.getSnapshot().getOriginalOffset(tokenEnd);

                    if (docStart != -1 && docEnd != -1 && docStart < docEnd) {
                        try {
                            if (isKey) {
                                highlights.put(new OffsetRange(docStart, docEnd), ColoringAttributes.FIELD_SET);
                            } else {
                                highlights.put(new OffsetRange(docStart, docEnd), ColoringAttributes.CUSTOM1_SET);
                            }
                        } catch (AssertionError e) {
                            LOGGER.log(Level.WARNING, "Invalid offset range for JSON: start={0}, end={1}",
                                new Object[]{docStart, docEnd});
                        }
                    }
                }

                // Track previous non-whitespace token for context
                if (tokenType != HTTPLexer.WS
                    && tokenType != HTTPLexer.NEWLINE) {
                    previousNonWhitespaceTokenType = tokenType;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error in token-based JSON highlighting", e);
        }
    }

    /**
     * Determines the priority of this semantic analyzer. Lower values indicate
     * higher priority.
     *
     * @return Priority value (0 indicates highest priority)
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * Specifies the scheduler class for managing editor-sensitive tasks.
     *
     * @return Scheduler class for editor-sensitive operations
     */
    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    /**
     * Cancels the current semantic analysis process. Sets the cancelled flag to
     * interrupt ongoing analysis.
     */
    @Override
    public void cancel() {
        cancelled.set(true);
    }
}
