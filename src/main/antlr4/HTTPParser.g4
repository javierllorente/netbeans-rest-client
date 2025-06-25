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

httpRequestsFile: (blank | COMMENT)* firstRequest requestBlockWithSeparator* EOF;

firstRequest:
    separatorLine? (blank | COMMENT)* request (blank | COMMENT)*;

requestBlockWithSeparator:
    separatorLine (blank | COMMENT)* request (blank | COMMENT)*;

request:
    requestLine requestHeaders? ((NEWLINE NEWLINE requestBody)?
	| (requestBody) { notifyErrorListeners("Unknown header"); _errHandler.recover(this, new
    InputMismatchException(this)); });

requestHeaders: (header NEWLINE?)+ header?;

requestBody: (
    OPEN_BLOCK_BRAKET NEWLINE* WS* (
    fieldName COLON WS* fieldValue
    )? NEWLINE* WS* CLOSE_BLOCK_BRAKET
    );

header: headerField;

headerField: headerFieldName COLON WS* headerFieldValue;

headerFieldName: (ALPHA_CHARS | DASH | UNDERSCORE)+;
headerFieldValue: (~NEWLINE)*;

fieldName: QUOTE (ALPHA_CHARS | DASH | UNDERSCORE) QUOTE;

fieldValue:
    QUOTE* (ALPHA_CHARS | DASH | UNDERSCORE | DIGITS | SLASH)+ QUOTE* (
    WS+ fieldValue
    )?;

blank: NEWLINE | WS;

separatorLine: REQUEST_SEPARATOR NEWLINE?;

requestLine: (METHOD WS)? requestTarget (WS httpVersion)? NEWLINE?;

requestTarget: originForm | absoluteForm | asteriskForm;

originForm: slashPathPart (queryPart | fragmentPart)*;

absolutePath: SLASH (pathSeparator segment)+;

segment: (ALPHA_CHARS | DIGITS | DASH | UNDERSCORE)*;

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

httpVersion: HTTP_PROTOCOL SLASH versionNumber; // HTTP/1.1

versionNumber: DIGITS (DOT DIGITS)?;

invalidContent: ~(WS | NEWLINE | REQUEST_SEPARATOR | METHOD)+;