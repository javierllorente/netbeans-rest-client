// Generated from HTTPParser.g4 by ANTLR 4.13.2
package com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class HTTPParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NEWLINE=2, REQUEST_SEPARATOR=3, COMMENT=4, METHOD=5, HTTP_PROTOCOL=6, 
		COLON=7, DOT=8, SLASH=9, QUESTION_MARK=10, HASH=11, ASTERISK=12, EQUAL=13, 
		AMPERSAND=14, PERCENT=15, OPEN_BRAKET=16, CLOSE_BRAKET=17, OPEN_BLOCK_BRAKET=18, 
		CLOSE_BLOCK_BRAKET=19, QUOTE=20, DIGITS=21, SCHEME=22, ALPHA_CHARS=23, 
		DASH=24, UNDERSCORE=25, SCHEME_SEPARATOR=26, INPUT_FILE_REF=27, RESPONSE_HANDLER_SCRIPT_START=28, 
		RESPONSE_HANDLER_SCRIPT_END=29, RESPONSE_HANDLER_FILE_REF=30, SCRIPT_CONTENT=31, 
		ENV_VARIABLE=32, MULTIPART_BOUNDARY=33, MULTIPART_PART=34, ERROR=35;
	public static final int
		RULE_httpRequestsFile = 0, RULE_firstRequest = 1, RULE_requestBlockWithSeparator = 2, 
		RULE_request = 3, RULE_requestHeaders = 4, RULE_requestBody = 5, RULE_header = 6, 
		RULE_headerField = 7, RULE_headerFieldName = 8, RULE_headerFieldValue = 9, 
		RULE_fieldName = 10, RULE_fieldValue = 11, RULE_blank = 12, RULE_separatorLine = 13, 
		RULE_requestLine = 14, RULE_requestTarget = 15, RULE_originForm = 16, 
		RULE_absolutePath = 17, RULE_segment = 18, RULE_pathSeparator = 19, RULE_absoluteForm = 20, 
		RULE_hostPort = 21, RULE_host = 22, RULE_ipAddress = 23, RULE_ipv4Address = 24, 
		RULE_ipv6Address = 25, RULE_ipv6Literal = 26, RULE_fullIPv6 = 27, RULE_compressedIPv6 = 28, 
		RULE_hextet = 29, RULE_hexa = 30, RULE_slashPathPart = 31, RULE_pathSegment = 32, 
		RULE_asteriskForm = 33, RULE_queryPart = 34, RULE_queryContent = 35, RULE_fragmentPart = 36, 
		RULE_fragmentContent = 37, RULE_httpVersion = 38, RULE_versionNumber = 39, 
		RULE_invalidContent = 40;
	private static String[] makeRuleNames() {
		return new String[] {
			"httpRequestsFile", "firstRequest", "requestBlockWithSeparator", "request", 
			"requestHeaders", "requestBody", "header", "headerField", "headerFieldName", 
			"headerFieldValue", "fieldName", "fieldValue", "blank", "separatorLine", 
			"requestLine", "requestTarget", "originForm", "absolutePath", "segment", 
			"pathSeparator", "absoluteForm", "hostPort", "host", "ipAddress", "ipv4Address", 
			"ipv6Address", "ipv6Literal", "fullIPv6", "compressedIPv6", "hextet", 
			"hexa", "slashPathPart", "pathSegment", "asteriskForm", "queryPart", 
			"queryContent", "fragmentPart", "fragmentContent", "httpVersion", "versionNumber", 
			"invalidContent"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'HTTP'", "':'", "'.'", "'/'", "'?'", 
			"'#'", "'*'", "'='", "'&'", "'%'", "'['", "']'", "'{'", "'}'", "'\"'", 
			null, null, null, "'-'", "'_'", "'://'", null, null, "'%}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "NEWLINE", "REQUEST_SEPARATOR", "COMMENT", "METHOD", "HTTP_PROTOCOL", 
			"COLON", "DOT", "SLASH", "QUESTION_MARK", "HASH", "ASTERISK", "EQUAL", 
			"AMPERSAND", "PERCENT", "OPEN_BRAKET", "CLOSE_BRAKET", "OPEN_BLOCK_BRAKET", 
			"CLOSE_BLOCK_BRAKET", "QUOTE", "DIGITS", "SCHEME", "ALPHA_CHARS", "DASH", 
			"UNDERSCORE", "SCHEME_SEPARATOR", "INPUT_FILE_REF", "RESPONSE_HANDLER_SCRIPT_START", 
			"RESPONSE_HANDLER_SCRIPT_END", "RESPONSE_HANDLER_FILE_REF", "SCRIPT_CONTENT", 
			"ENV_VARIABLE", "MULTIPART_BOUNDARY", "MULTIPART_PART", "ERROR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "HTTPParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public HTTPParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HttpRequestsFileContext extends ParserRuleContext {
		public FirstRequestContext firstRequest() {
			return getRuleContext(FirstRequestContext.class,0);
		}
		public TerminalNode EOF() { return getToken(HTTPParser.EOF, 0); }
		public List<BlankContext> blank() {
			return getRuleContexts(BlankContext.class);
		}
		public BlankContext blank(int i) {
			return getRuleContext(BlankContext.class,i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(HTTPParser.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(HTTPParser.COMMENT, i);
		}
		public List<RequestBlockWithSeparatorContext> requestBlockWithSeparator() {
			return getRuleContexts(RequestBlockWithSeparatorContext.class);
		}
		public RequestBlockWithSeparatorContext requestBlockWithSeparator(int i) {
			return getRuleContext(RequestBlockWithSeparatorContext.class,i);
		}
		public HttpRequestsFileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_httpRequestsFile; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHttpRequestsFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHttpRequestsFile(this);
		}
	}

	public final HttpRequestsFileContext httpRequestsFile() throws RecognitionException {
		HttpRequestsFileContext _localctx = new HttpRequestsFileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_httpRequestsFile);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(84);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case WS:
					case NEWLINE:
						{
						setState(82);
						blank();
						}
						break;
					case COMMENT:
						{
						setState(83);
						match(COMMENT);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(88);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(89);
			firstRequest();
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==REQUEST_SEPARATOR) {
				{
				{
				setState(90);
				requestBlockWithSeparator();
				}
				}
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(96);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FirstRequestContext extends ParserRuleContext {
		public RequestContext request() {
			return getRuleContext(RequestContext.class,0);
		}
		public SeparatorLineContext separatorLine() {
			return getRuleContext(SeparatorLineContext.class,0);
		}
		public List<BlankContext> blank() {
			return getRuleContexts(BlankContext.class);
		}
		public BlankContext blank(int i) {
			return getRuleContext(BlankContext.class,i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(HTTPParser.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(HTTPParser.COMMENT, i);
		}
		public FirstRequestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_firstRequest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterFirstRequest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitFirstRequest(this);
		}
	}

	public final FirstRequestContext firstRequest() throws RecognitionException {
		FirstRequestContext _localctx = new FirstRequestContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_firstRequest);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(98);
				separatorLine();
				}
				break;
			}
			setState(105);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(103);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case WS:
					case NEWLINE:
						{
						setState(101);
						blank();
						}
						break;
					case COMMENT:
						{
						setState(102);
						match(COMMENT);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(108);
			request();
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 22L) != 0)) {
				{
				setState(111);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case WS:
				case NEWLINE:
					{
					setState(109);
					blank();
					}
					break;
				case COMMENT:
					{
					setState(110);
					match(COMMENT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RequestBlockWithSeparatorContext extends ParserRuleContext {
		public SeparatorLineContext separatorLine() {
			return getRuleContext(SeparatorLineContext.class,0);
		}
		public RequestContext request() {
			return getRuleContext(RequestContext.class,0);
		}
		public List<BlankContext> blank() {
			return getRuleContexts(BlankContext.class);
		}
		public BlankContext blank(int i) {
			return getRuleContext(BlankContext.class,i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(HTTPParser.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(HTTPParser.COMMENT, i);
		}
		public RequestBlockWithSeparatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_requestBlockWithSeparator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterRequestBlockWithSeparator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitRequestBlockWithSeparator(this);
		}
	}

	public final RequestBlockWithSeparatorContext requestBlockWithSeparator() throws RecognitionException {
		RequestBlockWithSeparatorContext _localctx = new RequestBlockWithSeparatorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_requestBlockWithSeparator);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			separatorLine();
			setState(121);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(119);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case WS:
					case NEWLINE:
						{
						setState(117);
						blank();
						}
						break;
					case COMMENT:
						{
						setState(118);
						match(COMMENT);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(123);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			setState(124);
			request();
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 22L) != 0)) {
				{
				setState(127);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case WS:
				case NEWLINE:
					{
					setState(125);
					blank();
					}
					break;
				case COMMENT:
					{
					setState(126);
					match(COMMENT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(131);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RequestContext extends ParserRuleContext {
		public RequestLineContext requestLine() {
			return getRuleContext(RequestLineContext.class,0);
		}
		public RequestHeadersContext requestHeaders() {
			return getRuleContext(RequestHeadersContext.class,0);
		}
		public RequestBodyContext requestBody() {
			return getRuleContext(RequestBodyContext.class,0);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(HTTPParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(HTTPParser.NEWLINE, i);
		}
		public RequestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_request; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterRequest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitRequest(this);
		}
	}

	public final RequestContext request() throws RecognitionException {
		RequestContext _localctx = new RequestContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_request);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			requestLine();
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
				{
				setState(133);
				requestHeaders();
				}
			}

			setState(144);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EOF:
			case WS:
			case NEWLINE:
			case REQUEST_SEPARATOR:
			case COMMENT:
				{
				setState(139);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(136);
					match(NEWLINE);
					setState(137);
					match(NEWLINE);
					setState(138);
					requestBody();
					}
					break;
				}
				}
				break;
			case OPEN_BLOCK_BRAKET:
				{
				{
				setState(141);
				requestBody();
				}
				 notifyErrorListeners("Unknown header"); _errHandler.recover(this, new
				    InputMismatchException(this)); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RequestHeadersContext extends ParserRuleContext {
		public List<HeaderContext> header() {
			return getRuleContexts(HeaderContext.class);
		}
		public HeaderContext header(int i) {
			return getRuleContext(HeaderContext.class,i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(HTTPParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(HTTPParser.NEWLINE, i);
		}
		public RequestHeadersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_requestHeaders; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterRequestHeaders(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitRequestHeaders(this);
		}
	}

	public final RequestHeadersContext requestHeaders() throws RecognitionException {
		RequestHeadersContext _localctx = new RequestHeadersContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_requestHeaders);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(150); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(146);
					header();
					setState(148);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						setState(147);
						match(NEWLINE);
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(152); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
				{
				setState(154);
				header();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RequestBodyContext extends ParserRuleContext {
		public TerminalNode OPEN_BLOCK_BRAKET() { return getToken(HTTPParser.OPEN_BLOCK_BRAKET, 0); }
		public TerminalNode CLOSE_BLOCK_BRAKET() { return getToken(HTTPParser.CLOSE_BLOCK_BRAKET, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(HTTPParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(HTTPParser.NEWLINE, i);
		}
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public FieldNameContext fieldName() {
			return getRuleContext(FieldNameContext.class,0);
		}
		public TerminalNode COLON() { return getToken(HTTPParser.COLON, 0); }
		public FieldValueContext fieldValue() {
			return getRuleContext(FieldValueContext.class,0);
		}
		public RequestBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_requestBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterRequestBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitRequestBody(this);
		}
	}

	public final RequestBodyContext requestBody() throws RecognitionException {
		RequestBodyContext _localctx = new RequestBodyContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_requestBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(157);
			match(OPEN_BLOCK_BRAKET);
			setState(161);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(158);
					match(NEWLINE);
					}
					} 
				}
				setState(163);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			setState(167);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(164);
					match(WS);
					}
					} 
				}
				setState(169);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QUOTE) {
				{
				setState(170);
				fieldName();
				setState(171);
				match(COLON);
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(172);
					match(WS);
					}
					}
					setState(177);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(178);
				fieldValue();
				}
			}

			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(182);
				match(NEWLINE);
				}
				}
				setState(187);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(188);
				match(WS);
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(194);
			match(CLOSE_BLOCK_BRAKET);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HeaderContext extends ParserRuleContext {
		public HeaderFieldContext headerField() {
			return getRuleContext(HeaderFieldContext.class,0);
		}
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHeader(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			headerField();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HeaderFieldContext extends ParserRuleContext {
		public HeaderFieldNameContext headerFieldName() {
			return getRuleContext(HeaderFieldNameContext.class,0);
		}
		public TerminalNode COLON() { return getToken(HTTPParser.COLON, 0); }
		public HeaderFieldValueContext headerFieldValue() {
			return getRuleContext(HeaderFieldValueContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public HeaderFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHeaderField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHeaderField(this);
		}
	}

	public final HeaderFieldContext headerField() throws RecognitionException {
		HeaderFieldContext _localctx = new HeaderFieldContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_headerField);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			headerFieldName();
			setState(199);
			match(COLON);
			setState(203);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(200);
					match(WS);
					}
					} 
				}
				setState(205);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			setState(206);
			headerFieldValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HeaderFieldNameContext extends ParserRuleContext {
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public List<TerminalNode> DASH() { return getTokens(HTTPParser.DASH); }
		public TerminalNode DASH(int i) {
			return getToken(HTTPParser.DASH, i);
		}
		public List<TerminalNode> UNDERSCORE() { return getTokens(HTTPParser.UNDERSCORE); }
		public TerminalNode UNDERSCORE(int i) {
			return getToken(HTTPParser.UNDERSCORE, i);
		}
		public HeaderFieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerFieldName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHeaderFieldName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHeaderFieldName(this);
		}
	}

	public final HeaderFieldNameContext headerFieldName() throws RecognitionException {
		HeaderFieldNameContext _localctx = new HeaderFieldNameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_headerFieldName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(208);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(211); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HeaderFieldValueContext extends ParserRuleContext {
		public List<TerminalNode> NEWLINE() { return getTokens(HTTPParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(HTTPParser.NEWLINE, i);
		}
		public HeaderFieldValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerFieldValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHeaderFieldValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHeaderFieldValue(this);
		}
	}

	public final HeaderFieldValueContext headerFieldValue() throws RecognitionException {
		HeaderFieldValueContext _localctx = new HeaderFieldValueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_headerFieldValue);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(213);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==NEWLINE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					} 
				}
				setState(218);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldNameContext extends ParserRuleContext {
		public List<TerminalNode> QUOTE() { return getTokens(HTTPParser.QUOTE); }
		public TerminalNode QUOTE(int i) {
			return getToken(HTTPParser.QUOTE, i);
		}
		public TerminalNode ALPHA_CHARS() { return getToken(HTTPParser.ALPHA_CHARS, 0); }
		public TerminalNode DASH() { return getToken(HTTPParser.DASH, 0); }
		public TerminalNode UNDERSCORE() { return getToken(HTTPParser.UNDERSCORE, 0); }
		public FieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterFieldName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitFieldName(this);
		}
	}

	public final FieldNameContext fieldName() throws RecognitionException {
		FieldNameContext _localctx = new FieldNameContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_fieldName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(QUOTE);
			setState(220);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(221);
			match(QUOTE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldValueContext extends ParserRuleContext {
		public List<TerminalNode> QUOTE() { return getTokens(HTTPParser.QUOTE); }
		public TerminalNode QUOTE(int i) {
			return getToken(HTTPParser.QUOTE, i);
		}
		public FieldValueContext fieldValue() {
			return getRuleContext(FieldValueContext.class,0);
		}
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public List<TerminalNode> DASH() { return getTokens(HTTPParser.DASH); }
		public TerminalNode DASH(int i) {
			return getToken(HTTPParser.DASH, i);
		}
		public List<TerminalNode> UNDERSCORE() { return getTokens(HTTPParser.UNDERSCORE); }
		public TerminalNode UNDERSCORE(int i) {
			return getToken(HTTPParser.UNDERSCORE, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(HTTPParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(HTTPParser.SLASH, i);
		}
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public FieldValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterFieldValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitFieldValue(this);
		}
	}

	public final FieldValueContext fieldValue() throws RecognitionException {
		FieldValueContext _localctx = new FieldValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_fieldValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUOTE) {
				{
				{
				setState(223);
				match(QUOTE);
				}
				}
				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(230); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(229);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 60817920L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(232); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 60817920L) != 0) );
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUOTE) {
				{
				{
				setState(234);
				match(QUOTE);
				}
				}
				setState(239);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(241); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(240);
					match(WS);
					}
					}
					setState(243); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==WS );
				setState(245);
				fieldValue();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlankContext extends ParserRuleContext {
		public TerminalNode NEWLINE() { return getToken(HTTPParser.NEWLINE, 0); }
		public TerminalNode WS() { return getToken(HTTPParser.WS, 0); }
		public BlankContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blank; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterBlank(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitBlank(this);
		}
	}

	public final BlankContext blank() throws RecognitionException {
		BlankContext _localctx = new BlankContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_blank);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			_la = _input.LA(1);
			if ( !(_la==WS || _la==NEWLINE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SeparatorLineContext extends ParserRuleContext {
		public TerminalNode REQUEST_SEPARATOR() { return getToken(HTTPParser.REQUEST_SEPARATOR, 0); }
		public TerminalNode NEWLINE() { return getToken(HTTPParser.NEWLINE, 0); }
		public SeparatorLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_separatorLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterSeparatorLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitSeparatorLine(this);
		}
	}

	public final SeparatorLineContext separatorLine() throws RecognitionException {
		SeparatorLineContext _localctx = new SeparatorLineContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_separatorLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250);
			match(REQUEST_SEPARATOR);
			setState(252);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(251);
				match(NEWLINE);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RequestLineContext extends ParserRuleContext {
		public RequestTargetContext requestTarget() {
			return getRuleContext(RequestTargetContext.class,0);
		}
		public TerminalNode METHOD() { return getToken(HTTPParser.METHOD, 0); }
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public HttpVersionContext httpVersion() {
			return getRuleContext(HttpVersionContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(HTTPParser.NEWLINE, 0); }
		public RequestLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_requestLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterRequestLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitRequestLine(this);
		}
	}

	public final RequestLineContext requestLine() throws RecognitionException {
		RequestLineContext _localctx = new RequestLineContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_requestLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==METHOD) {
				{
				setState(254);
				match(METHOD);
				setState(255);
				match(WS);
				}
			}

			setState(258);
			requestTarget();
			setState(261);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(259);
				match(WS);
				setState(260);
				httpVersion();
				}
				break;
			}
			setState(264);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(263);
				match(NEWLINE);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RequestTargetContext extends ParserRuleContext {
		public OriginFormContext originForm() {
			return getRuleContext(OriginFormContext.class,0);
		}
		public AbsoluteFormContext absoluteForm() {
			return getRuleContext(AbsoluteFormContext.class,0);
		}
		public AsteriskFormContext asteriskForm() {
			return getRuleContext(AsteriskFormContext.class,0);
		}
		public RequestTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_requestTarget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterRequestTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitRequestTarget(this);
		}
	}

	public final RequestTargetContext requestTarget() throws RecognitionException {
		RequestTargetContext _localctx = new RequestTargetContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_requestTarget);
		try {
			setState(269);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(266);
				originForm();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(267);
				absoluteForm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(268);
				asteriskForm();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OriginFormContext extends ParserRuleContext {
		public SlashPathPartContext slashPathPart() {
			return getRuleContext(SlashPathPartContext.class,0);
		}
		public List<QueryPartContext> queryPart() {
			return getRuleContexts(QueryPartContext.class);
		}
		public QueryPartContext queryPart(int i) {
			return getRuleContext(QueryPartContext.class,i);
		}
		public List<FragmentPartContext> fragmentPart() {
			return getRuleContexts(FragmentPartContext.class);
		}
		public FragmentPartContext fragmentPart(int i) {
			return getRuleContext(FragmentPartContext.class,i);
		}
		public OriginFormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_originForm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterOriginForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitOriginForm(this);
		}
	}

	public final OriginFormContext originForm() throws RecognitionException {
		OriginFormContext _localctx = new OriginFormContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_originForm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			slashPathPart();
			setState(276);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUESTION_MARK || _la==HASH) {
				{
				setState(274);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUESTION_MARK:
					{
					setState(272);
					queryPart();
					}
					break;
				case HASH:
					{
					setState(273);
					fragmentPart();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(278);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbsolutePathContext extends ParserRuleContext {
		public TerminalNode SLASH() { return getToken(HTTPParser.SLASH, 0); }
		public List<PathSeparatorContext> pathSeparator() {
			return getRuleContexts(PathSeparatorContext.class);
		}
		public PathSeparatorContext pathSeparator(int i) {
			return getRuleContext(PathSeparatorContext.class,i);
		}
		public List<SegmentContext> segment() {
			return getRuleContexts(SegmentContext.class);
		}
		public SegmentContext segment(int i) {
			return getRuleContext(SegmentContext.class,i);
		}
		public AbsolutePathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absolutePath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterAbsolutePath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitAbsolutePath(this);
		}
	}

	public final AbsolutePathContext absolutePath() throws RecognitionException {
		AbsolutePathContext _localctx = new AbsolutePathContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_absolutePath);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(SLASH);
			setState(283); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(280);
				pathSeparator();
				setState(281);
				segment();
				}
				}
				setState(285); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==SLASH );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SegmentContext extends ParserRuleContext {
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> DASH() { return getTokens(HTTPParser.DASH); }
		public TerminalNode DASH(int i) {
			return getToken(HTTPParser.DASH, i);
		}
		public List<TerminalNode> UNDERSCORE() { return getTokens(HTTPParser.UNDERSCORE); }
		public TerminalNode UNDERSCORE(int i) {
			return getToken(HTTPParser.UNDERSCORE, i);
		}
		public SegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_segment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitSegment(this);
		}
	}

	public final SegmentContext segment() throws RecognitionException {
		SegmentContext _localctx = new SegmentContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_segment);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(287);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 60817408L) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					} 
				}
				setState(292);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PathSeparatorContext extends ParserRuleContext {
		public TerminalNode SLASH() { return getToken(HTTPParser.SLASH, 0); }
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public PathSeparatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathSeparator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterPathSeparator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitPathSeparator(this);
		}
	}

	public final PathSeparatorContext pathSeparator() throws RecognitionException {
		PathSeparatorContext _localctx = new PathSeparatorContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_pathSeparator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			match(SLASH);
			setState(295); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(294);
				match(WS);
				}
				}
				setState(297); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbsoluteFormContext extends ParserRuleContext {
		public HostPortContext hostPort() {
			return getRuleContext(HostPortContext.class,0);
		}
		public IpAddressContext ipAddress() {
			return getRuleContext(IpAddressContext.class,0);
		}
		public TerminalNode SCHEME() { return getToken(HTTPParser.SCHEME, 0); }
		public TerminalNode SCHEME_SEPARATOR() { return getToken(HTTPParser.SCHEME_SEPARATOR, 0); }
		public List<SlashPathPartContext> slashPathPart() {
			return getRuleContexts(SlashPathPartContext.class);
		}
		public SlashPathPartContext slashPathPart(int i) {
			return getRuleContext(SlashPathPartContext.class,i);
		}
		public List<QueryPartContext> queryPart() {
			return getRuleContexts(QueryPartContext.class);
		}
		public QueryPartContext queryPart(int i) {
			return getRuleContext(QueryPartContext.class,i);
		}
		public List<FragmentPartContext> fragmentPart() {
			return getRuleContexts(FragmentPartContext.class);
		}
		public FragmentPartContext fragmentPart(int i) {
			return getRuleContext(FragmentPartContext.class,i);
		}
		public AbsoluteFormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absoluteForm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterAbsoluteForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitAbsoluteForm(this);
		}
	}

	public final AbsoluteFormContext absoluteForm() throws RecognitionException {
		AbsoluteFormContext _localctx = new AbsoluteFormContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_absoluteForm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SCHEME) {
				{
				setState(299);
				match(SCHEME);
				setState(300);
				match(SCHEME_SEPARATOR);
				}
			}

			setState(305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(303);
				hostPort();
				}
				break;
			case 2:
				{
				setState(304);
				ipAddress();
				}
				break;
			}
			setState(312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3584L) != 0)) {
				{
				setState(310);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SLASH:
					{
					setState(307);
					slashPathPart();
					}
					break;
				case QUESTION_MARK:
					{
					setState(308);
					queryPart();
					}
					break;
				case HASH:
					{
					setState(309);
					fragmentPart();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(314);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HostPortContext extends ParserRuleContext {
		public HostContext host() {
			return getRuleContext(HostContext.class,0);
		}
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public HostPortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hostPort; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHostPort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHostPort(this);
		}
	}

	public final HostPortContext hostPort() throws RecognitionException {
		HostPortContext _localctx = new HostPortContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_hostPort);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			host();
			setState(320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COLON) {
				{
				{
				setState(316);
				match(COLON);
				setState(317);
				match(DIGITS);
				}
				}
				setState(322);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HostContext extends ParserRuleContext {
		public List<SegmentContext> segment() {
			return getRuleContexts(SegmentContext.class);
		}
		public SegmentContext segment(int i) {
			return getRuleContext(SegmentContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(HTTPParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(HTTPParser.DOT, i);
		}
		public HostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_host; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHost(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHost(this);
		}
	}

	public final HostContext host() throws RecognitionException {
		HostContext _localctx = new HostContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_host);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			segment();
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(324);
				match(DOT);
				setState(325);
				segment();
				}
				}
				setState(330);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IpAddressContext extends ParserRuleContext {
		public Ipv4AddressContext ipv4Address() {
			return getRuleContext(Ipv4AddressContext.class,0);
		}
		public Ipv6AddressContext ipv6Address() {
			return getRuleContext(Ipv6AddressContext.class,0);
		}
		public IpAddressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ipAddress; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterIpAddress(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitIpAddress(this);
		}
	}

	public final IpAddressContext ipAddress() throws RecognitionException {
		IpAddressContext _localctx = new IpAddressContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_ipAddress);
		try {
			setState(333);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DIGITS:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				ipv4Address();
				}
				break;
			case OPEN_BRAKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(332);
				ipv6Address();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Ipv4AddressContext extends ParserRuleContext {
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> DOT() { return getTokens(HTTPParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(HTTPParser.DOT, i);
		}
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public Ipv4AddressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ipv4Address; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterIpv4Address(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitIpv4Address(this);
		}
	}

	public final Ipv4AddressContext ipv4Address() throws RecognitionException {
		Ipv4AddressContext _localctx = new Ipv4AddressContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_ipv4Address);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(DIGITS);
			setState(336);
			match(DOT);
			setState(337);
			match(DIGITS);
			setState(338);
			match(DOT);
			setState(339);
			match(DIGITS);
			setState(340);
			match(DOT);
			setState(341);
			match(DIGITS);
			setState(346);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COLON) {
				{
				{
				setState(342);
				match(COLON);
				setState(343);
				match(DIGITS);
				}
				}
				setState(348);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Ipv6AddressContext extends ParserRuleContext {
		public TerminalNode OPEN_BRAKET() { return getToken(HTTPParser.OPEN_BRAKET, 0); }
		public Ipv6LiteralContext ipv6Literal() {
			return getRuleContext(Ipv6LiteralContext.class,0);
		}
		public TerminalNode CLOSE_BRAKET() { return getToken(HTTPParser.CLOSE_BRAKET, 0); }
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public Ipv6AddressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ipv6Address; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterIpv6Address(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitIpv6Address(this);
		}
	}

	public final Ipv6AddressContext ipv6Address() throws RecognitionException {
		Ipv6AddressContext _localctx = new Ipv6AddressContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_ipv6Address);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			match(OPEN_BRAKET);
			setState(350);
			ipv6Literal();
			setState(351);
			match(CLOSE_BRAKET);
			setState(356);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COLON) {
				{
				{
				setState(352);
				match(COLON);
				setState(353);
				match(DIGITS);
				}
				}
				setState(358);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Ipv6LiteralContext extends ParserRuleContext {
		public FullIPv6Context fullIPv6() {
			return getRuleContext(FullIPv6Context.class,0);
		}
		public CompressedIPv6Context compressedIPv6() {
			return getRuleContext(CompressedIPv6Context.class,0);
		}
		public Ipv6LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ipv6Literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterIpv6Literal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitIpv6Literal(this);
		}
	}

	public final Ipv6LiteralContext ipv6Literal() throws RecognitionException {
		Ipv6LiteralContext _localctx = new Ipv6LiteralContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_ipv6Literal);
		try {
			setState(361);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(359);
				fullIPv6();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				compressedIPv6();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FullIPv6Context extends ParserRuleContext {
		public List<HextetContext> hextet() {
			return getRuleContexts(HextetContext.class);
		}
		public HextetContext hextet(int i) {
			return getRuleContext(HextetContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public FullIPv6Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullIPv6; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterFullIPv6(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitFullIPv6(this);
		}
	}

	public final FullIPv6Context fullIPv6() throws RecognitionException {
		FullIPv6Context _localctx = new FullIPv6Context(_ctx, getState());
		enterRule(_localctx, 54, RULE_fullIPv6);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			hextet();
			setState(364);
			match(COLON);
			setState(365);
			hextet();
			setState(366);
			match(COLON);
			setState(367);
			hextet();
			setState(368);
			match(COLON);
			setState(369);
			hextet();
			setState(370);
			match(COLON);
			setState(371);
			hextet();
			setState(372);
			match(COLON);
			setState(373);
			hextet();
			setState(374);
			match(COLON);
			setState(375);
			hextet();
			setState(376);
			match(COLON);
			setState(377);
			hextet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompressedIPv6Context extends ParserRuleContext {
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public List<HextetContext> hextet() {
			return getRuleContexts(HextetContext.class);
		}
		public HextetContext hextet(int i) {
			return getRuleContext(HextetContext.class,i);
		}
		public CompressedIPv6Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compressedIPv6; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterCompressedIPv6(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitCompressedIPv6(this);
		}
	}

	public final CompressedIPv6Context compressedIPv6() throws RecognitionException {
		CompressedIPv6Context _localctx = new CompressedIPv6Context(_ctx, getState());
		enterRule(_localctx, 56, RULE_compressedIPv6);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DIGITS || _la==ALPHA_CHARS) {
				{
				setState(379);
				hextet();
				setState(384);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(380);
						match(COLON);
						setState(381);
						hextet();
						}
						} 
					}
					setState(386);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
				}
				}
			}

			setState(389);
			match(COLON);
			setState(390);
			match(COLON);
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DIGITS || _la==ALPHA_CHARS) {
				{
				setState(391);
				hextet();
				setState(396);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COLON) {
					{
					{
					setState(392);
					match(COLON);
					setState(393);
					hextet();
					}
					}
					setState(398);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HextetContext extends ParserRuleContext {
		public List<HexaContext> hexa() {
			return getRuleContexts(HexaContext.class);
		}
		public HexaContext hexa(int i) {
			return getRuleContext(HexaContext.class,i);
		}
		public HextetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hextet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHextet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHextet(this);
		}
	}

	public final HextetContext hextet() throws RecognitionException {
		HextetContext _localctx = new HextetContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_hextet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(401);
				hexa();
				}
				}
				setState(404); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DIGITS || _la==ALPHA_CHARS );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HexaContext extends ParserRuleContext {
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public HexaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hexa; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHexa(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHexa(this);
		}
	}

	public final HexaContext hexa() throws RecognitionException {
		HexaContext _localctx = new HexaContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_hexa);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(407); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(406);
					_la = _input.LA(1);
					if ( !(_la==DIGITS || _la==ALPHA_CHARS) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(409); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SlashPathPartContext extends ParserRuleContext {
		public List<TerminalNode> SLASH() { return getTokens(HTTPParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(HTTPParser.SLASH, i);
		}
		public PathSegmentContext pathSegment() {
			return getRuleContext(PathSegmentContext.class,0);
		}
		public SlashPathPartContext slashPathPart() {
			return getRuleContext(SlashPathPartContext.class,0);
		}
		public SlashPathPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slashPathPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterSlashPathPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitSlashPathPart(this);
		}
	}

	public final SlashPathPartContext slashPathPart() throws RecognitionException {
		SlashPathPartContext _localctx = new SlashPathPartContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_slashPathPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(SLASH);
			setState(413);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(412);
				pathSegment();
				}
				break;
			}
			setState(416);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(415);
				match(SLASH);
				}
				break;
			}
			setState(419);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(418);
				slashPathPart();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PathSegmentContext extends ParserRuleContext {
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> DOT() { return getTokens(HTTPParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(HTTPParser.DOT, i);
		}
		public PathSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterPathSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitPathSegment(this);
		}
	}

	public final PathSegmentContext pathSegment() throws RecognitionException {
		PathSegmentContext _localctx = new PathSegmentContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_pathSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(421);
			_la = _input.LA(1);
			if ( !(_la==DIGITS || _la==ALPHA_CHARS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT || _la==DIGITS) {
				{
				setState(425);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DOT:
					{
					setState(422);
					match(DOT);
					setState(423);
					match(ALPHA_CHARS);
					}
					break;
				case DIGITS:
					{
					setState(424);
					match(DIGITS);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(429);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsteriskFormContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(HTTPParser.ASTERISK, 0); }
		public AsteriskFormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asteriskForm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterAsteriskForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitAsteriskForm(this);
		}
	}

	public final AsteriskFormContext asteriskForm() throws RecognitionException {
		AsteriskFormContext _localctx = new AsteriskFormContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_asteriskForm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(430);
			match(ASTERISK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryPartContext extends ParserRuleContext {
		public TerminalNode QUESTION_MARK() { return getToken(HTTPParser.QUESTION_MARK, 0); }
		public QueryContentContext queryContent() {
			return getRuleContext(QueryContentContext.class,0);
		}
		public QueryPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterQueryPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitQueryPart(this);
		}
	}

	public final QueryPartContext queryPart() throws RecognitionException {
		QueryPartContext _localctx = new QueryPartContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_queryPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			match(QUESTION_MARK);
			setState(434);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(433);
				queryContent();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryContentContext extends ParserRuleContext {
		public SegmentContext segment() {
			return getRuleContext(SegmentContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(HTTPParser.EQUAL, 0); }
		public QueryContentContext queryContent() {
			return getRuleContext(QueryContentContext.class,0);
		}
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> DOT() { return getTokens(HTTPParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(HTTPParser.DOT, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(HTTPParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(HTTPParser.SLASH, i);
		}
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public List<TerminalNode> AMPERSAND() { return getTokens(HTTPParser.AMPERSAND); }
		public TerminalNode AMPERSAND(int i) {
			return getToken(HTTPParser.AMPERSAND, i);
		}
		public List<TerminalNode> PERCENT() { return getTokens(HTTPParser.PERCENT); }
		public TerminalNode PERCENT(int i) {
			return getToken(HTTPParser.PERCENT, i);
		}
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public QueryContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterQueryContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitQueryContent(this);
		}
	}

	public final QueryContentContext queryContent() throws RecognitionException {
		QueryContentContext _localctx = new QueryContentContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_queryContent);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			segment();
			setState(437);
			match(EQUAL);
			setState(439); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(438);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 10535810L) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(441); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(444);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(443);
				queryContent();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FragmentPartContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(HTTPParser.HASH, 0); }
		public FragmentContentContext fragmentContent() {
			return getRuleContext(FragmentContentContext.class,0);
		}
		public FragmentPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fragmentPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterFragmentPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitFragmentPart(this);
		}
	}

	public final FragmentPartContext fragmentPart() throws RecognitionException {
		FragmentPartContext _localctx = new FragmentPartContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_fragmentPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(HASH);
			setState(447);
			fragmentContent();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FragmentContentContext extends ParserRuleContext {
		public List<TerminalNode> ALPHA_CHARS() { return getTokens(HTTPParser.ALPHA_CHARS); }
		public TerminalNode ALPHA_CHARS(int i) {
			return getToken(HTTPParser.ALPHA_CHARS, i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public List<TerminalNode> DOT() { return getTokens(HTTPParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(HTTPParser.DOT, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(HTTPParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(HTTPParser.SLASH, i);
		}
		public List<TerminalNode> COLON() { return getTokens(HTTPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTTPParser.COLON, i);
		}
		public List<TerminalNode> PERCENT() { return getTokens(HTTPParser.PERCENT); }
		public TerminalNode PERCENT(int i) {
			return getToken(HTTPParser.PERCENT, i);
		}
		public FragmentContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fragmentContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterFragmentContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitFragmentContent(this);
		}
	}

	public final FragmentContentContext fragmentContent() throws RecognitionException {
		FragmentContentContext _localctx = new FragmentContentContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_fragmentContent);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(450); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(449);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 10519424L) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(452); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HttpVersionContext extends ParserRuleContext {
		public TerminalNode HTTP_PROTOCOL() { return getToken(HTTPParser.HTTP_PROTOCOL, 0); }
		public TerminalNode SLASH() { return getToken(HTTPParser.SLASH, 0); }
		public VersionNumberContext versionNumber() {
			return getRuleContext(VersionNumberContext.class,0);
		}
		public HttpVersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_httpVersion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterHttpVersion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitHttpVersion(this);
		}
	}

	public final HttpVersionContext httpVersion() throws RecognitionException {
		HttpVersionContext _localctx = new HttpVersionContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_httpVersion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			match(HTTP_PROTOCOL);
			setState(455);
			match(SLASH);
			setState(456);
			versionNumber();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VersionNumberContext extends ParserRuleContext {
		public List<TerminalNode> DIGITS() { return getTokens(HTTPParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(HTTPParser.DIGITS, i);
		}
		public TerminalNode DOT() { return getToken(HTTPParser.DOT, 0); }
		public VersionNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_versionNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterVersionNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitVersionNumber(this);
		}
	}

	public final VersionNumberContext versionNumber() throws RecognitionException {
		VersionNumberContext _localctx = new VersionNumberContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_versionNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			match(DIGITS);
			setState(461);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(459);
				match(DOT);
				setState(460);
				match(DIGITS);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvalidContentContext extends ParserRuleContext {
		public List<TerminalNode> WS() { return getTokens(HTTPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(HTTPParser.WS, i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(HTTPParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(HTTPParser.NEWLINE, i);
		}
		public List<TerminalNode> REQUEST_SEPARATOR() { return getTokens(HTTPParser.REQUEST_SEPARATOR); }
		public TerminalNode REQUEST_SEPARATOR(int i) {
			return getToken(HTTPParser.REQUEST_SEPARATOR, i);
		}
		public List<TerminalNode> METHOD() { return getTokens(HTTPParser.METHOD); }
		public TerminalNode METHOD(int i) {
			return getToken(HTTPParser.METHOD, i);
		}
		public InvalidContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invalidContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).enterInvalidContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTTPParserListener ) ((HTTPParserListener)listener).exitInvalidContent(this);
		}
	}

	public final InvalidContentContext invalidContent() throws RecognitionException {
		InvalidContentContext _localctx = new InvalidContentContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_invalidContent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(463);
				_la = _input.LA(1);
				if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & 46L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(466); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 68719476688L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001#\u01d5\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0001\u0000\u0001\u0000\u0005\u0000U\b\u0000\n\u0000\f\u0000"+
		"X\t\u0000\u0001\u0000\u0001\u0000\u0005\u0000\\\b\u0000\n\u0000\f\u0000"+
		"_\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0003\u0001d\b\u0001\u0001"+
		"\u0001\u0001\u0001\u0005\u0001h\b\u0001\n\u0001\f\u0001k\t\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0005\u0001p\b\u0001\n\u0001\f\u0001s\t"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002x\b\u0002\n\u0002"+
		"\f\u0002{\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0080"+
		"\b\u0002\n\u0002\f\u0002\u0083\t\u0002\u0001\u0003\u0001\u0003\u0003\u0003"+
		"\u0087\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003\u008c\b"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003\u0091\b\u0003\u0001"+
		"\u0004\u0001\u0004\u0003\u0004\u0095\b\u0004\u0004\u0004\u0097\b\u0004"+
		"\u000b\u0004\f\u0004\u0098\u0001\u0004\u0003\u0004\u009c\b\u0004\u0001"+
		"\u0005\u0001\u0005\u0005\u0005\u00a0\b\u0005\n\u0005\f\u0005\u00a3\t\u0005"+
		"\u0001\u0005\u0005\u0005\u00a6\b\u0005\n\u0005\f\u0005\u00a9\t\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00ae\b\u0005\n\u0005\f\u0005"+
		"\u00b1\t\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00b5\b\u0005\u0001"+
		"\u0005\u0005\u0005\u00b8\b\u0005\n\u0005\f\u0005\u00bb\t\u0005\u0001\u0005"+
		"\u0005\u0005\u00be\b\u0005\n\u0005\f\u0005\u00c1\t\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0005"+
		"\u0007\u00ca\b\u0007\n\u0007\f\u0007\u00cd\t\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0004\b\u00d2\b\b\u000b\b\f\b\u00d3\u0001\t\u0005\t\u00d7\b\t"+
		"\n\t\f\t\u00da\t\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0005\u000b"+
		"\u00e1\b\u000b\n\u000b\f\u000b\u00e4\t\u000b\u0001\u000b\u0004\u000b\u00e7"+
		"\b\u000b\u000b\u000b\f\u000b\u00e8\u0001\u000b\u0005\u000b\u00ec\b\u000b"+
		"\n\u000b\f\u000b\u00ef\t\u000b\u0001\u000b\u0004\u000b\u00f2\b\u000b\u000b"+
		"\u000b\f\u000b\u00f3\u0001\u000b\u0003\u000b\u00f7\b\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0003\r\u00fd\b\r\u0001\u000e\u0001\u000e\u0003\u000e"+
		"\u0101\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0106\b"+
		"\u000e\u0001\u000e\u0003\u000e\u0109\b\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0003\u000f\u010e\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0005"+
		"\u0010\u0113\b\u0010\n\u0010\f\u0010\u0116\t\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0004\u0011\u011c\b\u0011\u000b\u0011\f\u0011"+
		"\u011d\u0001\u0012\u0005\u0012\u0121\b\u0012\n\u0012\f\u0012\u0124\t\u0012"+
		"\u0001\u0013\u0001\u0013\u0004\u0013\u0128\b\u0013\u000b\u0013\f\u0013"+
		"\u0129\u0001\u0014\u0001\u0014\u0003\u0014\u012e\b\u0014\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u0132\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005"+
		"\u0014\u0137\b\u0014\n\u0014\f\u0014\u013a\t\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0005\u0015\u013f\b\u0015\n\u0015\f\u0015\u0142\t\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u0147\b\u0016\n\u0016\f\u0016"+
		"\u014a\t\u0016\u0001\u0017\u0001\u0017\u0003\u0017\u014e\b\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u0159\b\u0018\n\u0018\f\u0018"+
		"\u015c\t\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0005\u0019\u0163\b\u0019\n\u0019\f\u0019\u0166\t\u0019\u0001\u001a\u0001"+
		"\u001a\u0003\u001a\u016a\b\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0005\u001c\u017f\b\u001c\n"+
		"\u001c\f\u001c\u0182\t\u001c\u0003\u001c\u0184\b\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0005\u001c\u018b\b\u001c\n"+
		"\u001c\f\u001c\u018e\t\u001c\u0003\u001c\u0190\b\u001c\u0001\u001d\u0004"+
		"\u001d\u0193\b\u001d\u000b\u001d\f\u001d\u0194\u0001\u001e\u0004\u001e"+
		"\u0198\b\u001e\u000b\u001e\f\u001e\u0199\u0001\u001f\u0001\u001f\u0003"+
		"\u001f\u019e\b\u001f\u0001\u001f\u0003\u001f\u01a1\b\u001f\u0001\u001f"+
		"\u0003\u001f\u01a4\b\u001f\u0001 \u0001 \u0001 \u0001 \u0005 \u01aa\b"+
		" \n \f \u01ad\t \u0001!\u0001!\u0001\"\u0001\"\u0003\"\u01b3\b\"\u0001"+
		"#\u0001#\u0001#\u0004#\u01b8\b#\u000b#\f#\u01b9\u0001#\u0003#\u01bd\b"+
		"#\u0001$\u0001$\u0001$\u0001%\u0004%\u01c3\b%\u000b%\f%\u01c4\u0001&\u0001"+
		"&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0003\'\u01ce\b\'\u0001(\u0004"+
		"(\u01d1\b(\u000b(\f(\u01d2\u0001(\u0000\u0000)\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,."+
		"02468:<>@BDFHJLNP\u0000\t\u0001\u0000\u0017\u0019\u0001\u0000\u0002\u0002"+
		"\u0003\u0000\t\t\u0015\u0015\u0017\u0019\u0001\u0000\u0001\u0002\u0002"+
		"\u0000\u0015\u0015\u0017\u0019\u0002\u0000\u0015\u0015\u0017\u0017\u0005"+
		"\u0000\u0001\u0001\u0007\t\u000e\u000f\u0015\u0015\u0017\u0017\u0004\u0000"+
		"\u0007\t\u000f\u000f\u0015\u0015\u0017\u0017\u0002\u0000\u0001\u0003\u0005"+
		"\u0005\u01f2\u0000V\u0001\u0000\u0000\u0000\u0002c\u0001\u0000\u0000\u0000"+
		"\u0004t\u0001\u0000\u0000\u0000\u0006\u0084\u0001\u0000\u0000\u0000\b"+
		"\u0096\u0001\u0000\u0000\u0000\n\u009d\u0001\u0000\u0000\u0000\f\u00c4"+
		"\u0001\u0000\u0000\u0000\u000e\u00c6\u0001\u0000\u0000\u0000\u0010\u00d1"+
		"\u0001\u0000\u0000\u0000\u0012\u00d8\u0001\u0000\u0000\u0000\u0014\u00db"+
		"\u0001\u0000\u0000\u0000\u0016\u00e2\u0001\u0000\u0000\u0000\u0018\u00f8"+
		"\u0001\u0000\u0000\u0000\u001a\u00fa\u0001\u0000\u0000\u0000\u001c\u0100"+
		"\u0001\u0000\u0000\u0000\u001e\u010d\u0001\u0000\u0000\u0000 \u010f\u0001"+
		"\u0000\u0000\u0000\"\u0117\u0001\u0000\u0000\u0000$\u0122\u0001\u0000"+
		"\u0000\u0000&\u0125\u0001\u0000\u0000\u0000(\u012d\u0001\u0000\u0000\u0000"+
		"*\u013b\u0001\u0000\u0000\u0000,\u0143\u0001\u0000\u0000\u0000.\u014d"+
		"\u0001\u0000\u0000\u00000\u014f\u0001\u0000\u0000\u00002\u015d\u0001\u0000"+
		"\u0000\u00004\u0169\u0001\u0000\u0000\u00006\u016b\u0001\u0000\u0000\u0000"+
		"8\u0183\u0001\u0000\u0000\u0000:\u0192\u0001\u0000\u0000\u0000<\u0197"+
		"\u0001\u0000\u0000\u0000>\u019b\u0001\u0000\u0000\u0000@\u01a5\u0001\u0000"+
		"\u0000\u0000B\u01ae\u0001\u0000\u0000\u0000D\u01b0\u0001\u0000\u0000\u0000"+
		"F\u01b4\u0001\u0000\u0000\u0000H\u01be\u0001\u0000\u0000\u0000J\u01c2"+
		"\u0001\u0000\u0000\u0000L\u01c6\u0001\u0000\u0000\u0000N\u01ca\u0001\u0000"+
		"\u0000\u0000P\u01d0\u0001\u0000\u0000\u0000RU\u0003\u0018\f\u0000SU\u0005"+
		"\u0004\u0000\u0000TR\u0001\u0000\u0000\u0000TS\u0001\u0000\u0000\u0000"+
		"UX\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000VW\u0001\u0000\u0000"+
		"\u0000WY\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000Y]\u0003\u0002"+
		"\u0001\u0000Z\\\u0003\u0004\u0002\u0000[Z\u0001\u0000\u0000\u0000\\_\u0001"+
		"\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000"+
		"^`\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000`a\u0005\u0000\u0000"+
		"\u0001a\u0001\u0001\u0000\u0000\u0000bd\u0003\u001a\r\u0000cb\u0001\u0000"+
		"\u0000\u0000cd\u0001\u0000\u0000\u0000di\u0001\u0000\u0000\u0000eh\u0003"+
		"\u0018\f\u0000fh\u0005\u0004\u0000\u0000ge\u0001\u0000\u0000\u0000gf\u0001"+
		"\u0000\u0000\u0000hk\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000"+
		"ij\u0001\u0000\u0000\u0000jl\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000"+
		"\u0000lq\u0003\u0006\u0003\u0000mp\u0003\u0018\f\u0000np\u0005\u0004\u0000"+
		"\u0000om\u0001\u0000\u0000\u0000on\u0001\u0000\u0000\u0000ps\u0001\u0000"+
		"\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000r\u0003"+
		"\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000ty\u0003\u001a\r\u0000"+
		"ux\u0003\u0018\f\u0000vx\u0005\u0004\u0000\u0000wu\u0001\u0000\u0000\u0000"+
		"wv\u0001\u0000\u0000\u0000x{\u0001\u0000\u0000\u0000yw\u0001\u0000\u0000"+
		"\u0000yz\u0001\u0000\u0000\u0000z|\u0001\u0000\u0000\u0000{y\u0001\u0000"+
		"\u0000\u0000|\u0081\u0003\u0006\u0003\u0000}\u0080\u0003\u0018\f\u0000"+
		"~\u0080\u0005\u0004\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u007f~"+
		"\u0001\u0000\u0000\u0000\u0080\u0083\u0001\u0000\u0000\u0000\u0081\u007f"+
		"\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0005"+
		"\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000\u0084\u0086"+
		"\u0003\u001c\u000e\u0000\u0085\u0087\u0003\b\u0004\u0000\u0086\u0085\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0090\u0001"+
		"\u0000\u0000\u0000\u0088\u0089\u0005\u0002\u0000\u0000\u0089\u008a\u0005"+
		"\u0002\u0000\u0000\u008a\u008c\u0003\n\u0005\u0000\u008b\u0088\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u0091\u0001\u0000"+
		"\u0000\u0000\u008d\u008e\u0003\n\u0005\u0000\u008e\u008f\u0006\u0003\uffff"+
		"\uffff\u0000\u008f\u0091\u0001\u0000\u0000\u0000\u0090\u008b\u0001\u0000"+
		"\u0000\u0000\u0090\u008d\u0001\u0000\u0000\u0000\u0091\u0007\u0001\u0000"+
		"\u0000\u0000\u0092\u0094\u0003\f\u0006\u0000\u0093\u0095\u0005\u0002\u0000"+
		"\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000"+
		"\u0000\u0095\u0097\u0001\u0000\u0000\u0000\u0096\u0092\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009b\u0001\u0000\u0000"+
		"\u0000\u009a\u009c\u0003\f\u0006\u0000\u009b\u009a\u0001\u0000\u0000\u0000"+
		"\u009b\u009c\u0001\u0000\u0000\u0000\u009c\t\u0001\u0000\u0000\u0000\u009d"+
		"\u00a1\u0005\u0012\u0000\u0000\u009e\u00a0\u0005\u0002\u0000\u0000\u009f"+
		"\u009e\u0001\u0000\u0000\u0000\u00a0\u00a3\u0001\u0000\u0000\u0000\u00a1"+
		"\u009f\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2"+
		"\u00a7\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4"+
		"\u00a6\u0005\u0001\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a6"+
		"\u00a9\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a8\u0001\u0000\u0000\u0000\u00a8\u00b4\u0001\u0000\u0000\u0000\u00a9"+
		"\u00a7\u0001\u0000\u0000\u0000\u00aa\u00ab\u0003\u0014\n\u0000\u00ab\u00af"+
		"\u0005\u0007\u0000\u0000\u00ac\u00ae\u0005\u0001\u0000\u0000\u00ad\u00ac"+
		"\u0001\u0000\u0000\u0000\u00ae\u00b1\u0001\u0000\u0000\u0000\u00af\u00ad"+
		"\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0\u00b2"+
		"\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000\u0000\u0000\u00b2\u00b3"+
		"\u0003\u0016\u000b\u0000\u00b3\u00b5\u0001\u0000\u0000\u0000\u00b4\u00aa"+
		"\u0001\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5\u00b9"+
		"\u0001\u0000\u0000\u0000\u00b6\u00b8\u0005\u0002\u0000\u0000\u00b7\u00b6"+
		"\u0001\u0000\u0000\u0000\u00b8\u00bb\u0001\u0000\u0000\u0000\u00b9\u00b7"+
		"\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u00bf"+
		"\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc\u00be"+
		"\u0005\u0001\u0000\u0000\u00bd\u00bc\u0001\u0000\u0000\u0000\u00be\u00c1"+
		"\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000\u00bf\u00c0"+
		"\u0001\u0000\u0000\u0000\u00c0\u00c2\u0001\u0000\u0000\u0000\u00c1\u00bf"+
		"\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005\u0013\u0000\u0000\u00c3\u000b"+
		"\u0001\u0000\u0000\u0000\u00c4\u00c5\u0003\u000e\u0007\u0000\u00c5\r\u0001"+
		"\u0000\u0000\u0000\u00c6\u00c7\u0003\u0010\b\u0000\u00c7\u00cb\u0005\u0007"+
		"\u0000\u0000\u00c8\u00ca\u0005\u0001\u0000\u0000\u00c9\u00c8\u0001\u0000"+
		"\u0000\u0000\u00ca\u00cd\u0001\u0000\u0000\u0000\u00cb\u00c9\u0001\u0000"+
		"\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc\u00ce\u0001\u0000"+
		"\u0000\u0000\u00cd\u00cb\u0001\u0000\u0000\u0000\u00ce\u00cf\u0003\u0012"+
		"\t\u0000\u00cf\u000f\u0001\u0000\u0000\u0000\u00d0\u00d2\u0007\u0000\u0000"+
		"\u0000\u00d1\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000"+
		"\u0000\u00d4\u0011\u0001\u0000\u0000\u0000\u00d5\u00d7\b\u0001\u0000\u0000"+
		"\u00d6\u00d5\u0001\u0000\u0000\u0000\u00d7\u00da\u0001\u0000\u0000\u0000"+
		"\u00d8\u00d6\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000"+
		"\u00d9\u0013\u0001\u0000\u0000\u0000\u00da\u00d8\u0001\u0000\u0000\u0000"+
		"\u00db\u00dc\u0005\u0014\u0000\u0000\u00dc\u00dd\u0007\u0000\u0000\u0000"+
		"\u00dd\u00de\u0005\u0014\u0000\u0000\u00de\u0015\u0001\u0000\u0000\u0000"+
		"\u00df\u00e1\u0005\u0014\u0000\u0000\u00e0\u00df\u0001\u0000\u0000\u0000"+
		"\u00e1\u00e4\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000\u0000"+
		"\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e5\u00e7\u0007\u0002\u0000\u0000"+
		"\u00e6\u00e5\u0001\u0000\u0000\u0000\u00e7\u00e8\u0001\u0000\u0000\u0000"+
		"\u00e8\u00e6\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000"+
		"\u00e9\u00ed\u0001\u0000\u0000\u0000\u00ea\u00ec\u0005\u0014\u0000\u0000"+
		"\u00eb\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ef\u0001\u0000\u0000\u0000"+
		"\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000"+
		"\u00ee\u00f6\u0001\u0000\u0000\u0000\u00ef\u00ed\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f2\u0005\u0001\u0000\u0000\u00f1\u00f0\u0001\u0000\u0000\u0000"+
		"\u00f2\u00f3\u0001\u0000\u0000\u0000\u00f3\u00f1\u0001\u0000\u0000\u0000"+
		"\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4\u00f5\u0001\u0000\u0000\u0000"+
		"\u00f5\u00f7\u0003\u0016\u000b\u0000\u00f6\u00f1\u0001\u0000\u0000\u0000"+
		"\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7\u0017\u0001\u0000\u0000\u0000"+
		"\u00f8\u00f9\u0007\u0003\u0000\u0000\u00f9\u0019\u0001\u0000\u0000\u0000"+
		"\u00fa\u00fc\u0005\u0003\u0000\u0000\u00fb\u00fd\u0005\u0002\u0000\u0000"+
		"\u00fc\u00fb\u0001\u0000\u0000\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000"+
		"\u00fd\u001b\u0001\u0000\u0000\u0000\u00fe\u00ff\u0005\u0005\u0000\u0000"+
		"\u00ff\u0101\u0005\u0001\u0000\u0000\u0100\u00fe\u0001\u0000\u0000\u0000"+
		"\u0100\u0101\u0001\u0000\u0000\u0000\u0101\u0102\u0001\u0000\u0000\u0000"+
		"\u0102\u0105\u0003\u001e\u000f\u0000\u0103\u0104\u0005\u0001\u0000\u0000"+
		"\u0104\u0106\u0003L&\u0000\u0105\u0103\u0001\u0000\u0000\u0000\u0105\u0106"+
		"\u0001\u0000\u0000\u0000\u0106\u0108\u0001\u0000\u0000\u0000\u0107\u0109"+
		"\u0005\u0002\u0000\u0000\u0108\u0107\u0001\u0000\u0000\u0000\u0108\u0109"+
		"\u0001\u0000\u0000\u0000\u0109\u001d\u0001\u0000\u0000\u0000\u010a\u010e"+
		"\u0003 \u0010\u0000\u010b\u010e\u0003(\u0014\u0000\u010c\u010e\u0003B"+
		"!\u0000\u010d\u010a\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000"+
		"\u0000\u010d\u010c\u0001\u0000\u0000\u0000\u010e\u001f\u0001\u0000\u0000"+
		"\u0000\u010f\u0114\u0003>\u001f\u0000\u0110\u0113\u0003D\"\u0000\u0111"+
		"\u0113\u0003H$\u0000\u0112\u0110\u0001\u0000\u0000\u0000\u0112\u0111\u0001"+
		"\u0000\u0000\u0000\u0113\u0116\u0001\u0000\u0000\u0000\u0114\u0112\u0001"+
		"\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115!\u0001\u0000"+
		"\u0000\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0117\u011b\u0005\t\u0000"+
		"\u0000\u0118\u0119\u0003&\u0013\u0000\u0119\u011a\u0003$\u0012\u0000\u011a"+
		"\u011c\u0001\u0000\u0000\u0000\u011b\u0118\u0001\u0000\u0000\u0000\u011c"+
		"\u011d\u0001\u0000\u0000\u0000\u011d\u011b\u0001\u0000\u0000\u0000\u011d"+
		"\u011e\u0001\u0000\u0000\u0000\u011e#\u0001\u0000\u0000\u0000\u011f\u0121"+
		"\u0007\u0004\u0000\u0000\u0120\u011f\u0001\u0000\u0000\u0000\u0121\u0124"+
		"\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000\u0000\u0000\u0122\u0123"+
		"\u0001\u0000\u0000\u0000\u0123%\u0001\u0000\u0000\u0000\u0124\u0122\u0001"+
		"\u0000\u0000\u0000\u0125\u0127\u0005\t\u0000\u0000\u0126\u0128\u0005\u0001"+
		"\u0000\u0000\u0127\u0126\u0001\u0000\u0000\u0000\u0128\u0129\u0001\u0000"+
		"\u0000\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000"+
		"\u0000\u0000\u012a\'\u0001\u0000\u0000\u0000\u012b\u012c\u0005\u0016\u0000"+
		"\u0000\u012c\u012e\u0005\u001a\u0000\u0000\u012d\u012b\u0001\u0000\u0000"+
		"\u0000\u012d\u012e\u0001\u0000\u0000\u0000\u012e\u0131\u0001\u0000\u0000"+
		"\u0000\u012f\u0132\u0003*\u0015\u0000\u0130\u0132\u0003.\u0017\u0000\u0131"+
		"\u012f\u0001\u0000\u0000\u0000\u0131\u0130\u0001\u0000\u0000\u0000\u0132"+
		"\u0138\u0001\u0000\u0000\u0000\u0133\u0137\u0003>\u001f\u0000\u0134\u0137"+
		"\u0003D\"\u0000\u0135\u0137\u0003H$\u0000\u0136\u0133\u0001\u0000\u0000"+
		"\u0000\u0136\u0134\u0001\u0000\u0000\u0000\u0136\u0135\u0001\u0000\u0000"+
		"\u0000\u0137\u013a\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000"+
		"\u0000\u0138\u0139\u0001\u0000\u0000\u0000\u0139)\u0001\u0000\u0000\u0000"+
		"\u013a\u0138\u0001\u0000\u0000\u0000\u013b\u0140\u0003,\u0016\u0000\u013c"+
		"\u013d\u0005\u0007\u0000\u0000\u013d\u013f\u0005\u0015\u0000\u0000\u013e"+
		"\u013c\u0001\u0000\u0000\u0000\u013f\u0142\u0001\u0000\u0000\u0000\u0140"+
		"\u013e\u0001\u0000\u0000\u0000\u0140\u0141\u0001\u0000\u0000\u0000\u0141"+
		"+\u0001\u0000\u0000\u0000\u0142\u0140\u0001\u0000\u0000\u0000\u0143\u0148"+
		"\u0003$\u0012\u0000\u0144\u0145\u0005\b\u0000\u0000\u0145\u0147\u0003"+
		"$\u0012\u0000\u0146\u0144\u0001\u0000\u0000\u0000\u0147\u014a\u0001\u0000"+
		"\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000\u0148\u0149\u0001\u0000"+
		"\u0000\u0000\u0149-\u0001\u0000\u0000\u0000\u014a\u0148\u0001\u0000\u0000"+
		"\u0000\u014b\u014e\u00030\u0018\u0000\u014c\u014e\u00032\u0019\u0000\u014d"+
		"\u014b\u0001\u0000\u0000\u0000\u014d\u014c\u0001\u0000\u0000\u0000\u014e"+
		"/\u0001\u0000\u0000\u0000\u014f\u0150\u0005\u0015\u0000\u0000\u0150\u0151"+
		"\u0005\b\u0000\u0000\u0151\u0152\u0005\u0015\u0000\u0000\u0152\u0153\u0005"+
		"\b\u0000\u0000\u0153\u0154\u0005\u0015\u0000\u0000\u0154\u0155\u0005\b"+
		"\u0000\u0000\u0155\u015a\u0005\u0015\u0000\u0000\u0156\u0157\u0005\u0007"+
		"\u0000\u0000\u0157\u0159\u0005\u0015\u0000\u0000\u0158\u0156\u0001\u0000"+
		"\u0000\u0000\u0159\u015c\u0001\u0000\u0000\u0000\u015a\u0158\u0001\u0000"+
		"\u0000\u0000\u015a\u015b\u0001\u0000\u0000\u0000\u015b1\u0001\u0000\u0000"+
		"\u0000\u015c\u015a\u0001\u0000\u0000\u0000\u015d\u015e\u0005\u0010\u0000"+
		"\u0000\u015e\u015f\u00034\u001a\u0000\u015f\u0164\u0005\u0011\u0000\u0000"+
		"\u0160\u0161\u0005\u0007\u0000\u0000\u0161\u0163\u0005\u0015\u0000\u0000"+
		"\u0162\u0160\u0001\u0000\u0000\u0000\u0163\u0166\u0001\u0000\u0000\u0000"+
		"\u0164\u0162\u0001\u0000\u0000\u0000\u0164\u0165\u0001\u0000\u0000\u0000"+
		"\u01653\u0001\u0000\u0000\u0000\u0166\u0164\u0001\u0000\u0000\u0000\u0167"+
		"\u016a\u00036\u001b\u0000\u0168\u016a\u00038\u001c\u0000\u0169\u0167\u0001"+
		"\u0000\u0000\u0000\u0169\u0168\u0001\u0000\u0000\u0000\u016a5\u0001\u0000"+
		"\u0000\u0000\u016b\u016c\u0003:\u001d\u0000\u016c\u016d\u0005\u0007\u0000"+
		"\u0000\u016d\u016e\u0003:\u001d\u0000\u016e\u016f\u0005\u0007\u0000\u0000"+
		"\u016f\u0170\u0003:\u001d\u0000\u0170\u0171\u0005\u0007\u0000\u0000\u0171"+
		"\u0172\u0003:\u001d\u0000\u0172\u0173\u0005\u0007\u0000\u0000\u0173\u0174"+
		"\u0003:\u001d\u0000\u0174\u0175\u0005\u0007\u0000\u0000\u0175\u0176\u0003"+
		":\u001d\u0000\u0176\u0177\u0005\u0007\u0000\u0000\u0177\u0178\u0003:\u001d"+
		"\u0000\u0178\u0179\u0005\u0007\u0000\u0000\u0179\u017a\u0003:\u001d\u0000"+
		"\u017a7\u0001\u0000\u0000\u0000\u017b\u0180\u0003:\u001d\u0000\u017c\u017d"+
		"\u0005\u0007\u0000\u0000\u017d\u017f\u0003:\u001d\u0000\u017e\u017c\u0001"+
		"\u0000\u0000\u0000\u017f\u0182\u0001\u0000\u0000\u0000\u0180\u017e\u0001"+
		"\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000\u0000\u0181\u0184\u0001"+
		"\u0000\u0000\u0000\u0182\u0180\u0001\u0000\u0000\u0000\u0183\u017b\u0001"+
		"\u0000\u0000\u0000\u0183\u0184\u0001\u0000\u0000\u0000\u0184\u0185\u0001"+
		"\u0000\u0000\u0000\u0185\u0186\u0005\u0007\u0000\u0000\u0186\u018f\u0005"+
		"\u0007\u0000\u0000\u0187\u018c\u0003:\u001d\u0000\u0188\u0189\u0005\u0007"+
		"\u0000\u0000\u0189\u018b\u0003:\u001d\u0000\u018a\u0188\u0001\u0000\u0000"+
		"\u0000\u018b\u018e\u0001\u0000\u0000\u0000\u018c\u018a\u0001\u0000\u0000"+
		"\u0000\u018c\u018d\u0001\u0000\u0000\u0000\u018d\u0190\u0001\u0000\u0000"+
		"\u0000\u018e\u018c\u0001\u0000\u0000\u0000\u018f\u0187\u0001\u0000\u0000"+
		"\u0000\u018f\u0190\u0001\u0000\u0000\u0000\u01909\u0001\u0000\u0000\u0000"+
		"\u0191\u0193\u0003<\u001e\u0000\u0192\u0191\u0001\u0000\u0000\u0000\u0193"+
		"\u0194\u0001\u0000\u0000\u0000\u0194\u0192\u0001\u0000\u0000\u0000\u0194"+
		"\u0195\u0001\u0000\u0000\u0000\u0195;\u0001\u0000\u0000\u0000\u0196\u0198"+
		"\u0007\u0005\u0000\u0000\u0197\u0196\u0001\u0000\u0000\u0000\u0198\u0199"+
		"\u0001\u0000\u0000\u0000\u0199\u0197\u0001\u0000\u0000\u0000\u0199\u019a"+
		"\u0001\u0000\u0000\u0000\u019a=\u0001\u0000\u0000\u0000\u019b\u019d\u0005"+
		"\t\u0000\u0000\u019c\u019e\u0003@ \u0000\u019d\u019c\u0001\u0000\u0000"+
		"\u0000\u019d\u019e\u0001\u0000\u0000\u0000\u019e\u01a0\u0001\u0000\u0000"+
		"\u0000\u019f\u01a1\u0005\t\u0000\u0000\u01a0\u019f\u0001\u0000\u0000\u0000"+
		"\u01a0\u01a1\u0001\u0000\u0000\u0000\u01a1\u01a3\u0001\u0000\u0000\u0000"+
		"\u01a2\u01a4\u0003>\u001f\u0000\u01a3\u01a2\u0001\u0000\u0000\u0000\u01a3"+
		"\u01a4\u0001\u0000\u0000\u0000\u01a4?\u0001\u0000\u0000\u0000\u01a5\u01ab"+
		"\u0007\u0005\u0000\u0000\u01a6\u01a7\u0005\b\u0000\u0000\u01a7\u01aa\u0005"+
		"\u0017\u0000\u0000\u01a8\u01aa\u0005\u0015\u0000\u0000\u01a9\u01a6\u0001"+
		"\u0000\u0000\u0000\u01a9\u01a8\u0001\u0000\u0000\u0000\u01aa\u01ad\u0001"+
		"\u0000\u0000\u0000\u01ab\u01a9\u0001\u0000\u0000\u0000\u01ab\u01ac\u0001"+
		"\u0000\u0000\u0000\u01acA\u0001\u0000\u0000\u0000\u01ad\u01ab\u0001\u0000"+
		"\u0000\u0000\u01ae\u01af\u0005\f\u0000\u0000\u01afC\u0001\u0000\u0000"+
		"\u0000\u01b0\u01b2\u0005\n\u0000\u0000\u01b1\u01b3\u0003F#\u0000\u01b2"+
		"\u01b1\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3"+
		"E\u0001\u0000\u0000\u0000\u01b4\u01b5\u0003$\u0012\u0000\u01b5\u01b7\u0005"+
		"\r\u0000\u0000\u01b6\u01b8\u0007\u0006\u0000\u0000\u01b7\u01b6\u0001\u0000"+
		"\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9\u01b7\u0001\u0000"+
		"\u0000\u0000\u01b9\u01ba\u0001\u0000\u0000\u0000\u01ba\u01bc\u0001\u0000"+
		"\u0000\u0000\u01bb\u01bd\u0003F#\u0000\u01bc\u01bb\u0001\u0000\u0000\u0000"+
		"\u01bc\u01bd\u0001\u0000\u0000\u0000\u01bdG\u0001\u0000\u0000\u0000\u01be"+
		"\u01bf\u0005\u000b\u0000\u0000\u01bf\u01c0\u0003J%\u0000\u01c0I\u0001"+
		"\u0000\u0000\u0000\u01c1\u01c3\u0007\u0007\u0000\u0000\u01c2\u01c1\u0001"+
		"\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01c2\u0001"+
		"\u0000\u0000\u0000\u01c4\u01c5\u0001\u0000\u0000\u0000\u01c5K\u0001\u0000"+
		"\u0000\u0000\u01c6\u01c7\u0005\u0006\u0000\u0000\u01c7\u01c8\u0005\t\u0000"+
		"\u0000\u01c8\u01c9\u0003N\'\u0000\u01c9M\u0001\u0000\u0000\u0000\u01ca"+
		"\u01cd\u0005\u0015\u0000\u0000\u01cb\u01cc\u0005\b\u0000\u0000\u01cc\u01ce"+
		"\u0005\u0015\u0000\u0000\u01cd\u01cb\u0001\u0000\u0000\u0000\u01cd\u01ce"+
		"\u0001\u0000\u0000\u0000\u01ceO\u0001\u0000\u0000\u0000\u01cf\u01d1\b"+
		"\b\u0000\u0000\u01d0\u01cf\u0001\u0000\u0000\u0000\u01d1\u01d2\u0001\u0000"+
		"\u0000\u0000\u01d2\u01d0\u0001\u0000\u0000\u0000\u01d2\u01d3\u0001\u0000"+
		"\u0000\u0000\u01d3Q\u0001\u0000\u0000\u0000ETV]cgioqwy\u007f\u0081\u0086"+
		"\u008b\u0090\u0094\u0098\u009b\u00a1\u00a7\u00af\u00b4\u00b9\u00bf\u00cb"+
		"\u00d3\u00d8\u00e2\u00e8\u00ed\u00f3\u00f6\u00fc\u0100\u0105\u0108\u010d"+
		"\u0112\u0114\u011d\u0122\u0129\u012d\u0131\u0136\u0138\u0140\u0148\u014d"+
		"\u015a\u0164\u0169\u0180\u0183\u018c\u018f\u0194\u0199\u019d\u01a0\u01a3"+
		"\u01a9\u01ab\u01b2\u01b9\u01bc\u01c4\u01cd\u01d2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}