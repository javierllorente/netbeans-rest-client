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
package com.javierllorente.netbeans.rest.client.http.editor.completion;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser.RequestLineContext;
import java.util.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.netbeans.spi.editor.completion.CompletionItem;

/**
 * Logic for HTTP code completion.
 */
public final class HTTPCompletionLogic {

    private static final int INVALID = -1;

    private HTTPCompletionLogic() {
    }

    public static CompletionResult compute(Document doc, int caretOffset) throws BadLocationException {
        String fullText = doc.getText(0, doc.getLength());
        Element rootElement = doc.getDefaultRootElement();

        if (fullText.trim().isEmpty() && caretOffset == 0) {
            return createMethodResult("", caretOffset, caretOffset);
        }

        int currentLineIndex = rootElement.getElementIndex(caretOffset);
        int lineStartOffset = rootElement.getElement(currentLineIndex).getStartOffset();
        int lineEndOffset = rootElement.getElement(currentLineIndex).getEndOffset();

        String lineTextToCaret = doc.getText(lineStartOffset, caretOffset - lineStartOffset);
        String lineTextAfterCaret = doc.getText(caretOffset, Math.min(lineEndOffset, doc.getLength()) - caretOffset);

        int wordStartOffset = findLastWordStart(lineTextToCaret);
        int anchor = lineStartOffset + wordStartOffset;
        String prefix = buildPrefix(lineTextToCaret, lineTextAfterCaret, wordStartOffset);

        HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(fullText));
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        // Token-based detection for incomplete lines
        if (CompletionTokenAnalyzer.isMethodFollowedBySpace(tokens, lineStartOffset, caretOffset)) {
            return createUrlResult("", caretOffset, caretOffset);
        }

        if (CompletionTokenAnalyzer.isMethodUrlFollowedBySpace(tokens, lineStartOffset, caretOffset)) {
            return createVersionResult(doc, "", caretOffset, caretOffset);
        }

        HTTPParser parser = new HTTPParser(tokens);
        parser.removeErrorListeners();

        HTTPParser.HttpRequestsFileContext fileCtx;

        try {
            fileCtx = parser.httpRequestsFile();
        } catch (RecognitionException e) {
            if (currentLineIndex == 0 && isOnlyAlphaOrWhitespace(tokens, lineStartOffset, caretOffset)) {
                return createMethodResult(prefix, anchor, caretOffset);
            }

            return null;
        }

        // Check if at start of new request block
        boolean isNewLineAfterSeparator = isStartOfRequestBlock(tokens, lineStartOffset);
        if (isNewLineAfterSeparator) {
            return handleNewRequestBlock(tokens, lineStartOffset, caretOffset, lineTextToCaret, prefix, anchor);
        }

        HTTPParser.RequestContext requestCtx = findRequestAtOffset(fileCtx, caretOffset);
        if (requestCtx == null) {
            return null;
        }

        CompletionResult result = tryCompleteInRequest(requestCtx, doc, rootElement, caretOffset, currentLineIndex, anchor, prefix);
        if (result != null) {
            return result;
        }

