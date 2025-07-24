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
lexer grammar HTTPLexer;

WS: [ \t];
NEWLINE: ('\r'? '\n');
REQUEST_SEPARATOR : '###' (~[\r\n])*;
COMMENT: ('#' | '//') (~[\r\n])*;

fragment OPTIONAL_WS: WS*;
fragment REQUIRED_WS: WS+;

METHOD
    : 'GET'
    | 'HEAD'
    | 'POST'
    | 'PUT'
    | 'DELETE'
    | 'CONNECT'
    | 'PATCH'
    | 'OPTIONS'
    | 'TRACE'
    ;

HTTP_PROTOCOL : 'HTTP';

COLON: ':';
DOT: '.';
SLASH: '/';
QUESTION_MARK: '?';
HASH: '#';
ASTERISK: '*';
EQUAL: '=';
AMPERSAND: '&';
PERCENT: '%';
OPEN_BRAKET: '[';
CLOSE_BRAKET: ']';
OPEN_BLOCK_BRAKET: '{';
CLOSE_BLOCK_BRAKET: '}';
QUOTE: '"';

fragment DIGIT: [0-9];
DIGITS: DIGIT+;

SCHEME
    : 'http'
    | 'https'
    | 'HTTPS'
    | 'ws'
    | 'WS'
    | 'wss'
    | 'WSS'
    | 'ftp'
    | 'FTP'
    | 'ftps'
    | 'FTPS'
    | 'sftp'
    | 'SFTP'
    | 'ldap'
    | 'LDAP'
    | 'ldaps'
    | 'LDAPS'
    | 'imap'
    | 'IMAP'
    | 'imaps'
    | 'IMAPS'
    | 'smtp'
    | 'SMTP'
    | 'smtps'
    | 'SMTPS'
    | 'pop3'
    | 'POP3'
    | 'pop3s'
    | 'POP3S'
    | 'file'
    | 'FILE'
    | 'telnet'
    | 'TELNET'
    | 'rtsp'
    | 'RTSP'
    | 'smb'
    | 'SMB'
    | 'nfs'
    | 'NFS'
    | 'git'
    | 'GIT'
    | 'mms'
    | 'MMS'
    | 'news'
    | 'NEWS'
    | 'urn'
    | 'URN'
    | 'data'
    | 'DATA'
    ;

fragment LINE_TAIL: (~[\r\n])* NEWLINE;

ALPHA_CHARS: [a-zA-Z]+;

DASH: '-';

UNDERSCORE: '_';

SCHEME_SEPARATOR : '://';

INPUT_FILE_REF : '<' REQUIRED_WS LINE_TAIL;

RESPONSE_HANDLER_SCRIPT_START : '>' REQUIRED_WS '{%';
RESPONSE_HANDLER_SCRIPT_END   : '%}';
RESPONSE_HANDLER_FILE_REF     : '>' REQUIRED_WS LINE_TAIL;

SCRIPT_CONTENT : . ;

// Environment variables
ENV_VARIABLE : '{{' OPTIONAL_WS (ALPHA_CHARS DIGITS)* OPTIONAL_WS '}}';

// Multipart-Form-Data
MULTIPART_BOUNDARY : '--'  (~[\r\n])* '-' '--';
MULTIPART_PART      : '--'  (~[\r\n])* '-';

// Ignore everything else
ERROR : .;
