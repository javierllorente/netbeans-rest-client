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
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
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

        // Ensure parsing has run
        result.parse();

        for (HTTPParser.RequestLineContext reqLine : result.getRequestLineContexts()) {
            // Add null check for the request line context itself
            if (reqLine == null) {
                continue;
            }
            highlightUrl(result, reqLine);
            highlightHttpVersion(result, reqLine);
        }
    }

    // Include the modified helper methods with null checks as well
    private void highlightUrl(HTTPLangParserResult result, HTTPParser.RequestLineContext requestLine) {
        if (requestLine == null) {
            return; // Should not happen if checked in run, but safe practice
        }
        HTTPParser.RequestTargetContext url = requestLine.requestTarget();

        // Add null check for url context and its start/stop tokens
        if (url == null || url.start == null || url.stop == null) {
            return; // Cannot determine range if context or tokens are null
        }

        // Null checks for requestLine start/stop
        int lineStart = requestLine.start != null ? requestLine.start.getStartIndex() : -1;
        int lineEnd = requestLine.stop != null ? requestLine.stop.getStopIndex() + 1 : -1; // Use +1 for end offset consistency

        if (lineStart == -1 || lineEnd == -1) {
            // If line context is broken, don't proceed with highlighting for this line
            return;
        }

        // Check for diagnostics within this line's *parsed* range
        boolean hasErrorInThisLine = false;
        for (org.netbeans.modules.csl.api.Error diagnostic : result.getDiagnostics()) {
            int errorOffset = diagnostic.getStartPosition();
            // Check if error falls within the valid range of the request line context
            if (errorOffset >= lineStart && errorOffset < lineEnd) {
                hasErrorInThisLine = true;
                break;
            }
        }

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
            OffsetRange protocolRange = new OffsetRange(docProtocolStart, docProtocolStop);
            highlights.put(protocolRange, ColoringAttributes.CUSTOM1_SET); // CUSTOM1 used for URL
        }
    }

    private void highlightHttpVersion(HTTPLangParserResult result, HTTPParser.RequestLineContext requestLine) {
        if (requestLine == null) {
            return;
        }

        HTTPParser.HttpVersionContext httpVersion = requestLine.httpVersion();

        // Add null check for httpVersion context and its start/stop tokens
        if (httpVersion == null || httpVersion.start == null || httpVersion.stop == null) {
            return; // Cannot highlight if version context is incomplete/null
        }

        // Null checks for requestLine start/stop (redundant if checked before calling, but safe)
        int lineStart = requestLine.start != null ? requestLine.start.getStartIndex() : -1;
        int lineEnd = requestLine.stop != null ? requestLine.stop.getStopIndex() + 1 : -1;

        if (lineStart == -1 || lineEnd == -1) {
            return;
        }

        // Check for diagnostics (similar to highlightUrl)
        boolean hasErrorInThisLine = false;
        for (org.netbeans.modules.csl.api.Error diagnostic : result.getDiagnostics()) {
            int errorOffset = diagnostic.getStartPosition();
            if (errorOffset >= lineStart && errorOffset < lineEnd) {
                hasErrorInThisLine = true;
                break;
            }
        }

        if (hasErrorInThisLine) {
            return; // Skip highlighting if errors present
        }

        // Highlight HTTP_PROTOCOL part (e.g., "HTTP")
        TerminalNode protocolPart = httpVersion.HTTP_PROTOCOL();
        if (protocolPart != null && protocolPart.getSymbol() != null) {
            int protocolStart = protocolPart.getSymbol().getStartIndex();
            // Use symbol's stop index + 1 for end offset
            int protocolStop = protocolPart.getSymbol().getStopIndex() + 1;
            int docProtocolStart = result.getSnapshot().getOriginalOffset(protocolStart);
            int docProtocolStop = result.getSnapshot().getOriginalOffset(protocolStop);

            if (docProtocolStart != -1 && docProtocolStop != -1 && docProtocolStart < docProtocolStop) {
                OffsetRange protocolRange = new OffsetRange(docProtocolStart, docProtocolStop);
                // Use METHOD_SET for "HTTP" part as an example, adjust if needed
                highlights.put(protocolRange, ColoringAttributes.METHOD_SET);
            }
        }

        // Optionally highlight version number part differently
//        HTTPParser.VersionNumberContext versionNumCtx = httpVersion.versionNumber();
//        if (versionNumCtx != null && versionNumCtx.start != null && versionNumCtx.stop != null) {
//            int versionStart = versionNumCtx.start.getStartIndex();
//            int versionStop = versionNumCtx.stop.getStopIndex() + 1;
//            int docVersionStart = result.getSnapshot().getOriginalOffset(versionStart);
//            int docVersionStop = result.getSnapshot().getOriginalOffset(versionStop);
//
//            if (docVersionStart != -1 && docVersionStop != -1 && docVersionStart < docVersionStop) {
//                OffsetRange versionRange = new OffsetRange(docVersionStart, docVersionStop);
//                // Use e.g., NUMBER_SET or another custom attribute for version number
//                highlights.put(versionRange, ColoringAttributes.NUMBER_SET);
//            }
//        }
    }

