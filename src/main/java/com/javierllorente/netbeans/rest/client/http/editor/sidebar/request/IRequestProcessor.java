package com.javierllorente.netbeans.rest.client.http.editor.sidebar.request;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import java.util.List;

/**
 *
 * @author Christian Lenz
 */
public interface IRequestProcessor {

    List<Request> getRequests();

    void updateRequestsList(String text);

    void callRequest(HTTPParser.RequestContext requestContext);

    void openRequestInUi(HTTPParser.RequestContext requestContext);
}