        return tryFallbackHeaderCompletion(requestCtx, doc, tokens, fullText, lineStartOffset, caretOffset, currentLineIndex, lineTextToCaret, prefix, anchor);
    }

    private static CompletionResult handleNewRequestBlock(CommonTokenStream tokens, int lineStartOffset, int caretOffset, String lineTextToCaret, String prefix, int anchor) {
        if (lineTextToCaret.isBlank()) {
            return createMethodResult("", caretOffset, caretOffset);
        }

        if (isOnlyAlphaOrWhitespace(tokens, lineStartOffset, caretOffset)) {
            return createMethodResult(prefix, anchor, caretOffset);
        }

        return null;
    }

    private static CompletionResult tryCompleteInRequest(HTTPParser.RequestContext requestCtx, Document doc, Element rootElement, int caretOffset, int currentLineIndex, int anchor, String prefix) throws BadLocationException {
        // Check RequestLine context
        RequestLineContext reqLineCtx = requestCtx.requestLine();
        if (reqLineCtx != null && containsOffset(reqLineCtx, caretOffset)) {
            CompletionResult result = handleRequestLineContext(reqLineCtx, doc, caretOffset, anchor, prefix);
            if (result != null) {
                return result;
            }
        }

        // Check HeaderField context
        HTTPParser.HeaderFieldContext headerFieldCtx = findHeaderFieldAtOffset(requestCtx, caretOffset, rootElement, currentLineIndex);
        if (headerFieldCtx != null) {
            CompletionResult result = handleHeaderFieldContext(headerFieldCtx, doc, caretOffset, anchor, prefix);
            if (result != null) {
                return result;
            }
        }

        // Inside body - no suggestions
        HTTPParser.RequestBodySectionContext bodySectionCtx = requestCtx.requestBodySection();
        if (bodySectionCtx != null && containsOffset(bodySectionCtx, caretOffset)) {
            return new CompletionResult(Collections.emptyList(), caretOffset);
        }

        return null;
    }

    private static CompletionResult tryFallbackHeaderCompletion(HTTPParser.RequestContext requestCtx, Document doc, CommonTokenStream tokens, String fullText, int lineStartOffset, int caretOffset, int currentLineIndex, String lineTextToCaret, String prefix, int anchor) throws BadLocationException {
        if (currentLineIndex == 0) {
            return null;
        }

        ParserRuleContext lastCtx = findLastContextBeforeOffset(requestCtx, lineStartOffset);
        if (lastCtx == null || lastCtx.stop == null) {
            return null;
        }

        int endOfLastContext = lastCtx.stop.getStopIndex() + 1;
        if (hasBlankLineBetween(fullText, endOfLastContext, lineStartOffset)) {
            return null;
        }

        if (lineTextToCaret.isBlank()) {
            return createHeaderKeyResult(doc, "", caretOffset, caretOffset);
        }

        if (!prefix.isEmpty() && isOnlyHeaderKeyChars(tokens, lineStartOffset, caretOffset)) {
            return createHeaderKeyResult(doc, prefix, anchor, caretOffset);
        }

        return null;
    }

    private static CompletionResult handleRequestLineContext(
        HTTPParser.RequestLineContext ctx, Document doc, int caretOffset, int anchor, String prefix)
        throws BadLocationException {

        Element rootElement = doc.getDefaultRootElement();
        int caretLine = rootElement.getElementIndex(caretOffset);
        int requestLine = rootElement.getElementIndex(ctx.start.getStartIndex());

        if (caretLine > requestLine) {
            return null;
        }

        // ANTLR context-based suggestions
        TerminalNode methodNode = ctx.METHOD();
        HTTPParser.RequestTargetContext targetCtx = ctx.requestTarget();
        HTTPParser.HttpVersionContext versionCtx = ctx.httpVersion();

        int methodStart = startOf(methodNode);
        int methodEnd = endOf(methodNode);
        int targetStart = startOf(targetCtx);
        int targetEnd = endOf(targetCtx);
        int versionStart = startOf(versionCtx);
        int versionEnd = endOf(versionCtx);

        // Caret at/within METHOD
        if (isInRange(caretOffset, methodStart, methodEnd)) {
            boolean noTargetOverlap = targetStart == INVALID || caretOffset < targetStart || (caretOffset == targetStart && targetStart == methodEnd);
            if (noTargetOverlap) {
                return createMethodResult(prefix, anchor, caretOffset);
            }
        }

        // Caret at/within URL
        if (isInRange(caretOffset, targetStart, targetEnd) && (versionStart == INVALID || caretOffset < versionStart)) {
            return createUrlResult(prefix, anchor, caretOffset);
        }

        // Caret at/within HTTP version
        if (isInRange(caretOffset, versionStart, versionEnd)) {
            return createVersionResult(doc, prefix, anchor, caretOffset);
        }

        return null;
    }

    private static CompletionResult handleHeaderFieldContext(HTTPParser.HeaderFieldContext ctx, Document doc, int caretOffset, int anchor, String prefix) throws BadLocationException {
        HTTPParser.HeaderFieldNameContext nameCtx = ctx.headerFieldName();
        TerminalNode colonNode = ctx.COLON();
        HTTPParser.HeaderFieldValueContext valueCtx = ctx.headerFieldValue();

        int nameStart = startOf(nameCtx);
        int nameEnd = endOf(nameCtx);
        int colonPos = startOf(colonNode);
        int valueStart = startOf(valueCtx);
        int valueEnd = endOf(valueCtx);

        // Caret at/inside header name (before colon)
        boolean caretInName = isInRange(caretOffset, nameStart, nameEnd) && (colonPos == INVALID || caretOffset < colonPos);
        if (caretInName) {
            return createHeaderKeyResult(doc, prefix, anchor, caretOffset);
        }

        // Caret between name and colon — no completion
        if (nameEnd != INVALID && caretOffset > nameEnd && (colonPos == INVALID || caretOffset <= colonPos)) {
            return null;
        }

        // Caret after colon — offer header value completion
        String headerKey = (nameCtx != null) ? nameCtx.getText().trim() : "";
        List<String> values = HTTPCompletionData.getHeaderValues(headerKey);

        if (values.isEmpty()) {
            return null;
        }

        if (colonPos != INVALID && caretOffset > colonPos) {
            String textAfterColon = doc.getText(colonPos + 1, caretOffset - (colonPos + 1));
            if (textAfterColon.isBlank()) {
                return createHeaderValueResult(doc, values, "", caretOffset, caretOffset);
            }
        }

        if (isInRange(caretOffset, valueStart, valueEnd)) {
            return createHeaderValueResult(doc, values, prefix, anchor, caretOffset);
        }

        return null;
    }

    // --- Offset extraction helpers ---

    private static int startOf(TerminalNode node) {
        if (node == null || node.getSymbol() == null) {
            return INVALID;
        }

        return node.getSymbol().getStartIndex();
    }

    private static int endOf(TerminalNode node) {
        if (node == null || node.getSymbol() == null) {
            return INVALID;
        }

        return node.getSymbol().getStopIndex() + 1;
    }

    private static int startOf(ParserRuleContext ctx) {
        if (ctx == null || ctx.start == null) {
            return INVALID;
        }

        return ctx.start.getStartIndex();
    }

    private static int endOf(ParserRuleContext ctx) {
        if (ctx == null || ctx.stop == null) {
            return INVALID;
        }

        return ctx.stop.getStopIndex() + 1;
    }

    private static boolean isInRange(int offset, int rangeStart, int rangeEnd) {
        return rangeStart != INVALID && rangeEnd != INVALID && offset >= rangeStart && offset <= rangeEnd;
    }

    // --- Token-based checks ---

    private static boolean isOnlyAlphaOrWhitespace(CommonTokenStream tokens, int lineStart, int caretOffset) {
        return CompletionTokenAnalyzer.containsOnlyAllowedTokenTypes(tokens, lineStart, caretOffset, HTTPLexer.WS, HTTPLexer.ALPHA_CHARS);
    }

    private static boolean isOnlyHeaderKeyChars(CommonTokenStream tokens, int lineStart, int caretOffset) {
        return CompletionTokenAnalyzer.containsOnlyAllowedTokenTypes(tokens, lineStart, caretOffset, HTTPLexer.WS, HTTPLexer.ALPHA_CHARS, HTTPLexer.DASH);
    }

    // --- Result factory methods ---

    private static CompletionResult createMethodResult(String prefix, int anchor, int caretOffset) {
        return createResult(HTTPCompletionData.getMethods(), prefix, anchor, " ", caretOffset, true);
    }

    private static CompletionResult createUrlResult(String prefix, int anchor, int caretOffset) {
        List<CompletionItem> items = new ArrayList<>();
        String upperPrefix = prefix.toUpperCase();

        for (String url : HTTPCompletionData.getUrls()) {
            if (prefix.isEmpty() || url.toUpperCase().startsWith(upperPrefix)) {
                boolean isSimpleUrl = url.equals("/") || url.equals("*");
                String suffix = isSimpleUrl ? " " : "";
                items.add(new HTTPCompletionItem(url, anchor, suffix, caretOffset, isSimpleUrl));
            }
        }

        return items.isEmpty() ? null : new CompletionResult(items, anchor);
    }

    private static CompletionResult createVersionResult(Document doc, String prefix, int anchor, int caretOffset) throws BadLocationException {
        String suffix = hasContentOnNextLine(doc, caretOffset) ? "" : "\n";
        return createResult(HTTPCompletionData.getVersions(), prefix, anchor, suffix, caretOffset, false);
    }

    private static CompletionResult createHeaderKeyResult(Document doc, String prefix, int anchor, int caretOffset) throws BadLocationException {
        List<CompletionItem> items = new ArrayList<>();
        String upperPrefix = prefix.toUpperCase();

        for (String key : HTTPCompletionData.getHeaderKeys()) {
            if (prefix.isEmpty() || key.toUpperCase().startsWith(upperPrefix)) {
                boolean obsolete = HTTPCompletionData.isObsoleteHeader(key);
                items.add(new HTTPCompletionItem(key, anchor, ": ", caretOffset, true, obsolete));
            }
        }

        return items.isEmpty() ? null : new CompletionResult(items, anchor);
    }

    private static CompletionResult createHeaderValueResult(Document doc, List<String> values, String prefix, int anchor, int caretOffset) throws BadLocationException {
        if (values.isEmpty()) {
            return null;
        }

        boolean hasNextContent = hasContentOnNextLine(doc, caretOffset);
        String suffix = hasNextContent ? "" : "\n";
        return createResult(values, prefix, anchor, suffix, caretOffset, !hasNextContent);
    }

    private static CompletionResult createResult(List<String> candidates, String prefix, int anchor, String suffix, int caretOffset, boolean triggerNext) {
        List<CompletionItem> items = new ArrayList<>();
        String upperPrefix = prefix.toUpperCase();

        for (String candidate : candidates) {
            if (prefix.isEmpty() || candidate.toUpperCase().startsWith(upperPrefix)) {
                items.add(new HTTPCompletionItem(candidate, anchor, suffix, caretOffset, triggerNext));
            }
        }

        return items.isEmpty() ? null : new CompletionResult(items, anchor);
    }

    // --- Helper methods ---

    private static String buildPrefix(String lineTextToCaret, String lineTextAfterCaret, int wordStartOffset) {
        int wordEndRelative = 0;

        for (int i = 0; i < lineTextAfterCaret.length(); i++) {
            char c = lineTextAfterCaret.charAt(i);
            if (Character.isWhitespace(c) || c == ':' || c == '/' || c == '\n' || c == '\r') {
                break;
            }

            wordEndRelative++;
        }

        String beforeCaret = lineTextToCaret.substring(wordStartOffset);
        String afterCaret = wordEndRelative > 0 ? lineTextAfterCaret.substring(0, wordEndRelative) : "";
        return beforeCaret + afterCaret;
    }

    private static int findLastWordStart(String lineTextToCaret) {
        int wordStart = lineTextToCaret.length();
        boolean foundChar = false;

        for (int i = lineTextToCaret.length() - 1; i >= 0; i--) {
            char c = lineTextToCaret.charAt(i);

            if (Character.isWhitespace(c)) {
                if (foundChar) {
                    wordStart = i + 1;
                    break;
                }

                wordStart = i;
            } else {
                foundChar = true;
                wordStart = i;
            }
        }

        return Math.max(0, wordStart);
    }

    private static boolean isStartOfRequestBlock(CommonTokenStream tokens, int lineStartOffset) {
        if (lineStartOffset <= 0 || tokens == null || tokens.size() == 0) {
            return true;
        }

        int tokenIdx = CompletionTokenAnalyzer.findTokenIndexAtOffset(tokens, lineStartOffset - 1);

        for (int i = tokenIdx; i >= 0; i--) {
            Token t = tokens.get(i);

            if (t == null || t.getChannel() == Token.HIDDEN_CHANNEL) {
                continue;
            }

            int type = t.getType();

            if (type == HTTPLexer.WS || type == HTTPLexer.NEWLINE || type == HTTPLexer.COMMENT) {
                continue;
            }

            return type == HTTPLexer.REQUEST_SEPARATOR;
        }

        return true;
    }

    private static HTTPParser.RequestContext findRequestAtOffset(HTTPParser.HttpRequestsFileContext fileCtx, int offset) {
        HTTPParser.RequestContext nearest = null;

        for (HTTPParser.RequestBlockContext block : fileCtx.requestBlock()) {
            HTTPParser.RequestContext req = block.request();

            if (req == null || req.start == null) {
                continue;
            }

            if (containsOffset(req, offset)) {
                return req;
            }

            if (req.start.getStartIndex() <= offset) {
                nearest = req;
            }
        }

        return nearest;
    }

    private static boolean containsOffset(ParserRuleContext ctx, int offset) {
        if (ctx == null || ctx.start == null || ctx.stop == null) {
            return false;
        }

        return ctx.start.getStartIndex() <= offset && offset <= ctx.stop.getStopIndex() + 1;
    }

    private static HTTPParser.HeaderFieldContext findHeaderFieldAtOffset(HTTPParser.RequestContext requestCtx, int caretOffset, Element rootElement, int currentLineIndex) {
        HTTPParser.RequestHeadersContext headersCtx = requestCtx.requestHeaders();

        if (headersCtx == null) {
            return null;
        }

        for (HTTPParser.HeaderLineContext headerLine : headersCtx.headerLine()) {
            HTTPParser.HeaderContext header = headerLine.header();

            if (header == null) {
                continue;
            }

            HTTPParser.HeaderFieldContext field = header.headerField();

            if (field == null || field.start == null) {
                continue;
            }

            int headerCtxLine = rootElement.getElementIndex(field.start.getStartIndex());

            if (headerCtxLine == currentLineIndex || headerCtxLine == currentLineIndex - 1) {
                return field;
            }
        }

        return null;
    }

    private static ParserRuleContext findLastContextBeforeOffset(HTTPParser.RequestContext requestCtx, int targetOffset) {
        ParserRuleContext last = null;

        RequestLineContext reqLine = requestCtx.requestLine();

        if (reqLine != null && reqLine.stop != null && reqLine.stop.getStopIndex() < targetOffset) {
            last = reqLine;
        }

        HTTPParser.RequestHeadersContext headersCtx = requestCtx.requestHeaders();

        if (headersCtx == null) {
            return last;
        }

        for (HTTPParser.HeaderLineContext headerLine : headersCtx.headerLine()) {
            HTTPParser.HeaderContext header = headerLine.header();

            if (header == null) {
                continue;
            }

            HTTPParser.HeaderFieldContext field = header.headerField();

            if (field != null && field.stop != null && field.stop.getStopIndex() < targetOffset) {
                last = field;
            }
        }

        return last;
    }

    private static boolean hasContentOnNextLine(Document doc, int caretOffset) throws BadLocationException {
        Element rootElement = doc.getDefaultRootElement();
        int currentLineIndex = rootElement.getElementIndex(caretOffset);

        if (currentLineIndex + 1 >= rootElement.getElementCount()) {
            return false;
        }

        Element nextLineElement = rootElement.getElement(currentLineIndex + 1);
        int nextLineStart = nextLineElement.getStartOffset();
        int nextLineEnd = nextLineElement.getEndOffset();
        String nextLineText = doc.getText(nextLineStart, Math.min(nextLineEnd - nextLineStart, doc.getLength() - nextLineStart)).trim();
        return !nextLineText.isEmpty() && !nextLineText.equals("\n");
    }

    private static boolean hasBlankLineBetween(String fullText, int startOffset, int endOffset) {
        if (startOffset < 0 || endOffset > fullText.length()) {
            return false;
        }

        String gap = fullText.substring(startOffset, endOffset);
        return gap.contains("\n\n") || gap.contains("\r\n\r\n");
    }
}
