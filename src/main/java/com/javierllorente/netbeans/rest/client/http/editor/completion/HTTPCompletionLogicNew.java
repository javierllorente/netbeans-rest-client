// main/java/com/javierllorente/netbeans/rest/client/http/editor/completion/HTTPCompletionLogic.java
package com.javierllorente.netbeans.rest.client.http.editor.completion;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser.RequestLineContext;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
// Import für StyledDocument entfernt, da doc.getDefaultRootElement() von Document kommt
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;
import org.netbeans.spi.editor.completion.CompletionItem;

public final class HTTPCompletionLogicNew {

    private static final Logger log = Logger.getLogger(HTTPCompletionLogicNew.class.getName());

    private static final Pattern METHOD_SPACE_PATTERN = Pattern.compile("^\\s*([A-Z]+)\\s$");
    private static final Pattern METHOD_URL_SPACE_PATTERN = Pattern.compile("^\\s*([A-Z]+)\\s+([^\\s]+)\\s$");

    private static final List<String> METHODS = List.of(
        "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "TRACE", "CONNECT"
    );
    private static final List<String> URLS = List.of(
        "http://", "https://", "http://localhost:", "/"
    );
    private static final List<String> VERSIONS = List.of("HTTP/1.1", "HTTP/2", "HTTP/1.0");
    private static final List<String> HEADER_KEYS = List.of(
        "Accept", "Authorization", "Content-Type", "User-Agent", "Host", "Connection", "Cache-Control", "Cookie"
    );
    private static final Map<String, List<String>> HEADER_VALUES = Map.ofEntries(
        Map.entry("Content-Type", List.of("application/json", "application/xml", "text/plain")),
        Map.entry("Accept", List.of("application/json", "application/xml", "*/*"))
    );

    private HTTPCompletionLogicNew() {
    }

    public static class CompletionResult {

        public final Collection<CompletionItem> items;
        public final int anchor;

        public CompletionResult(Collection<CompletionItem> items, int anchor) {
            this.items = items;
            this.anchor = anchor;
        }
    }

    public static CompletionResult compute(Document doc, int caretOffset) throws BadLocationException {
        long startTime = System.currentTimeMillis();
        String fullText = doc.getText(0, doc.getLength());
        Element rootElement = doc.getDefaultRootElement();

        if (fullText.trim().isEmpty() && caretOffset == 0) {
            log.log(Level.FINE, "Empty file at offset 0, suggesting METHODS");
            return createResult(METHODS, "", caretOffset, " ", caretOffset);
        }

        int currentLineIndex = rootElement.getElementIndex(caretOffset);
        int lineStartOffset = rootElement.getElement(currentLineIndex).getStartOffset();
        String lineTextToCaret = doc.getText(lineStartOffset, caretOffset - lineStartOffset);
        int wordStartOffset = findLastWordStart(lineTextToCaret);
        int anchor = lineStartOffset + wordStartOffset;
        String prefix = lineTextToCaret.substring(wordStartOffset);

        log.log(Level.FINE, "Caret: {0}, LineStart: {1}, Anchor: {2}, Prefix: ''{3}'', LineTextToCaret: ''{4}''",
            new Object[]{caretOffset, lineStartOffset, anchor, prefix, lineTextToCaret.replace("\n", "\\n")});

        HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(fullText));
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HTTPParser parser = new HTTPParser(tokens);
        parser.removeErrorListeners();

        ParseTree tree;
        try {
            tree = parser.httpRequestsFile();
        } catch (Exception e) {
            log.log(Level.SEVERE, "ANTLR parsing failed", e);
            if (currentLineIndex == 0 && lineTextToCaret.matches("\\s*[A-Za-z]*")) {
                log.log(Level.WARNING, "ANTLR failed, likely start of first line. Suggesting METHODS based on prefix ''{0}''", prefix);
                return createResult(METHODS, prefix, anchor, " ", caretOffset);
            }
            return null;
        }
        tokens.fill();

        boolean isNewLineAfterSeparator = isStartOfRequestBlock(tokens, lineStartOffset);
        if (isNewLineAfterSeparator) {
            if (lineTextToCaret.isBlank()) {
                log.log(Level.FINE, "Context: Start of new request after separator/start on empty line. Suggesting METHODS.");
                return createResult(METHODS, "", caretOffset, " ", caretOffset);
            } else if (lineTextToCaret.matches("\\s*[A-Za-z]*")) {
                log.log(Level.FINE, "Context: Typing method after separator/start. Filtering METHODS by ''{0}''", prefix);
                return createResult(METHODS, prefix, anchor, " ", caretOffset);
            }
            log.log(Level.FINE, "Line after separator has content (''{0}''). Proceeding.", lineTextToCaret);
        }

