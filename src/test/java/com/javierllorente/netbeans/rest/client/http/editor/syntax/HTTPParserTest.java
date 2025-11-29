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
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for HTTP Parser
 *
 * @author Christian Lenz
 */
public class HTTPParserTest {

    /**
     * Helper method to parse HTTP text and collect errors. This uses a
     * simplified validation approach that mimics HTTPLangParserResult.
     */
    private List<String> parseAndGetErrors(String httpText) {
        List<String> errors = new ArrayList<>();

        HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(httpText));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HTTPParser parser = new HTTPParser(tokens);

        // Collect parser errors
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e) {
                errors.add(msg);
            }
        });

        HTTPParser.HttpRequestsFileContext fileCtx = parser.httpRequestsFile();

        // Perform validation (same as HTTPLangParserResult.validateRequestBodies)
        if (fileCtx != null && tokens != null) {
            for (HTTPParser.RequestBlockContext blockCtx : fileCtx.requestBlock()) {
                if (blockCtx == null) {
                    continue;
                }

                HTTPParser.RequestContext req = blockCtx.request();
                if (req == null) {
                    continue;
                }

                // Check requestLineWithBody
                HTTPParser.RequestLineWithBodyContext reqLineWithBody = req.requestLineWithBody();
                if (reqLineWithBody != null) {
                    errors.add("Invalid content found: Unknown HTTP header");
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
                    errors.add("Invalid content found: Unknown HTTP header");
                } // BODY_START_WITH_BLANK needs blank line validation
                else if (bodyTokenType == HTTPLexer.BODY_START_WITH_BLANK) {
                    if (!hasBlankLineBefore(tokens, body.start)) {
                        errors.add("Invalid content found: Unknown HTTP header");
                    }
                }
            }
        }

        return errors;
    }

    /**
     * Helper method to check if there's a blank line before
     * BODY_START_WITH_BLANK token. This is the same logic as in
     * HTTPLangParserResult.hasBlankLineBefore().
     */
    private boolean hasBlankLineBefore(CommonTokenStream tokens, org.antlr.v4.runtime.Token bodyToken) {
        if (bodyToken == null || bodyToken.getType() != HTTPLexer.BODY_START_WITH_BLANK) {
            return true;
        }

        if (tokens == null) {
            return false;
        }

        int idx = bodyToken.getTokenIndex() - 1;

        // Skip only WS tokens (not COMMENT)
        while (idx >= 0 && tokens.get(idx).getType() == HTTPLexer.WS) {
            idx--;
        }

        if (idx < 0) {
            return false;
        }

        Token prev = tokens.get(idx);

        // If previous token (after skipping WS) is NEWLINE, we have a blank line
        if (prev.getType() == HTTPLexer.NEWLINE) {
            return true;
        }

        // If previous token is COMMENT, check for TWO newlines before body
        if (prev.getType() == HTTPLexer.COMMENT) {
            idx--;

            while (idx >= 0 && tokens.get(idx).getType() == HTTPLexer.WS) {
                idx--;
            }

            if (idx >= 0 && tokens.get(idx).getType() == HTTPLexer.NEWLINE) {
                idx--;

                while (idx >= 0 && tokens.get(idx).getType() == HTTPLexer.WS) {
                    idx--;
                }

                if (idx >= 0 && tokens.get(idx).getType() == HTTPLexer.NEWLINE) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Test Case 1: Valid POST request with headers Expected: No errors
     */
    @Test
    public void testValidPostRequestWithHeaders() {
        String httpText
            = "### Request 1\n"
            + "\n"
            + "// POST request with json payload\n"
            + "POST https://google.de HTTP/1.1\n"
            + "content-type: application/json\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid POST request with headers, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 2: Valid GET request with headers and JSON body Expected: No
     * errors
     */
    @Test
    public void testValidGetRequestWithHeadersAndBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "\n"
            + "{\n"
            + "    \"test\": \"12\",\n"
            + "    \"\": 12\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 3: Valid GET request with body (no extra blank line) Expected:
     * No errors
     */
    @Test
    public void testValidGetRequestWithBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 4: Request separator properly ends previous request Expected:
     * No errors (separator cleanly ends Request 1)
     */
    @Test
    public void testRequestSeparatorEndsRequest() {
        String httpText
            = "### Request 1\n"
            + "\n"
            + "POST https://google.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "### Test\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors - separator should cleanly end request, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 5: JSON body without request line (unhappy path) Expected:
     * Error "Request body requires a request line"
     */
    @Test
    public void testBodyWithoutRequestLine() {
        String httpText
            = "### Test\n"
            + "{\n"
            + "    \"name\":12,\n"
            + "    \"time\": \"Wed, 21 Oct 2015 18:27:50 GMT\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for body without request line",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Request body requires a request line"));

        assertTrue("Expected error message 'Request body requires a request line', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 6: Body directly after request line without blank line (unhappy
     * path) Expected: Error "Unknown HTTP header"
     */
    @Test
    public void testBodyWithoutBlankLine() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for body without blank line",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 7: Body directly after request line without blank but with
     * request seperator (unhappy path) Expected: Error "Unknown HTTP header"
     */
    @Test
    public void testBodyWithoutBlankLineWithRequestSeperator() {
        String httpText
            = "### With requestseperator\n"
            + "GET http://test.de HTTP/1.1\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for body without blank line",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 8: Invalid header content (unhappy path) Expected: Error
     * "Unknown HTTP header"
     */
    @Test
    public void testInvalidHeaderContent() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "this is not a valid header\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for invalid header",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 9: Valid request with only URL (no method, no version)
     * Expected: No errors
     */
    @Test
    public void testValidRequestWithOnlyUrl() {
        String httpText
            = "test.de\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for request with only URL, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 10: Multiple empty request separators Expected: No errors
     */
    @Test
    public void testMultipleEmptySeparators() {
        String httpText
            = "###\n"
            + "###\n"
            + "### Test\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for multiple empty separators, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 11: Request with comment and space before body Expected: No
     * errors (comment counts as blank separator)
     */
    @Test
    public void testRequestWithCommentAndSpaceBeforeBodyWithoutHeader() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "// This is a comment\n"
            + "\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for space after comment before body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 12: Request with comment before body and space before comment
     * Expected: No errors (comment counts as blank separator)
     */
    @Test
    public void testRequestWithCommentAndSpaceBeforeCommentWithoutHeader() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "# This is a comment\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for space after comment before body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 13: Request with comment before body Expected: Error "Unknown
     * HTTP header" (every request needs blank line before body)
     */
    @Test
    public void testRequestWithCommentBeforeBodyWithoutHeader() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "// This is a comment\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for invalid header",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 14: Complete HTTPExample content (mixed happy/unhappy)
     * Expected: Exactly 1 error for body without request line
     */
    @Test
    public void testCompleteHTTPExampleWithErrorOfMissingRequstLineBeforeBody() {
        String httpText
            = "### Request 1\n"
            + "\n"
            + "// POST request with json payload\n"
            + "POST https://google.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "### Test\n"
            + "{\n"
            + "    \"name\":12,\n"
            + "    \"time\": \"Wed, 21 Oct 2015 18:27:50 GMT\",\n"
            + "    \"path\": \"./test/tstse\",\n"
            + "    \"urlstring\": \"https://google.de\",\n"
            + "    \"commentstring\": \"//google.de\",\n"
            + "    \"seperatorstring\": \"###google.de\",\n"
            + "    \"methodstring\": \"GET\",\n"
            + "    \"header\": \"content-type: application/json\",\n"
            + "    \"\": 33\n"
            + "}\n"
            + "\n"
            + "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "\n"
            + "{\n"
            + "    \"test\": \"12\",\n"
            + "    \"\": 12\n"
            + "}\n"
            + "\n"
            + "\n"
            + "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "{\n"
            + "    \"test\": \"12\"\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        // Should have exactly 1 error: body without request line after "### Test"
        assertEquals("Expected exactly 1 error for complete HTTPExample.http, but got: " + errors,
            1, errors.size());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Request body requires a request line"));

        assertTrue("Expected error message 'Request body requires a request line', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 15: Body directly after bare request without blank line
     * Expected: Error "Unknown HTTP header" (every request needs blank line
     * before body)
     */
    @Test
    public void testBareRequestWithoutBlankLineBeforeBody() {
        String httpText
            = "### Test\n"
            + "GET post.de\n"
            + "{\n"
            + "    \"name\":12\n"
            + "}\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for bare request without blank line before body",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 16: Simple requestline Expected: No errors
     */
    @Test
    public void testValieSimpleRequestLine() {
        String httpText = "GET http://test.de HTTP/1.1";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for simple requestline, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 17: Simple requestline with header Expected: No errors
     */
    @Test
    public void testValieSimpleRequestLineWithheader() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "content-type: application/json";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for simple requestline, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 18: requestline with header and empty body Expected: Error
     * "Unknown HTTP header" (every request needs blank line before body)
     */
    @Test
    public void testValidSimpleRequestLineWithHeaderAndInvalidEmptyBody() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "{}";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for bare request without blank line before body",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }

    /**
     * Test Case 19: 2 requestBodies with seperator Expected: No Errors
     */
    @Test
    public void testValieDoubleRequestBlocks() {
        String httpText
            = "### Request 1\n"
            + "\n"
            + "// POST request with json payload\n"
            + "POST http://test.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "\n"
            + "### Request 1\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "{\n"
            + "    \"test\": \"13\"\n"
            + "}";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for two requestBlocks with seperator, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 20: URL without method, headers or body (bare URL) Expected: No
     * errors
     */
    @Test
    public void testBareUrl() {
        String httpText = "https://jsonplaceholder.typicode.com/todos/1";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for bare URL, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 21: URL without method, headers or body with newline Expected:
     * No errors
     */
    @Test
    public void testBareUrlWithNewline() {
        String httpText = "https://jsonplaceholder.typicode.com/todos/1\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for bare URL with newline, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 22: Multiple bare URLs separated by request separator Expected:
     * No errors
     */
    @Test
    public void testMultipleBareUrls() {
        String httpText
            = "https://jsonplaceholder.typicode.com/todos/1\n"
            + "###\n"
            + "https://jsonplaceholder.typicode.com/todos/2\n"
            + "### Test\n"
            + "google.de/        HTTP/1.1\n"
            + "###\n"
            + "http://google.de                                  ";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for multiple bare URLs, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 23: Multiple origin based URLs separated by request separator
     * Expected: No errors
     */
    @Test
    public void testOriginUrls() {
        String httpText
            = "POST /url?sa=t&source=web&rct=j&url=https://zh.wikipedia.org/zh-hans/111&ved=2ahUKEwjhwLuRtbjiAhUPRK0KHRSjDpwQFjAKegQIAxAB HTTP/1.1\n"
            + "### test\n"
            + "POST /url?sa=t&source=web&rct=j HTTP/1.1\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for origin based urls /url?... URLs, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 24: Multiple bare URLs separated by request separator Expected:
     * No errors
     */
    @Test
    public void testBareUrlWithSpacesAndHaders() {
        String httpText
            = "http://google.de                                  \n"
            + "User-Agent: RestClient/0.7.1 (Windows 11 10.0; amd64) Apache NetBeans IDE 24";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for bare URL with spaces and header, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 25: Valid GET request with simple text body. Expected: No
     * errors
     */
    @Test
    public void testValidGetRequestWithTextBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "data";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 26: Valid GET request with html body. Expected: No errors
     */
    @Test
    public void testValidGetRequestWithHtmlBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "<div>Test</div>";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 27: Valid GET request with multiline text body. Expected: No
     * errors
     */
    @Test
    public void testValidGetRequestWithMultilineTextBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "\n"
            + "data\n"
            + "foo\n"
            + "bar\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with multiline text body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 28: Valid complete HTTP Example with multi requests with body
     * indentation. Expected: No errors
     */
    @Test
    public void testValidHttpExampleWithMultiRequestsAndBodyIndentation() {
        String httpText
            = "### Request 1\n"
            + "\n"
            + "    // POST request with json payload\n"
            + "    POST https://google.de HTTP/1.1\n"
            + "    content-type: application/json\n"
            + "\n"
            + "# Test\n"
            + "    {\n"
            + "        \"id\":12,\n"
            + "        \"time\": \"Wed, 21 Oct 2015 18:27:50 GMT\",\n"
            + "        \"path\": \"./test/test\",\n"
            + "        \"urlstring\": \"https://google.de\",\n"
            + "        \"commentstring\": \"//google.de\",\n"
            + "        \"seperatorstring\": \"###google.de\",\n"
            + "        \"methodstring\": \"GET\",\n"
            + "        \"header\": \"content-type: application/json\",\n"
            + "        \"array\": [\"1\", 3, \"foo\", \"bar\", 2, \"baz\"],\n"
            + "        \"\": 33,\n"
            + "        \"foo\": {\n"
            + "            \"foo\": 12,\n"
            + "            \"footer\": {\n"
            + "                \"test\": 12,\n"
            + "                \"bar\": \"asd\",\n"
            + "                \"baz\": \"asd\"\n"
            + "            }\n"
            + "        }\n"
            + "    }\n"
            + "\n"
            + "    ### Request 2\n"
            + "\n"
            + "    GET http://test.de HTTP/1.2\n"
            + "    content-type: application/json\n"
            + "\n"
            + "    ### Request 2\n"
            + "\n"
            + "    GET http://google.de HTTP/1.1\n"
            + "\n"
            + "    {\n"
            + "        \"test\": \"13\",\n"
            + "        \"test\": {\n"
            + "            \"test\": 12\n"
            + "        }\n"
            + "    }\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid multi requests, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 29: Valid complete HTTP Example with multi requests without
     * body indentation. Expected: No errors
     */
    @Test
    public void testValidHttpExampleWithMultiRequestsWithoutBodyIndentation() {
        String httpText
            = "### Request 1\n"
            + "\n"
            + "    // POST request with json payload\n"
            + "    POST https://google.de HTTP/1.1\n"
            + "    content-type: application/json\n"
            + "\n"
            + "# Test\n"
            + "{\n"
            + "    \"id\":12,\n"
            + "    \"time\": \"Wed, 21 Oct 2015 18:27:50 GMT\",\n"
            + "    \"path\": \"./test/test\",\n"
            + "    \"urlstring\": \"https://google.de\",\n"
            + "    \"commentstring\": \"//google.de\",\n"
            + "    \"seperatorstring\": \"###google.de\",\n"
            + "    \"methodstring\": \"GET\",\n"
            + "    \"header\": \"content-type: application/json\",\n"
            + "    \"array\": [\"1\", 3, \"foo\", \"bar\", 2, \"baz\"],\n"
            + "    \"\": 33,\n"
            + "    \"foo\": {\n"
            + "        \"foo\": 12,\n"
            + "        \"footer\": {\n"
            + "            \"test\": 12,\n"
            + "            \"bar\": \"asd\",\n"
            + "            \"baz\": \"asd\"\n"
            + "        }\n"
            + "    }\n"
            + "}\n"
            + "\n"
            + "    ### Request 2\n"
            + "\n"
            + "    GET http://test.de HTTP/1.2\n"
            + "    content-type: application/json\n"
            + "\n"
            + "    ### Request 2\n"
            + "\n"
            + "    GET http://google.de HTTP/1.1\n"
            + "\n"
            + "    {\n"
            + "        \"test\": \"13\",\n"
            + "        \"test\": {\n"
            + "            \"test\": 12\n"
            + "        }\n"
            + "    }\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid multi requests, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 30: Valid GET request with header and multiline text body.
     * Expected: No errors
     */
    @Test
    public void testValidGetRequestWithHeaderAndMultilineTextBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "Content-Type: application/json\n"
            + "\n"
            + "data\n"
            + "foo\n"
            + "bar\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with header and multiline text body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 31: Valid GET request with header and comment and multilinme
     * text body. Expected: No errors
     */
    @Test
    public void testValidGetRequestWithHeaderAndCOmmentMultilineTextBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "Content-Type: application/json\n"
            + "\n"
            + "# Test"
            + "data\n"
            + "foo\n"
            + "bar\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with multiline text body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 32: Valid GET request with header and multiline text body with
     * uppercase first character. Expected: No errors
     */
    @Test
    public void testValidGetRequestWithHeaderAndMultilineTextBodyAndUpperCharacterInBody() {
        String httpText
            = "### Request 2\n"
            + "\n"
            + "GET http://test.de HTTP/1.1\n"
            + "Content-Type: application/json\n"
            + "\n"
            + "Data\n"
            + "foo\n"
            + "bar\n";

        List<String> errors = parseAndGetErrors(httpText);

        assertTrue("Expected no errors for valid GET request with header and multiline text body, but got: " + errors,
            errors.isEmpty());
    }

    /**
     * Test Case 33: requestline with valid header and invalid header Expected:
     * Error "Unknown HTTP header"
     */
    @Test
    public void testValieSimpleRequestLineWithValidheaderAndInvalidHeaderPart() {
        String httpText
            = "GET http://test.de HTTP/1.1\n"
            + "content-type: application/json\n"
            + "test";

        List<String> errors = parseAndGetErrors(httpText);

        assertFalse("Expected at least one error for bare request without blank line before body",
            errors.isEmpty());

        boolean foundExpectedError = errors.stream()
            .anyMatch(msg -> msg.contains("Unknown HTTP header"));

        assertTrue("Expected error message 'Unknown HTTP header', but got: " + errors,
            foundExpectedError);
    }
}