//    /**
//     * Performs semantic analysis on the parsed HTTP language result. Clears
//     * previous highlights and processes the request line for HTTP-specific
//     * syntax coloring.
//     *
//     * @param result The parsed result of the HTTP language
//     * @param event The scheduling event triggering this analysis
//     */
//    @Override
//    public void run(HTTPLangParserResult result, SchedulerEvent event) {
//        if (cancelled.get()) {
//            return;
//        }
//
//        highlights.clear();
//
//        for (HTTPParser.RequestLineContext reqLine : result.getRequestLineContexts()) {
//            highlightUrl(result, reqLine);
//            highlightHttpVersion(result, reqLine);
//        }
    ////    }
//    private void highlightUrl(HTTPLangParserResult result, HTTPParser.RequestLineContext requestLine) {
//        HTTPParser.RequestTargetContext url = requestLine.requestTarget();
//
//        if (url == null) {
//            return;
//        }
//
//        int lineStart = requestLine.getStart().getStartIndex();
//        int lineEnd = requestLine.getStop().getStopIndex();
//
//        boolean hasErrorInThisLine = false;
//        for (org.netbeans.modules.csl.api.Error diagnostic : result.getDiagnostics()) {
//            int errorOffset = diagnostic.getStartPosition();
//            if (errorOffset >= lineStart && errorOffset <= lineEnd) {
//                hasErrorInThisLine = true;
//                break;
//            }
//        }
//
//        if (hasErrorInThisLine) {
//            return;
//        }
//
//        int protocolStart = url.start.getStartIndex();
//        int protocolStop = url.stop.getStopIndex() + 1;
//        int docProtocolStart = result.getSnapshot().getOriginalOffset(protocolStart);
//        int docProtocolStop = result.getSnapshot().getOriginalOffset(protocolStop);
//
//        if (docProtocolStart != -1 && docProtocolStop != -1 && docProtocolStart < docProtocolStop) {
//            OffsetRange protocolRange = new OffsetRange(docProtocolStart, docProtocolStop);
//            highlights.put(protocolRange, ColoringAttributes.CUSTOM1_SET);
//        }
//
//        // TODO: Maybe highlight version number part
//    }
//
//    private void highlightHttpVersion(HTTPLangParserResult result, HTTPParser.RequestLineContext requestLine) {
//        HTTPParser.HttpVersionContext httpVersion = requestLine.httpVersion();
//
//        if (httpVersion == null) {
//            return;
//        }
//
//        int lineStart = requestLine.getStart().getStartIndex();
//        int lineEnd = requestLine.getStop().getStopIndex();
//
//        boolean hasErrorInThisLine = false;
//        for (org.netbeans.modules.csl.api.Error diagnostic : result.getDiagnostics()) {
//            int errorOffset = diagnostic.getStartPosition();
//            if (errorOffset >= lineStart && errorOffset <= lineEnd) {
//                hasErrorInThisLine = true;
//                break;
//            }
//        }
//
//        if (hasErrorInThisLine) {
//            return;
//        }
//
//        TerminalNode protocolPart = httpVersion.HTTP_PROTOCOL();
//        if (protocolPart != null) {
//            int protocolStart = protocolPart.getSymbol().getStartIndex();
//            int protocolStop = protocolPart.getSymbol().getStopIndex() + 1;
//            int docProtocolStart = result.getSnapshot().getOriginalOffset(protocolStart);
//            int docProtocolStop = result.getSnapshot().getOriginalOffset(protocolStop);
//
//            if (docProtocolStart != -1 && docProtocolStop != -1 && docProtocolStart < docProtocolStop) {
//                OffsetRange protocolRange = new OffsetRange(docProtocolStart, docProtocolStop);
//                highlights.put(protocolRange, ColoringAttributes.METHOD_SET);
//            }
//        }
//
//        // TODO: Maybe highlight version number part
//    }

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
