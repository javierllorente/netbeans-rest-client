package com.javierllorente.netbeans.rest.client.http.editor.sidebar.request;

import com.javierllorente.netbeans.rest.client.RestClient;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.StyledDocument;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.openide.text.NbDocument;

/**
 *
 * @author Christian Lenz
 */
public class RequestProcessor implements IRequestProcessor {

    public final List<Request> currentRequests;
    private StyledDocument document;
    private RestClient restClient;

    public RequestProcessor(StyledDocument document) {
        this.currentRequests = new ArrayList<>();
        this.document = document;

        this.restClient = new RestClient();
    }

    @Override
    public List<Request> getRequests() {
        return currentRequests;
    }

    @Override
    public void updateRequestsList(String text) {
        currentRequests.clear();

        HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HTTPParser parser = new HTTPParser(tokens);

        HTTPParser.HttpRequestsFileContext fileCtx = parser.httpRequestsFile();

        if (fileCtx == null) {
            return;
        }

        List<HTTPParser.RequestBlockContext> requestBlock = fileCtx.requestBlock();

        for (HTTPParser.RequestBlockContext requestBlockContext : requestBlock) {
            HTTPParser.RequestContext request = requestBlockContext.request();

            if (request == null) {
                return;
            }

            HTTPParser.RequestLineContext requestLine = request.requestLine();

            if (requestLine == null) {
                return;
            }

            currentRequests.add(new Request(request, requestLine.getText(), NbDocument.findLineNumber(document, requestLine.start.getStartIndex())));
        }
    }

    @Override
    public void callRequest(HTTPParser.RequestContext requestContext) {
        if (requestContext == null) {
            return;
        }

        HTTPParser.RequestLineContext requestLine = requestContext.requestLine();

        if (requestLine == null) {
            return;
        }

        this.restClient.setHeaders(getHeaders(requestContext.requestHeaders()));
        this.restClient.setBody(getBody(requestContext.requestBodySection()));

        var method = requestLine.METHOD().getText();

        if (method == null || method.isEmpty()) {
            method = "GET";
        }

        HTTPParser.RequestTargetContext requestTarget = requestLine.requestTarget();

        if (requestTarget == null) {
            return;
        }

        this.restClient.request(requestTarget.getText(), method);
    }

    @Override
    public void openRequestInUi(HTTPParser.RequestContext request) {
        // TODO: Open request in UI
    }

    private MultivaluedMap<String, String> getHeaders(HTTPParser.RequestHeadersContext requestHeadersContext) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

        if (requestHeadersContext == null) {
            return headers;
        }

        List<HTTPParser.HeaderLineContext> headerLine = requestHeadersContext.headerLine();

        for (HTTPParser.HeaderLineContext headerLineContext : headerLine) {
            HTTPParser.HeaderContext header = headerLineContext.header();

            if (header == null) {
                break;
            }

            HTTPParser.HeaderFieldContext headerField = header.headerField();

            if (headerField == null) {
                break;
            }

            HTTPParser.HeaderFieldNameContext headerFieldNameContext = headerField.headerFieldName();
            HTTPParser.HeaderFieldValueContext headerFieldValueContext = headerField.headerFieldValue();

            if (headerFieldNameContext == null || headerFieldValueContext == null) {
                break;
            }

            headers.add(headerFieldNameContext.getText(), headerFieldValueContext.getText());
        }

        return headers;
    }

    private String getBody(HTTPParser.RequestBodySectionContext requestBodySectionContext) {
        if (requestBodySectionContext == null) {
            return "";
        }

        HTTPParser.RequestBodyContext requestBody = requestBodySectionContext.requestBody();

        if (requestBody == null) {
            return "";
        }

        HTTPParser.JsonBodyContentContext jsonBodyContent = requestBody.jsonBodyContent();

        if (jsonBodyContent == null) {
            return "";
        }

        return jsonBodyContent.getText();
    }
}
