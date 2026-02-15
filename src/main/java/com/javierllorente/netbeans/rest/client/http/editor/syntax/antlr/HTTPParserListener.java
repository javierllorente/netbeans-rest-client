// Generated from HTTPParser.g4 by ANTLR 4.13.2
package com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HTTPParser}.
 */
public interface HTTPParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HTTPParser#httpRequestsFile}.
	 * @param ctx the parse tree
	 */
	void enterHttpRequestsFile(HTTPParser.HttpRequestsFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#httpRequestsFile}.
	 * @param ctx the parse tree
	 */
	void exitHttpRequestsFile(HTTPParser.HttpRequestsFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestBlock}.
	 * @param ctx the parse tree
	 */
	void enterRequestBlock(HTTPParser.RequestBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestBlock}.
	 * @param ctx the parse tree
	 */
	void exitRequestBlock(HTTPParser.RequestBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#request}.
	 * @param ctx the parse tree
	 */
	void enterRequest(HTTPParser.RequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#request}.
	 * @param ctx the parse tree
	 */
	void exitRequest(HTTPParser.RequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestHeaders}.
	 * @param ctx the parse tree
	 */
	void enterRequestHeaders(HTTPParser.RequestHeadersContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestHeaders}.
	 * @param ctx the parse tree
	 */
	void exitRequestHeaders(HTTPParser.RequestHeadersContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#headerLine}.
	 * @param ctx the parse tree
	 */
	void enterHeaderLine(HTTPParser.HeaderLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#headerLine}.
	 * @param ctx the parse tree
	 */
	void exitHeaderLine(HTTPParser.HeaderLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#invalidBodyWithoutRequest}.
	 * @param ctx the parse tree
	 */
	void enterInvalidBodyWithoutRequest(HTTPParser.InvalidBodyWithoutRequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#invalidBodyWithoutRequest}.
	 * @param ctx the parse tree
	 */
	void exitInvalidBodyWithoutRequest(HTTPParser.InvalidBodyWithoutRequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#invalidHeaderLine}.
	 * @param ctx the parse tree
	 */
	void enterInvalidHeaderLine(HTTPParser.InvalidHeaderLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#invalidHeaderLine}.
	 * @param ctx the parse tree
	 */
	void exitInvalidHeaderLine(HTTPParser.InvalidHeaderLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#invalidHeaderContent}.
	 * @param ctx the parse tree
	 */
	void enterInvalidHeaderContent(HTTPParser.InvalidHeaderContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#invalidHeaderContent}.
	 * @param ctx the parse tree
	 */
	void exitInvalidHeaderContent(HTTPParser.InvalidHeaderContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestBodySection}.
	 * @param ctx the parse tree
	 */
	void enterRequestBodySection(HTTPParser.RequestBodySectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestBodySection}.
	 * @param ctx the parse tree
	 */
	void exitRequestBodySection(HTTPParser.RequestBodySectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestBody}.
	 * @param ctx the parse tree
	 */
	void enterRequestBody(HTTPParser.RequestBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestBody}.
	 * @param ctx the parse tree
	 */
	void exitRequestBody(HTTPParser.RequestBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#bodyWithStarter}.
	 * @param ctx the parse tree
	 */
	void enterBodyWithStarter(HTTPParser.BodyWithStarterContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#bodyWithStarter}.
	 * @param ctx the parse tree
	 */
	void exitBodyWithStarter(HTTPParser.BodyWithStarterContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterBodyContent(HTTPParser.BodyContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitBodyContent(HTTPParser.BodyContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#directBodyContent}.
	 * @param ctx the parse tree
	 */
	void enterDirectBodyContent(HTTPParser.DirectBodyContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#directBodyContent}.
	 * @param ctx the parse tree
	 */
	void exitDirectBodyContent(HTTPParser.DirectBodyContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonObject}.
	 * @param ctx the parse tree
	 */
	void enterJsonObject(HTTPParser.JsonObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonObject}.
	 * @param ctx the parse tree
	 */
	void exitJsonObject(HTTPParser.JsonObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(HTTPParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(HTTPParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonArray}.
	 * @param ctx the parse tree
	 */
	void enterJsonArray(HTTPParser.JsonArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonArray}.
	 * @param ctx the parse tree
	 */
	void exitJsonArray(HTTPParser.JsonArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void enterJsonValue(HTTPParser.JsonValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void exitJsonValue(HTTPParser.JsonValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonString}.
	 * @param ctx the parse tree
	 */
	void enterJsonString(HTTPParser.JsonStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonString}.
	 * @param ctx the parse tree
	 */
	void exitJsonString(HTTPParser.JsonStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonStringContent}.
	 * @param ctx the parse tree
	 */
	void enterJsonStringContent(HTTPParser.JsonStringContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonStringContent}.
	 * @param ctx the parse tree
	 */
	void exitJsonStringContent(HTTPParser.JsonStringContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonStringChar}.
	 * @param ctx the parse tree
	 */
	void enterJsonStringChar(HTTPParser.JsonStringCharContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonStringChar}.
	 * @param ctx the parse tree
	 */
	void exitJsonStringChar(HTTPParser.JsonStringCharContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonNumber}.
	 * @param ctx the parse tree
	 */
	void enterJsonNumber(HTTPParser.JsonNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonNumber}.
	 * @param ctx the parse tree
	 */
	void exitJsonNumber(HTTPParser.JsonNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#jsonBareWord}.
	 * @param ctx the parse tree
	 */
	void enterJsonBareWord(HTTPParser.JsonBareWordContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#jsonBareWord}.
	 * @param ctx the parse tree
	 */
	void exitJsonBareWord(HTTPParser.JsonBareWordContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#space}.
	 * @param ctx the parse tree
	 */
	void enterSpace(HTTPParser.SpaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#space}.
	 * @param ctx the parse tree
	 */
	void exitSpace(HTTPParser.SpaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(HTTPParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(HTTPParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#headerField}.
	 * @param ctx the parse tree
	 */
	void enterHeaderField(HTTPParser.HeaderFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#headerField}.
	 * @param ctx the parse tree
	 */
	void exitHeaderField(HTTPParser.HeaderFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#headerFieldName}.
	 * @param ctx the parse tree
	 */
	void enterHeaderFieldName(HTTPParser.HeaderFieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#headerFieldName}.
	 * @param ctx the parse tree
	 */
	void exitHeaderFieldName(HTTPParser.HeaderFieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#headerFieldValue}.
	 * @param ctx the parse tree
	 */
	void enterHeaderFieldValue(HTTPParser.HeaderFieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#headerFieldValue}.
	 * @param ctx the parse tree
	 */
	void exitHeaderFieldValue(HTTPParser.HeaderFieldValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(HTTPParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(HTTPParser.FieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void enterFieldValue(HTTPParser.FieldValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#fieldValue}.
	 * @param ctx the parse tree
	 */
	void exitFieldValue(HTTPParser.FieldValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#blank}.
	 * @param ctx the parse tree
	 */
	void enterBlank(HTTPParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#blank}.
	 * @param ctx the parse tree
	 */
	void exitBlank(HTTPParser.BlankContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestLine}.
	 * @param ctx the parse tree
	 */
	void enterRequestLine(HTTPParser.RequestLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestLine}.
	 * @param ctx the parse tree
	 */
	void exitRequestLine(HTTPParser.RequestLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestLineWithBody}.
	 * @param ctx the parse tree
	 */
	void enterRequestLineWithBody(HTTPParser.RequestLineWithBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestLineWithBody}.
	 * @param ctx the parse tree
	 */
	void exitRequestLineWithBody(HTTPParser.RequestLineWithBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#requestTarget}.
	 * @param ctx the parse tree
	 */
	void enterRequestTarget(HTTPParser.RequestTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#requestTarget}.
	 * @param ctx the parse tree
	 */
	void exitRequestTarget(HTTPParser.RequestTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#originForm}.
	 * @param ctx the parse tree
	 */
	void enterOriginForm(HTTPParser.OriginFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#originForm}.
	 * @param ctx the parse tree
	 */
	void exitOriginForm(HTTPParser.OriginFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#absolutePath}.
	 * @param ctx the parse tree
	 */
	void enterAbsolutePath(HTTPParser.AbsolutePathContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#absolutePath}.
	 * @param ctx the parse tree
	 */
	void exitAbsolutePath(HTTPParser.AbsolutePathContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#segment}.
	 * @param ctx the parse tree
	 */
	void enterSegment(HTTPParser.SegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#segment}.
	 * @param ctx the parse tree
	 */
	void exitSegment(HTTPParser.SegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#pathSeparator}.
	 * @param ctx the parse tree
	 */
	void enterPathSeparator(HTTPParser.PathSeparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#pathSeparator}.
	 * @param ctx the parse tree
	 */
	void exitPathSeparator(HTTPParser.PathSeparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#absoluteForm}.
	 * @param ctx the parse tree
	 */
	void enterAbsoluteForm(HTTPParser.AbsoluteFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#absoluteForm}.
	 * @param ctx the parse tree
	 */
	void exitAbsoluteForm(HTTPParser.AbsoluteFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#hostPort}.
	 * @param ctx the parse tree
	 */
	void enterHostPort(HTTPParser.HostPortContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#hostPort}.
	 * @param ctx the parse tree
	 */
	void exitHostPort(HTTPParser.HostPortContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#host}.
	 * @param ctx the parse tree
	 */
	void enterHost(HTTPParser.HostContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#host}.
	 * @param ctx the parse tree
	 */
	void exitHost(HTTPParser.HostContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#ipAddress}.
	 * @param ctx the parse tree
	 */
	void enterIpAddress(HTTPParser.IpAddressContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#ipAddress}.
	 * @param ctx the parse tree
	 */
	void exitIpAddress(HTTPParser.IpAddressContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#ipv4Address}.
	 * @param ctx the parse tree
	 */
	void enterIpv4Address(HTTPParser.Ipv4AddressContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#ipv4Address}.
	 * @param ctx the parse tree
	 */
	void exitIpv4Address(HTTPParser.Ipv4AddressContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#ipv6Address}.
	 * @param ctx the parse tree
	 */
	void enterIpv6Address(HTTPParser.Ipv6AddressContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#ipv6Address}.
	 * @param ctx the parse tree
	 */
	void exitIpv6Address(HTTPParser.Ipv6AddressContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#ipv6Literal}.
	 * @param ctx the parse tree
	 */
	void enterIpv6Literal(HTTPParser.Ipv6LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#ipv6Literal}.
	 * @param ctx the parse tree
	 */
	void exitIpv6Literal(HTTPParser.Ipv6LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#fullIPv6}.
	 * @param ctx the parse tree
	 */
	void enterFullIPv6(HTTPParser.FullIPv6Context ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#fullIPv6}.
	 * @param ctx the parse tree
	 */
	void exitFullIPv6(HTTPParser.FullIPv6Context ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#compressedIPv6}.
	 * @param ctx the parse tree
	 */
	void enterCompressedIPv6(HTTPParser.CompressedIPv6Context ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#compressedIPv6}.
	 * @param ctx the parse tree
	 */
	void exitCompressedIPv6(HTTPParser.CompressedIPv6Context ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#hextet}.
	 * @param ctx the parse tree
	 */
	void enterHextet(HTTPParser.HextetContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#hextet}.
	 * @param ctx the parse tree
	 */
	void exitHextet(HTTPParser.HextetContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#hexa}.
	 * @param ctx the parse tree
	 */
	void enterHexa(HTTPParser.HexaContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#hexa}.
	 * @param ctx the parse tree
	 */
	void exitHexa(HTTPParser.HexaContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#slashPathPart}.
	 * @param ctx the parse tree
	 */
	void enterSlashPathPart(HTTPParser.SlashPathPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#slashPathPart}.
	 * @param ctx the parse tree
	 */
	void exitSlashPathPart(HTTPParser.SlashPathPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#pathSegment}.
	 * @param ctx the parse tree
	 */
	void enterPathSegment(HTTPParser.PathSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#pathSegment}.
	 * @param ctx the parse tree
	 */
	void exitPathSegment(HTTPParser.PathSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#asteriskForm}.
	 * @param ctx the parse tree
	 */
	void enterAsteriskForm(HTTPParser.AsteriskFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#asteriskForm}.
	 * @param ctx the parse tree
	 */
	void exitAsteriskForm(HTTPParser.AsteriskFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#queryPart}.
	 * @param ctx the parse tree
	 */
	void enterQueryPart(HTTPParser.QueryPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#queryPart}.
	 * @param ctx the parse tree
	 */
	void exitQueryPart(HTTPParser.QueryPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#queryContent}.
	 * @param ctx the parse tree
	 */
	void enterQueryContent(HTTPParser.QueryContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#queryContent}.
	 * @param ctx the parse tree
	 */
	void exitQueryContent(HTTPParser.QueryContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#queryParam}.
	 * @param ctx the parse tree
	 */
	void enterQueryParam(HTTPParser.QueryParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#queryParam}.
	 * @param ctx the parse tree
	 */
	void exitQueryParam(HTTPParser.QueryParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#queryKey}.
	 * @param ctx the parse tree
	 */
	void enterQueryKey(HTTPParser.QueryKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#queryKey}.
	 * @param ctx the parse tree
	 */
	void exitQueryKey(HTTPParser.QueryKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#queryValue}.
	 * @param ctx the parse tree
	 */
	void enterQueryValue(HTTPParser.QueryValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#queryValue}.
	 * @param ctx the parse tree
	 */
	void exitQueryValue(HTTPParser.QueryValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#fragmentPart}.
	 * @param ctx the parse tree
	 */
	void enterFragmentPart(HTTPParser.FragmentPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#fragmentPart}.
	 * @param ctx the parse tree
	 */
	void exitFragmentPart(HTTPParser.FragmentPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#fragmentContent}.
	 * @param ctx the parse tree
	 */
	void enterFragmentContent(HTTPParser.FragmentContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#fragmentContent}.
	 * @param ctx the parse tree
	 */
	void exitFragmentContent(HTTPParser.FragmentContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#httpVersion}.
	 * @param ctx the parse tree
	 */
	void enterHttpVersion(HTTPParser.HttpVersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#httpVersion}.
	 * @param ctx the parse tree
	 */
	void exitHttpVersion(HTTPParser.HttpVersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#versionNumber}.
	 * @param ctx the parse tree
	 */
	void enterVersionNumber(HTTPParser.VersionNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#versionNumber}.
	 * @param ctx the parse tree
	 */
	void exitVersionNumber(HTTPParser.VersionNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTTPParser#invalidContent}.
	 * @param ctx the parse tree
	 */
	void enterInvalidContent(HTTPParser.InvalidContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTTPParser#invalidContent}.
	 * @param ctx the parse tree
	 */
	void exitInvalidContent(HTTPParser.InvalidContentContext ctx);
}
