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

requestBlock: blank* (request | invalidBodyWithoutRequest);

request:
    requestLine requestHeaders? requestBodySection?
    | requestLineWithBody;

requestHeaders: (headerLine)+;

headerLine: 
    (WS* header (NEWLINE | {_input.LA(1) == BODY_START_WITH_BLANK || _input.LA(1) == GENERIC_BODY_START || _input.LA(1) == BODY_START_NO_BLANK || _input.LA(1) == EOF}?)
    | WS* invalidHeaderLine);

invalidBodyWithoutRequest:
    {
        notifyErrorListeners("Request body requires a request line");
    }
    (BODY_START_WITH_BLANK | BODY_START_NO_BLANK | GENERIC_BODY_START)
    .*?
    (REQUEST_SEPARATOR | EOF);

invalidHeaderLine:
    {_input.LA(2) != COLON && _input.LA(1) != COMMENT && _input.LA(1) != NEWLINE}?
    invalidHeaderContent
    {
        notifyErrorListeners("Unknown HTTP header");
    }
    NEWLINE?;

invalidHeaderContent: ~(NEWLINE | REQUEST_SEPARATOR | BODY_START_WITH_BLANK | BODY_START_NO_BLANK | GENERIC_BODY_START | COMMENT | EOF | WS)+;

requestBodySection:
    (WS | NEWLINE | COMMENT)* requestBody;

requestBody:
    bodyWithStarter
    | directBodyContent;

bodyWithStarter:
    (BODY_START_WITH_BLANK | BODY_START_NO_BLANK | GENERIC_BODY_START) bodyContent;

// Pure body content in BODY_CONTENT mode
bodyContent:
    (.)*? CLOSE_BLOCK_BRAKET  // Match anything until }
    | (.)* ;  // Match anything until end

// Direct body content for special cases (uppercase, etc.)
directBodyContent:
    (~(REQUEST_SEPARATOR | EOF))+ ;

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
headerFieldValue: (~(NEWLINE | BODY_START_WITH_BLANK | BODY_START_NO_BLANK | GENERIC_BODY_START))*;

fieldName: QUOTE (ALPHA_CHARS | DASH | UNDERSCORE) QUOTE;

fieldValue: QUOTE? (ALPHA_CHARS | DASH | UNDERSCORE | DIGITS | SLASH)+ QUOTE?;

blank: NEWLINE | WS;

requestLine:
    (METHOD WS+)? requestTarget (WS+ httpVersion)? WS* NEWLINE?;

requestLineWithBody:
    (METHOD WS+)? requestTarget (WS+ httpVersion)? BODY_START_WITH_BLANK bodyContent;

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

slashPathPart: SLASH (pathSegment (SLASH pathSegment)* SLASH?)?;

pathSegment: (ALPHA_CHARS | DIGITS) (DOT | DASH | UNDERSCORE | ALPHA_CHARS | DIGITS)*;

asteriskForm: ASTERISK;

queryPart: QUESTION_MARK queryContent?;

queryContent:
    queryParam (AMPERSAND queryParam)*;

queryParam:
    queryKey (EQUAL queryValue)?;

queryKey:
    (ALPHA_CHARS | DIGITS | DASH | UNDERSCORE | DOT | PERCENT)+;

queryValue:
    (ALPHA_CHARS
    | DIGITS
    | DOT
    | SLASH
    | COLON
    | DASH
    | UNDERSCORE
    | PERCENT
    | EQUAL
    | QUESTION_MARK
    | HASH
    | OPEN_BRAKET
    | CLOSE_BRAKET
    | SCHEME_SEPARATOR
    | SCHEME
    | COMMA
    )+;

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