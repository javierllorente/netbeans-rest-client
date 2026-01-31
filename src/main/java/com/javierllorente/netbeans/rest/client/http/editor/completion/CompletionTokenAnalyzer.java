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
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

/**
 * Analyzes ANTLR token streams to determine completion context.
 *
 * <p>Provides methods to inspect token sequences within a given offset range,
 * similar to how NetBeans' {@code TokenSequence} works with
 * {@code move}/{@code moveNext} patterns.</p>
 */
public final class CompletionTokenAnalyzer {

    private CompletionTokenAnalyzer() {
    }

    /**
     * Checks whether all tokens within the given offset range are of one of the
     * specified types. Used to determine if a line fragment matches an expected
     * pattern (e.g. only alpha chars and whitespace for a method prefix).
     */
    public static boolean containsOnlyAllowedTokenTypes(CommonTokenStream tokens, int rangeStart, int rangeEnd, int... allowedTypes) {
        for (Token token : getTokensInRange(tokens, rangeStart, rangeEnd)) {
            if (!isOneOf(token.getType(), allowedTypes)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether the line contains exactly: [WS] METHOD WS — i.e. the user
     * typed a method and a space, so URL completion should trigger.
     */
    public static boolean isMethodFollowedBySpace(CommonTokenStream tokens, int lineStart, int caretOffset) {
        List<Token> lineTokens = getTokensInRange(tokens, lineStart, caretOffset);

        if (lineTokens.isEmpty()) {
            return false;
        }

        boolean foundMethod = false;
        boolean foundTrailingWhitespace = false;

        for (Token token : lineTokens) {
            int type = token.getType();

            if (isWhitespace(type)) {
                if (foundMethod) {
                    foundTrailingWhitespace = true;
                }
            } else if (type == HTTPLexer.METHOD && !foundMethod) {
                foundMethod = true;
            } else {
                return false;
            }
        }

        return foundMethod && foundTrailingWhitespace;
    }

    /**
     * Checks whether the line contains exactly: [WS] METHOD WS URL_TOKENS WS —
     * i.e. the user typed a method, a URL, and a trailing space, so HTTP version
     * completion should trigger.
     */
    public static boolean isMethodUrlFollowedBySpace(CommonTokenStream tokens, int lineStart, int caretOffset) {
        List<Token> lineTokens = getTokensInRange(tokens, lineStart, caretOffset);

        if (lineTokens.isEmpty()) {
            return false;
        }

        ParseState state = ParseState.BEFORE_METHOD;

        for (Token token : lineTokens) {
            int type = token.getType();
            boolean whitespace = isWhitespace(type);

            switch (state) {
                case BEFORE_METHOD:
                    if (whitespace) continue;
                    if (type == HTTPLexer.METHOD) { state = ParseState.AFTER_METHOD; continue; }
                    return false;

                case AFTER_METHOD:
                    if (whitespace) { state = ParseState.BEFORE_URL; continue; }
                    return false;

                case BEFORE_URL:
                    if (whitespace) continue;
                    state = ParseState.IN_URL;
                    // fall through

                case IN_URL:
                    if (whitespace) { state = ParseState.AFTER_URL; continue; }
                    continue;

                case AFTER_URL:
                    if (whitespace) continue;
                    return false;
            }
        }

        return state == ParseState.AFTER_URL;
    }

    /**
     * Finds the token index in the stream whose start offset is closest to (but
     * not after) the given document offset. Uses binary search for efficiency.
     */
    public static int findTokenIndexAtOffset(CommonTokenStream tokens, int documentOffset) {
        if (tokens == null) {
            return -1;
        }

        int firstIndex = 0;
        int lastIndex = tokens.size() - 1;
        int closestIndex = -1;

        while (firstIndex <= lastIndex) {
            int midIndex = firstIndex + (lastIndex - firstIndex) / 2;
            Token midToken = tokens.get(midIndex);

            if (midToken == null) {
                lastIndex = midIndex - 1;
                continue;
            }

            if (midToken.getStartIndex() <= documentOffset) {
                closestIndex = midIndex;
                firstIndex = midIndex + 1;
            } else {
                lastIndex = midIndex - 1;
            }
        }

        if (closestIndex == -1 && tokens.size() > 0) {
            return 0;
        }

        return Math.max(0, closestIndex);
    }

    /**
     * Collects all tokens whose range overlaps with
     * [{@code rangeStart}, {@code rangeEnd}), skipping EOF tokens.
     */
    private static List<Token> getTokensInRange(CommonTokenStream tokens, int rangeStart, int rangeEnd) {
        List<Token> result = new ArrayList<>();

        if (tokens == null || tokens.size() == 0 || rangeEnd <= rangeStart) {
            return result;
        }

        int startIndex = findTokenIndexAtOffset(tokens, rangeStart);

        for (int i = startIndex; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == Token.EOF || token.getStartIndex() >= rangeEnd) {
                break;
            }

            if (token.getStopIndex() < rangeStart) {
                continue;
            }

            result.add(token);
        }

        return result;
    }

    private static boolean isWhitespace(int tokenType) {
        return tokenType == HTTPLexer.WS || tokenType == HTTPLexer.NEWLINE;
    }

    private static boolean isOneOf(int tokenType, int... allowedTypes) {
        for (int allowed : allowedTypes) {
            if (tokenType == allowed) {
                return true;
            }
        }

        return false;
    }

    private enum ParseState {
        BEFORE_METHOD,
        AFTER_METHOD,
        BEFORE_URL,
        IN_URL,
        AFTER_URL
    }
}
