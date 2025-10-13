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

/**
 *
 * @author Christian Lenz <christian.lenz@gmx.net>
 */
parser grammar HTTPParser;

options {
	tokenVocab = HTTPLexer;
}

httpRequestsFile:
    (blank | COMMENT)*
    requestBlock?
    ((blank | COMMENT)* REQUEST_SEPARATOR (blank | COMMENT)* requestBlock?)*
    (blank | COMMENT)*
    EOF;

requestBlock: request | invalidBodyWithoutRequest;

request:
    requestLine requestHeaders? requestBodySection?
    | requestLineWithBody;

invalidBodyWithoutRequest:
    {
        notifyErrorListeners("Request body requires a request line");
    }
    (BODY_START_WITH_BLANK | BODY_START_NO_BLANK) jsonBodyContent;

requestHeaders: (headerLine)+;

headerLine:
    header NEWLINE?
    | invalidHeaderLine NEWLINE?;

invalidHeaderLine:
    {
        // Only call error listener if there's actual content (not just a blank line)
        getCurrentToken().getType() != NEWLINE
    }?
    {
        notifyErrorListeners("Unknown HTTP header");
    }
    invalidHeaderContent;

invalidHeaderContent: ~(NEWLINE | REQUEST_SEPARATOR | BODY_START_WITH_BLANK | BODY_START_NO_BLANK | COMMENT)+;

requestBodySection:
    (WS | NEWLINE | COMMENT)* requestBody;

requestBody:
    BODY_START_WITH_BLANK jsonBodyContent
    | BODY_START_NO_BLANK jsonBodyContent;

jsonBodyContent:
    space (pair (space COMMA space pair)*)? space CLOSE_BLOCK_BRAKET
    | {true}? ~REQUEST_SEPARATOR* CLOSE_BLOCK_BRAKET  // Catch-all: match anything until }
    | {true}? ~REQUEST_SEPARATOR*;  // Last resort: match anything

// Json structure for requestBody
jsonObject:
    OPEN_BLOCK_BRAKET space (pair (space COMMA space pair)*)? space
    (CLOSE_BLOCK_BRAKET | { notifyErrorListeners("Missing closing bracket '}' in JSON object"); });

pair:
    jsonString space COLON space jsonValue;

jsonArray:
    OPEN_BRAKET space (jsonValue (space COMMA space jsonValue)*)? space
    (CLOSE_BRAKET | { notifyErrorListeners("Missing closing bracket ']' in JSON array"); });

jsonValue:
    jsonString
    | jsonNumber
    | jsonObject
    | jsonArray
    | jsonBareWord;

jsonString: STRING | QUOTE jsonStringContent? QUOTE;

jsonStringContent: jsonStringChar+;

jsonStringChar:
    ALPHA_CHARS
    | DIGITS
    | DASH
    | UNDERSCORE
    | DOT
    | SLASH
    | COLON
    | AMPERSAND
    | PERCENT
    | QUESTION_MARK
    | HASH
    | WS
    | OPEN_BRAKET
    | CLOSE_BRAKET
    | OPEN_BLOCK_BRAKET
    | CLOSE_BLOCK_BRAKET
    | ASTERISK
    | EQUAL
    | SCHEME_SEPARATOR
    | COMMA;

jsonNumber: NUMBER;

jsonBareWord: ALPHA_CHARS;

space: (NEWLINE | WS)*;

header: headerField;

headerField: headerFieldName COLON WS* headerFieldValue;

headerFieldName: (ALPHA_CHARS | DASH | UNDERSCORE)+;
headerFieldValue: (~(NEWLINE | BODY_START_WITH_BLANK | BODY_START_NO_BLANK))*;

fieldName: QUOTE (ALPHA_CHARS | DASH | UNDERSCORE) QUOTE;

fieldValue: QUOTE? (ALPHA_CHARS | DASH | UNDERSCORE | DIGITS | SLASH)+ QUOTE?;

blank: NEWLINE | WS;

requestLine:
    (METHOD WS)? requestTarget (WS httpVersion)? NEWLINE?;

requestLineWithBody:
    (METHOD WS)? requestTarget (WS httpVersion)? BODY_START_WITH_BLANK jsonBodyContent;

requestTarget: originForm | absoluteForm | asteriskForm;

originForm: slashPathPart (queryPart | fragmentPart)*;

absolutePath: SLASH (pathSeparator segment)+;

segment: (ALPHA_CHARS | DIGITS | DASH | UNDERSCORE)+;

pathSeparator: SLASH WS+;

absoluteForm: (SCHEME SCHEME_SEPARATOR)? (hostPort | ipAddress) (
    slashPathPart
    | queryPart
    | fragmentPart
    )*;

hostPort: host (COLON DIGITS)*;

host: segment (DOT segment)*;

ipAddress: ipv4Address | ipv6Address;

ipv4Address:
    DIGITS DOT DIGITS DOT DIGITS DOT DIGITS (COLON DIGITS)*;

ipv6Address:
    OPEN_BRAKET ipv6Literal CLOSE_BRAKET (COLON DIGITS)*;

ipv6Literal: fullIPv6 | compressedIPv6;

fullIPv6:
    hextet COLON hextet COLON hextet COLON hextet COLON hextet COLON hextet COLON hextet COLON
    hextet;

compressedIPv6: (hextet (COLON hextet)*)? COLON COLON (
    hextet (COLON hextet)*
    )?;

hextet: hexa+;

hexa: (DIGITS | ALPHA_CHARS)+;

slashPathPart: SLASH pathSegment? SLASH? (slashPathPart)?;

pathSegment: ((ALPHA_CHARS | DIGITS) (DOT ALPHA_CHARS | DIGITS)*);

asteriskForm: ASTERISK;

queryPart: QUESTION_MARK queryContent?;

queryContent:
    segment EQUAL (
    ALPHA_CHARS
    | DIGITS
    | DOT
    | SLASH
    | COLON
    | AMPERSAND
    | PERCENT
    | WS
    )+ queryContent?;

    fragmentPart: HASH fragmentContent;

fragmentContent: (
    ALPHA_CHARS
    | DIGITS
    | DOT
    | SLASH
    | COLON
    | PERCENT
    )+;

httpVersion:
    HTTP_PROTOCOL
    (SLASH | { notifyErrorListeners("Missing '/' in HTTP version"); })
    (versionNumber | { notifyErrorListeners("Invalid HTTP version number"); }); // HTTP/1.1

versionNumber: DIGITS (DOT DIGITS)?;

invalidContent: ~(WS | NEWLINE | REQUEST_SEPARATOR | METHOD)+;