        ParseTree nodeNearCaret = findNodeCovering(tree, caretOffset);
        log.log(Level.FINER, "Node near caret: {0} ({1})", new Object[]{nodeNearCaret != null ? nodeNearCaret.getText() : "null", nodeNearCaret != null ? nodeNearCaret.getClass().getSimpleName() : "N/A"});

        RequestLineContext reqLineCtx = findAncestor(nodeNearCaret, RequestLineContext.class);
        // **WICHTIGE ÄNDERUNG HIER:** Handle RequestLineContext *nur*, wenn es wirklich für die aktuelle Zeile relevant ist.
        // Die Zeilenprüfung ist jetzt *in* handleRequestLineContext.
        CompletionResult reqLineCompletion = null;
        if (reqLineCtx != null) {
            log.log(Level.FINE, "Context Check: Potential RequestLine context found.");
            reqLineCompletion = handleRequestLineContext(reqLineCtx, doc, caretOffset, anchor, prefix, lineTextToCaret);
            logCompletionResult(reqLineCompletion, "RequestLine");
            if (reqLineCompletion != null) {
                return reqLineCompletion;
            }
        }

        // Wenn reqLineCompletion null ist (weil Cursor nicht mehr auf Request-Zeile oder keine passende Sub-Regel),
        // dann werden die weiteren Checks durchgeführt.
        HTTPParser.HeaderFieldContext headerFieldCtx = findAncestor(nodeNearCaret, HTTPParser.HeaderFieldContext.class);
        if (headerFieldCtx != null) {
            log.log(Level.FINE, "Context Check: Inside HeaderField");
            CompletionResult headerResult = handleHeaderFieldContext(headerFieldCtx, doc, caretOffset, anchor, prefix);
            logCompletionResult(headerResult, "HeaderField");
            if (headerResult != null) {
                return headerResult;
            }
        }

        HTTPParser.RequestBodyContext reqBodyCtx = findAncestor(nodeNearCaret, HTTPParser.RequestBodyContext.class);
        if (reqBodyCtx != null) {
            log.log(Level.FINE, "Context Check: Inside RequestBody. No suggestions.");
            return null;
        }

        // Fallback for new lines (expecting headers)
        // Dieser Block wird jetzt nur erreicht, wenn reqLineCompletion oben null war.
        if (reqLineCompletion == null && headerFieldCtx == null && reqBodyCtx == null
            && !isNewLineAfterSeparator
            && currentLineIndex > 0) {

            ParserRuleContext lastSignificantContext = findLastSignificantContext(tree, lineStartOffset);

            if (lastSignificantContext instanceof HTTPParser.RequestLineContext
                || lastSignificantContext instanceof HTTPParser.HeaderFieldContext) {
                log.log(Level.FINER, "Fallback: Last significant context before line {0} was {1}",
                    new Object[]{currentLineIndex, lastSignificantContext.getClass().getSimpleName()});

                int endOfLastContext = (lastSignificantContext.stop != null) ? lastSignificantContext.stop.getStopIndex() + 1 : -1;
                boolean doubleNewlineFound = (endOfLastContext != -1)
                    && hasDoubleNewlineBetweenTokens(tokens, endOfLastContext, lineStartOffset);

                if (!doubleNewlineFound) {
                    if (lineTextToCaret.isBlank()) {
                        log.log(Level.FINE, "Fallback Context: Start of new line after RequestLine/HeaderField (NO double newline). Suggesting HEADER_KEYS.");
                        return createResult(HEADER_KEYS, "", caretOffset, ": ", caretOffset);
                    } else if (!prefix.isEmpty()) {
                        log.log(Level.FINE, "Fallback Context: Typing on new line after RequestLine/HeaderField (NO double newline). Filtering HEADER_KEYS by ''{0}''", prefix);
                        return createResult(HEADER_KEYS, prefix, anchor, ": ", caretOffset);
                    }
                } else {
                    log.log(Level.FINE, "Fallback Context: New line, but double newline detected. Assuming body. No Header suggestions.");
                }
            } else {
                log.log(Level.FINE, "Fallback Context: New line, but previous significant context ({0}) was not RequestLine/Header.",
                    lastSignificantContext != null ? lastSignificantContext.getClass().getSimpleName() : "null");
            }
        }

