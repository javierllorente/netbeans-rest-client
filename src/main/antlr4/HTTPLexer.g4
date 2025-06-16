lexer grammar HTTPLexer;

WS: [ \t];
NEWLINE: ('\r'? '\n');
// NEWLINE_WITH_INDENT: NEWLINE REQUIRED_WS;
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
    //    | 'HTTP' TODO: using also for scheme not only for http version -> Parser
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

//IDENTIFIER_CHARACTER: ALPHA_CHARS DIGIT '_' '-';
//
//IDENTIFIER: IDENTIFIER_CHARACTER+;

//INPUT_CHARACTER: '\u0000'..'\uFFFE';

//INPUT_CHARACTER : ~[\r\n];

SCHEME_SEPARATOR : '://';

//REQUEST_SEPARATOR : '###' LINE_TAIL;

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
//ERROR : . -> skip;
ERROR : .;