        log.log(Level.INFO, "No specific completion context found by any check.");
        long endTime = System.currentTimeMillis();
        log.log(Level.FINER, "Completion compute took: {0} ms", (endTime - startTime));
        return null;
    }

    // --- Handler Methods ---
    private static CompletionResult handleRequestLineContext(
        HTTPParser.RequestLineContext ctx, // Kann null sein, wenn ANTLR es nicht als solches erkennt
        Document doc,
        int caretOffset,
        int anchor,
        String prefix,
        String lineTextToCaret)
        throws BadLocationException {

        // Ermittle die Zeilennummer des Carets
        int caretLineActualNum = doc.getDefaultRootElement().getElementIndex(caretOffset);

        // Wenn ein ANTLR RequestLineContext (ctx) übergeben wurde, prüfe dessen Zeilennummer
        int requestLineActualLineNum = -1;
        if (ctx != null && ctx.start != null) {
            requestLineActualLineNum = doc.getDefaultRootElement().getElementIndex(ctx.start.getStartIndex());
        }

        log.log(Level.FINER,
            "Entering handleRequestLineContext. CaretLine={0}, CaretOffset={1}, LineTextToCaret=''{2}'', Anchor={3}, Prefix=''{4}'', RequestLineStartsAtLine={5}, CTX_Text=''{6}''",
            new Object[]{caretLineActualNum, caretOffset, lineTextToCaret.replace("\r", "\\r").replace("\n", "\\n"),
                anchor, prefix, requestLineActualLineNum, ctx != null ? ctx.getText().replace("\r", "\\r").replace("\n", "\\n") : "null"});

        // **Strikte Zeilenprüfung:** Diese Methode ist NUR für die aktuelle Zeile zuständig, in der sich der Caret befindet.
        // Wenn der ANTLR-Kontext `ctx` von einer VORHERIGEN Zeile stammt, ignorieren wir ihn für URL/Version-Vorschläge hier.
        // Die Header-Logik in `compute` kümmert sich um neue Zeilen nach einer RequestLine.
        if (ctx != null && requestLineActualLineNum != -1 && caretLineActualNum != requestLineActualLineNum) {
            // Wenn der ANTLR-Kontext von einer anderen Zeile ist als der Cursor, ist dieser Handler nicht zuständig.
            // Ausnahme: Cursor ist am Ende der Request-Zeile, aber lineTextToCaret ist leer, weil der Cursor
            // technisch schon am Anfang der *nächsten* Zeile steht, aber logisch noch zur Request-Line gehört.
            // Dies wird kompliziert. Einfacher: Wenn Zeilennummern abweichen, soll compute() entscheiden.
            if (caretLineActualNum > requestLineActualLineNum) { // Sicher auf neuer Zeile
//                log.log(Level.FINE, "RLCTX: Caret (line {0}) is on a new line, ANTLR context from prev line ({1}). Aborting RLCTX handler for URL/Version.",
//                    caretLineActualNum, requestLineActualLineNum);
                return null;
            }
        }

        // --- Priority 1: Exact text matches for transitions based on lineTextToCaret ---
        Matcher methodSpaceMatcher = METHOD_SPACE_PATTERN.matcher(lineTextToCaret);
        if (methodSpaceMatcher.matches()) {
            log.log(Level.FINE, "RLCTX: Text Match METHOD_SPACE_PATTERN. Suggesting URLS. Anchor for new token: {0}, Prefix: \"\"", caretOffset);
            return createResult(URLS, "", caretOffset, " ", caretOffset);
        }

        Matcher methodUrlSpaceMatcher = METHOD_URL_SPACE_PATTERN.matcher(lineTextToCaret);
        if (methodUrlSpaceMatcher.matches()) {
            log.log(Level.FINE, "RLCTX: Text Match METHOD_URL_SPACE_PATTERN. Suggesting VERSIONS. Anchor for new token: {0}, Prefix: \"\"", caretOffset);
            return createResult(VERSIONS, "", caretOffset, "\n", caretOffset);
        }

        // --- Priority 2: ANTLR context-based suggestions (wenn Text-Patterns nicht griffen, z.B. Cursor ist *in* einem Token) ---
        // Dieser Teil ist nur relevant, wenn ctx != null UND der Cursor auf derselben Zeile ist wie ctx.
        if (ctx == null || (requestLineActualLineNum != -1 && caretLineActualNum != requestLineActualLineNum)) {
            // Wenn ctx null ist oder wir auf einer anderen Zeile sind, aber die Regexes nicht griffen,
            // und wir noch am Zeilenanfang sind und es wie eine Methode aussieht:
            if (anchor == getLineStartOffset(doc, caretOffset) && lineTextToCaret.matches("\\s*[A-Za-z]*")) {
                log.log(Level.FINE, "RLCTX: ANTLR Context null/mismatch OR Text patterns failed, but at line start, looks like METHOD. Anchor:{0}, Prefix:{1}", new Object[]{anchor, prefix});
                return createResult(METHODS, prefix, anchor, " ", caretOffset);
            }
            log.log(Level.WARNING, "RLCTX: ANTLR context is null or for different line, and text patterns did not match. No specific suggestions from RLCTX handler.");
            return null; // Keine ANTLR-basierte Logik ohne passenden Kontext für die aktuelle Zeile
        }

        TerminalNode methodNode = ctx.METHOD();
        HTTPParser.RequestTargetContext targetCtx = ctx.requestTarget();
        HTTPParser.HttpVersionContext versionCtx = ctx.httpVersion();

        int methodStart = (methodNode != null && methodNode.getSymbol() != null) ? methodNode.getSymbol().getStartIndex() : -1;
        int methodEnd = (methodNode != null && methodNode.getSymbol() != null) ? methodNode.getSymbol().getStopIndex() + 1 : -1;
        int targetStart = (targetCtx != null && targetCtx.start != null) ? targetCtx.start.getStartIndex() : -1;
        int targetEnd = (targetCtx != null && targetCtx.stop != null) ? targetCtx.stop.getStopIndex() + 1 : -1;
        int versionStart = (versionCtx != null && versionCtx.start != null) ? versionCtx.start.getStartIndex() : -1;
        int versionEnd = (versionCtx != null && versionCtx.stop != null) ? versionCtx.stop.getStopIndex() + 1 : -1;

        // ANTLR Fallback Case 1: Caret is AT or WITHIN a parsed METHOD
        if (methodNode != null && methodStart != -1 && methodEnd != -1 && caretOffset >= methodStart && caretOffset <= methodEnd) {
            if (targetStart == -1 || caretOffset < targetStart || (caretOffset == targetStart && targetStart == methodEnd)) {
                log.log(Level.FINE, "RLCTX ANTLR: Caret at/within METHOD. Anchor:{0}, Prefix:{1}", new Object[]{anchor, prefix});
                return createResult(METHODS, prefix, anchor, " ", caretOffset);
            }
        }

        // ANTLR Fallback Case 2: Caret is AT or WITHIN a parsed URL/RequestTarget
        if (targetCtx != null && targetStart != -1 && targetEnd != -1 && caretOffset >= targetStart && caretOffset <= targetEnd
            && (versionStart == -1 || caretOffset < versionStart)) {
            log.log(Level.FINE, "RLCTX ANTLR: Caret at/within RequestTarget. Anchor:{0}, Prefix:{1}", new Object[]{anchor, prefix});
            return createResult(URLS, prefix, anchor, " ", caretOffset);
        }

        // ANTLR Fallback Case 3: Caret is AT or WITHIN a parsed HTTP_VERSION
        if (versionCtx != null && versionStart != -1 && versionEnd != -1 && caretOffset >= versionStart && caretOffset <= versionEnd) {
            log.log(Level.FINE, "RLCTX ANTLR: Caret at/within HttpVersion. Anchor:{0}, Prefix:{1}", new Object[]{anchor, prefix});
            return createResult(VERSIONS, prefix, anchor, "\n", caretOffset);
        }

        // ANTLR Fallback Case 4: Caret is AFTER a parsed method, but NO target/version yet
        if (methodEnd != -1 && caretOffset > methodEnd && targetCtx == null && versionCtx == null) {
            String textAfterMethod = doc.getText(methodEnd, caretOffset - methodEnd);
            if (textAfterMethod.isBlank() && !textAfterMethod.isEmpty()) {
                log.log(Level.FINE, "RLCTX ANTLR: Whitespace after parsed METHOD, no target/version parsed. Suggesting URLS. Anchor:{0}, Prefix:\"\"", caretOffset);
                return createResult(URLS, "", caretOffset, " ", caretOffset);
            }
        }

        // ANTLR Fallback Case 5: Caret is AFTER a parsed target, but NO version yet
        if (targetEnd != -1 && caretOffset > targetEnd && versionCtx == null) {
            String textAfterTarget = doc.getText(targetEnd, caretOffset - targetEnd);
            if (textAfterTarget.isBlank() && !textAfterTarget.isEmpty()) {
                log.log(Level.FINE, "RLCTX ANTLR: Whitespace after parsed RequestTarget, no version parsed. Suggesting VERSIONS. Anchor:{0}, Prefix:\"\"", caretOffset);
                return createResult(VERSIONS, "", caretOffset, "\n", caretOffset);
            }
        }

        log.log(Level.WARNING, "RLCTX ANTLR: RequestLine context, but no specific sub-context handled for caret {0}", caretOffset);
        return null;
    }

    // ... (Rest der Methoden: handleHeaderFieldContext, isStartOfRequestBlock, createResult,
    //      findLastWordStart, findNodeCovering, findNodeCoveringRecursive, getNodeIntervalProper,
    //      findAncestor, getParentContext, findTokenIndexAt, findLastSignificantContext,
    //      getLineText, getLineStartOffset, logCompletionResult, hasDoubleNewlineBetweenTokens,
    //      HTTPCompletionItem Klasse - alle wie in deiner letzten funktionierenden Version,
    //      oder meiner vorherigen Antwort, falls dort keine Änderungen für diese nötig waren)
    private static CompletionResult handleHeaderFieldContext(HTTPParser.HeaderFieldContext ctx, Document doc, int caretOffset, int anchor, String prefix) throws BadLocationException {
        log.log(Level.FINER, "Entering handleHeaderFieldContext. Caret: {0}, Anchor: {1}, Prefix: ''{2}'', ContextText: ''{3}''",
            new Object[]{caretOffset, anchor, prefix, ctx != null ? ctx.getText() : "null"});
        if (ctx == null) {
            return null;
        }
        HTTPParser.HeaderFieldNameContext nameCtx = ctx.headerFieldName();
        TerminalNode colonNode = ctx.COLON();
        HTTPParser.HeaderFieldValueContext valueCtx = ctx.headerFieldValue();
        int nameStart = (nameCtx != null && nameCtx.start != null) ? nameCtx.start.getStartIndex() : -1;
        int nameEnd = (nameCtx != null && nameCtx.stop != null) ? nameCtx.stop.getStopIndex() + 1 : -1;
        int colonPos = (colonNode != null && colonNode.getSymbol() != null) ? colonNode.getSymbol().getStartIndex() : -1;
        int valueStart = (valueCtx != null && valueCtx.start != null) ? valueCtx.start.getStartIndex() : -1;
        int valueEnd = (valueCtx != null && valueCtx.stop != null) ? valueCtx.stop.getStopIndex() + 1 : -1;

        if (nameCtx != null && nameStart != -1 && nameEnd != -1 && caretOffset >= nameStart && caretOffset <= nameEnd && (colonPos == -1 || caretOffset < colonPos)) {
            log.log(Level.FINE, "Handling HeaderField: At/Inside Name");
            return createResult(HEADER_KEYS, prefix, anchor, ": ", caretOffset);
        }
        if (nameEnd != -1 && caretOffset > nameEnd && (colonPos == -1 || caretOffset <= colonPos)) {
            log.log(Level.FINE, "Handling HeaderField: After Name, before/at Colon. No specific suggestions.");
            return null;
        }
        if (colonPos != -1 && caretOffset > colonPos) {
            String textAfterColon = doc.getText(colonPos + 1, caretOffset - (colonPos + 1));
            if (textAfterColon.isBlank()) {
                String headerKey = (nameCtx != null) ? nameCtx.getText().trim() : "";
                List<String> values = HEADER_VALUES.getOrDefault(headerKey, Collections.emptyList());
                if (!values.isEmpty()) {
                    log.log(Level.FINE, "Handling HeaderField: After Colon + whitespace (Key: {0}), suggesting values.", headerKey);
                    return createResult(values, "", caretOffset, "\n", caretOffset);
                } else {
                    log.log(Level.FINE, "Handling HeaderField: After Colon + whitespace (Key: {0}), no predefined values.", headerKey);
                    return null;
                }
            }
        }
        if (valueCtx != null && valueStart != -1 && valueEnd != -1 && caretOffset >= valueStart && caretOffset <= valueEnd) {
            String headerKey = (nameCtx != null) ? nameCtx.getText().trim() : "";
            List<String> values = HEADER_VALUES.getOrDefault(headerKey, Collections.emptyList());
            if (!values.isEmpty()) {
                log.log(Level.FINE, "Handling HeaderField: Inside Value (Key: {0}). Filtering values.", headerKey);
                return createResult(values, prefix, anchor, "\n", caretOffset);
            } else {
                log.log(Level.FINE, "Handling HeaderField: Inside Value (Key: {0}), no predefined values.", headerKey);
                return null;
            }
        }
        log.log(Level.WARNING, "HeaderField context matched, but no specific sub-context handled for caret {0}", caretOffset);
        return null;
    }

    private static boolean isStartOfRequestBlock(CommonTokenStream tokens, int lineStartOffset) {
        log.log(Level.FINER, "Checking isStartOfRequestBlock for lineStartOffset: {0}", lineStartOffset);
        if (lineStartOffset <= 0) {
            log.log(Level.FINER, " -> Offset <= 0, is start. Returning true.");
            return true;
        }
        if (tokens == null || tokens.size() == 0) {
            log.log(Level.WARNING, " -> Token stream empty/null in isStartOfRequestBlock. Assuming start for safety.");
            return true;
        }
        int tokenIdxToInspect = findTokenIndexAt(tokens, lineStartOffset - 1);
        log.log(Level.FINER, " -> isStartOfRequestBlock: Starting search backward from token index: {0} (for line offset {1})",
            new Object[]{tokenIdxToInspect, lineStartOffset});
        for (int i = tokenIdxToInspect; i >= 0; i--) {
            Token t = tokens.get(i);
            if (t == null) {
                continue;
            }
            int type = t.getType();
            log.log(Level.FINER, " -> Inspecting token [{0}]: ''{1}'' Type: {2} Channel: {3} Start: {4} Stop: {5}",
                new Object[]{i, t.getText().replace("\n", "\\n").replace("\r", "\\r"), type, t.getChannel(), t.getStartIndex(), t.getStopIndex()});
            if (t.getChannel() == Token.HIDDEN_CHANNEL) {
                log.log(Level.FINER, " -> Skipping hidden token.");
                continue;
            }
            if (type == HTTPLexer.WS || type == HTTPLexer.NEWLINE || type == HTTPLexer.COMMENT) {
                log.log(Level.FINER, " -> Skipping explicit WS/NL/COMMENT token on default channel.");
                continue;
            }
            if (type == HTTPLexer.REQUEST_SEPARATOR) {
                log.log(Level.FINER, " -> Found REQUEST_SEPARATOR. Returning true.");
                return true;
            } else {
                log.log(Level.FINER, " -> Found other significant token (Type: {0}). Returning false.", type);
                return false;
            }
        }
        log.log(Level.FINER, " -> Reached start of stream without finding other significant tokens. Returning true (start of file).");
        return true;
    }

    private static CompletionResult createResult(List<String> candidates, String prefix, int anchor, String suffix, int caretOffset) {
        List<CompletionItem> items = new ArrayList<>();
        String upperPrefix = prefix.toUpperCase();
        for (String candidate : candidates) {
            if (prefix.isEmpty() || candidate.toUpperCase().startsWith(upperPrefix)) {
                items.add(new HTTPCompletionItem(candidate, anchor, suffix, caretOffset));
            }
        }
        log.log(Level.FINER, "Created {0} completion items for prefix ''{1}'' with anchor {2}", new Object[]{items.size(), prefix, anchor});
        return items.isEmpty() ? null : new CompletionResult(items, anchor);
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

    private static ParseTree findNodeCovering(ParseTree tree, int offset) {
        if (tree == null) {
            return null;
        }
        ParseTree bestFit = null;
        ArrayDeque<ParseTree> queue = new ArrayDeque<>();
        queue.add(tree);
        while (!queue.isEmpty()) {
            ParseTree current = queue.poll();
            Interval nodeInterval = getNodeIntervalProper(current);
            if (nodeInterval == null) {
                continue;
            }
            int nodeEnd = (current instanceof TerminalNode) ? nodeInterval.b : nodeInterval.b + 1;
            if (nodeInterval.a <= offset && offset <= nodeEnd) {
                bestFit = current;
                if (!(current instanceof TerminalNode)) {
                    for (int i = 0; i < current.getChildCount(); i++) {
                        queue.add(current.getChild(i));
                    }
                }
            }
        }
        if (bestFit instanceof ParserRuleContext) {
            ParseTree deeperFit = findNodeCoveringRecursive(bestFit, offset);
            if (deeperFit != null && deeperFit != bestFit) {
                return deeperFit;
            }
        }
        return bestFit;
    }

    private static ParseTree findNodeCoveringRecursive(ParseTree node, int offset) {
        Interval nodeInterval = getNodeIntervalProper(node);
        if (nodeInterval == null) {
            return null;
        }
        int nodeEnd = (node instanceof TerminalNode) ? nodeInterval.b : nodeInterval.b + 1;
        if (!(nodeInterval.a <= offset && offset <= nodeEnd)) {
            return null;
        }
        if (!(node instanceof TerminalNode)) {
            for (int i = 0; i < node.getChildCount(); i++) {
                ParseTree deeperNode = findNodeCoveringRecursive(node.getChild(i), offset);
                if (deeperNode != null) {
                    return deeperNode;
                }
            }
        }
        return node;
    }

    private static Interval getNodeIntervalProper(ParseTree node) {
        if (node instanceof TerminalNode) {
            Token token = ((TerminalNode) node).getSymbol();
            if (token == null) {
                return null;
            }
            return Interval.of(token.getStartIndex(), token.getStopIndex());
        } else if (node instanceof ParserRuleContext) {
            ParserRuleContext ctx = (ParserRuleContext) node;
            if (ctx.getStart() == null || ctx.getStop() == null) {
                return null;
            }
            return Interval.of(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
        }
        return null;
    }

    private static <T extends ParserRuleContext> T findAncestor(ParseTree node, Class<T> ancestorType) {
        if (node == null) {
            return null;
        }
        ParseTree current = node;
        if (ancestorType.isInstance(current) && current instanceof ParserRuleContext) {
            return ancestorType.cast(current);
        }
        RuleContext parent = getParentContext(current);
        while (parent != null) {
            if (ancestorType.isInstance(parent)) {
                return ancestorType.cast(parent);
            }
            parent = parent.getParent();
        }
        return null;
    }

    private static ParserRuleContext getParentContext(ParseTree node) {
        if (node == null || node.getParent() == null || !(node.getParent() instanceof ParserRuleContext)) {
            return null;
        }
        return (ParserRuleContext) node.getParent();
    }

    private static int findTokenIndexAt(CommonTokenStream tokens, int offset) {
        int low = 0;
        int high = tokens.size() - 1;
        int index = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (mid < 0 || mid >= tokens.size()) {
                break;
            }
            Token token = tokens.get(mid);
            if (token == null) {
                high = mid - 1;
                continue;
            }
            if (token.getStartIndex() <= offset) {
                index = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        int resultIndex = (index == -1 && tokens.size() > 0) ? 0 : Math.max(0, index);
        return Math.min(resultIndex, tokens.size() - 1);
    }

    private static ParserRuleContext findLastSignificantContext(ParseTree tree, int targetOffset) {
        if (tree == null || targetOffset <= 0) {
            return null;
        }
        ParserRuleContext bestCandidate = null;
        int maxEndOffset = -1;
        ArrayDeque<ParseTree> queue = new ArrayDeque<>();
        queue.add(tree);
        while (!queue.isEmpty()) {
            ParseTree current = queue.poll();
            Interval nodeInterval = getNodeIntervalProper(current);
            if (nodeInterval == null || nodeInterval.a >= targetOffset) {
                continue;
            }
            if (nodeInterval.b < targetOffset) {
                boolean isSignificantRule = (current instanceof HTTPParser.RequestLineContext
                    || current instanceof HTTPParser.HeaderFieldContext);
                if (isSignificantRule) {
                    if (nodeInterval.b > maxEndOffset) {
                        bestCandidate = (ParserRuleContext) current;
                        maxEndOffset = nodeInterval.b;
                    }
                }
            }
            if (!(current instanceof TerminalNode) && current instanceof ParserRuleContext && nodeInterval.a < targetOffset) {
                for (int i = 0; i < current.getChildCount(); i++) {
                    queue.add(current.getChild(i));
                }
            }
        }
        return bestCandidate;
    }

    private static String getLineText(Document doc, int offset) throws BadLocationException {
        Element line = doc.getDefaultRootElement().getElement(doc.getDefaultRootElement().getElementIndex(offset));
        int lineEndOffset = line.getEndOffset();
        if (lineEndOffset > line.getStartOffset() && doc.getText(lineEndOffset - 1, 1).equals("\n")) {
            lineEndOffset--;
        }
        return doc.getText(line.getStartOffset(), lineEndOffset - line.getStartOffset());
    }

    private static int getLineStartOffset(Document doc, int offset) throws BadLocationException {
        Element line = doc.getDefaultRootElement().getElement(doc.getDefaultRootElement().getElementIndex(offset));
        return line.getStartOffset();
    }

    private static void logCompletionResult(CompletionResult result, String context) {
        if (result != null) {
            log.log(Level.FINE, "Completion successful in context [{0}]. Anchor: {1}, Items: {2}", new Object[]{context, result.anchor, result.items.size()});
        } else {
            log.log(Level.FINE, "No completions generated for context [{0}].", context);
        }
    }

    private static boolean hasDoubleNewlineBetweenTokens(CommonTokenStream tokens, int startOffsetExclusive, int endOffsetExclusive) {
        if (startOffsetExclusive < 0 || endOffsetExclusive <= startOffsetExclusive) {
            return false;
        }
        int startIndex = findTokenIndexAt(tokens, startOffsetExclusive) + 1;
        int endIndex = findTokenIndexAt(tokens, endOffsetExclusive - 1);
        boolean firstNewlineFound = false;
        log.log(Level.FINER, "hasDoubleNewlineBetweenTokens: Checking from token index {0} to {1}", new Object[]{startIndex, endIndex});
        for (int i = startIndex; i <= endIndex; i++) {
            if (i < 0 || i >= tokens.size()) {
                continue;
            }
            Token t = tokens.get(i);
            if (t == null) {
                continue;
            }
            log.log(Level.FINER, "  -> Token {0}: ''{1}'' Type {2} Channel {3}", new Object[]{i, t.getText().replace("\n", "\\n").replace("\r", "\\r"), t.getType(), t.getChannel()});

            if (t.getChannel() == Token.HIDDEN_CHANNEL) {
                continue;
            }
            if (t.getType() == HTTPLexer.WS) {
                continue;
            }

            if (t.getType() == HTTPLexer.NEWLINE) {
                if (firstNewlineFound) {
                    log.log(Level.FINER, "  --> Double newline found!");
                    return true;
                }
                firstNewlineFound = true;
                log.log(Level.FINER, "  --> First newline found.");
            } else {
                log.log(Level.FINER, "  --> Significant token, resetting firstNewlineFound.");
                firstNewlineFound = false;
            }
        }
        log.log(Level.FINER, "No double newline found (token based).");
        return false;
    }

    private static class HTTPCompletionItem implements CompletionItem {

        private final String text;
        private final int substituteOffset;
        private final int substituteLength;
        private final String suffix;

        public HTTPCompletionItem(String text, int anchor, String suffix, int caretOffset) {
            this.text = text;
            this.substituteOffset = anchor;
            this.substituteLength = Math.max(0, caretOffset - anchor);
            this.suffix = suffix != null ? suffix : "";
        }

        @Override
        public void defaultAction(javax.swing.text.JTextComponent component) {
            log.log(Level.INFO, String.format("Default action called for item: '%s', anchor: %d, lengthToRemove: %d", text, substituteOffset, substituteLength));
            try {
                Document doc = component.getDocument();
                if (substituteLength > 0) {
                    doc.remove(substituteOffset, substituteLength);
                }
                doc.insertString(substituteOffset, text + suffix, null);
                component.setCaretPosition(substituteOffset + text.length() + suffix.length());
                org.netbeans.api.editor.completion.Completion.get().hideAll();
            } catch (BadLocationException e) {
                log.log(Level.SEVERE, "Error during completion item action", e);
            }
        }

        @Override
        public void processKeyEvent(java.awt.event.KeyEvent evt) {
        }

        @Override
        public int getPreferredWidth(java.awt.Graphics g, java.awt.Font font) {
            return org.netbeans.spi.editor.completion.support.CompletionUtilities.getPreferredWidth(text, null, g, font);
        }

        @Override
        public void render(java.awt.Graphics g, java.awt.Font defaultFont, java.awt.Color defaultColor, java.awt.Color backgroundColor, int width, int height, boolean selected) {
            org.netbeans.spi.editor.completion.support.CompletionUtilities.renderHtml(null, text, null, g, defaultFont, (selected ? java.awt.Color.WHITE : defaultColor), width, height, selected);
        }

        @Override
        public org.netbeans.spi.editor.completion.CompletionTask createDocumentationTask() {
            return null;
        }

        @Override
        public org.netbeans.spi.editor.completion.CompletionTask createToolTipTask() {
            return null;
        }

        @Override
        public boolean instantSubstitution(javax.swing.text.JTextComponent component) {
            return false;
        }

        @Override
        public int getSortPriority() {
            return 50;
        }

        @Override
        public CharSequence getSortText() {
            return text;
        }

        @Override
        public CharSequence getInsertPrefix() {
            return text;
        }
    }
}
