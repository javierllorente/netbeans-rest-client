package com.javierllorente.netbeans.rest.client.http.editor.syntax;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParserBaseListener;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.Severity;
import org.netbeans.modules.csl.spi.DefaultError;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.openide.filesystems.FileObject;

public class HTTPLangParserResult extends ParserResult {

    private final List<DefaultError> errors = new ArrayList<>();
    private boolean finished = false;

    // Store all requestLine contexts found in the file
    private final List<HTTPParser.RequestLineContext> requestLineContexts = new ArrayList<>();

    public HTTPLangParserResult(Snapshot snapshot) {
        super(snapshot);
    }

    public HTTPLangParserResult parse() {
        if (!finished) {
            String text = getSnapshot().getText().toString();

            HTTPLexer lexer = new HTTPLexer(CharStreams.fromString(text));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            HTTPParser parser = new HTTPParser(tokenStream);

            parser.removeErrorListeners();
            parser.addErrorListener(createErrorListener());
            parser.addParseListener(createFoldListener());

            HTTPParser.HttpRequestsFileContext fileCtx = parser.httpRequestsFile();

//            
//            for (ParseTree child : fileCtx.children) {
//                System.out.println("Count: " + child.getChildCount());
//            }
            if (fileCtx != null) {
                System.out.println("Reqeustsfile: " + fileCtx.getText());
//                HTTPParser.RequestContext req = fileCtx.request();
//                requestLineContexts.add(req.requestLine());

                // Iterate over all request nodes in the file
                System.out.println("fileCtx: " + fileCtx.requestBlockWithSeparator().size());
                for (HTTPParser.RequestBlockWithSeparatorContext requestBlockWithSeparatorContext : fileCtx.requestBlockWithSeparator()) {
                    System.out.println("RequestWithSeparator: " + requestBlockWithSeparatorContext.getText());
//
                    ////                    HTTPLexer httpLexer = new HTTPLexer(CharStreams.fromString(req.getText()));
////
////                    CommonTokenStream tokens = new CommonTokenStream(httpLexer);
////                    tokens.fill();
////
////                    for (Token t : tokens.getTokens()) {
////                        System.out.println(t.getText() + " -> " + lexer.getVocabulary().getSymbolicName(t.getType()));
////                    }
                    HTTPParser.RequestContext req = requestBlockWithSeparatorContext.request();

                    if (req.requestLine() != null) {
                        requestLineContexts.add(req.requestLine());
                    }
                }

                HTTPParser.FirstRequestContext firstRequest = fileCtx.firstRequest();

                if (firstRequest != null) {
                    HTTPParser.RequestContext request = firstRequest.request();
                    if (request != null) {
                        requestLineContexts.add(0, request.requestLine());
                    }
                }

            }

            // Debug: print all request lines
//            for (HTTPParser.RequestLineContext reqLine : requestLineContexts) {
//                if (reqLine.httpVersion() != null) {
//                    System.out.println("RequestLine: " + reqLine.getText() + " - httpVersion - " + reqLine.httpVersion().getText());
//                }
//            }
            // HINT: Debugging
//            if (this.requestLineContext != null) {
//                System.out.println("correctRequstLine: " + this.requestLineContext.getText());
//
//            lexer = new HTTPLexer(CharStreams.fromString(text));
//            CommonTokenStream tokens = new CommonTokenStream(lexer);
//            tokens.fill();
//            for (Token t : tokens.getTokens()) {
//                System.out.println(t.getText() + " -> " + lexer.getVocabulary().getSymbolicName(t.getType()));
//            }
//
//            for (HTTPParser.RequestContext r : fileCtx.request()) {
//                System.out.println("RequestLine: " + r.requestLine().getText());
//                if (r.requestLine().httpVersion() != null) {
//                    System.out.println("HTTP version: " + r.requestLine().httpVersion().getText());
//                } else {
//                    System.out.println("No httpVersion found");
//                }
//            }
//            }
            finished = true;
        }
        return this;
    }

    @Override
    public List<? extends org.netbeans.modules.csl.api.Error> getDiagnostics() {
        return errors;
    }

    /**
     * Returns the list of all requestLine contexts found.
     */
    public List<HTTPParser.RequestLineContext> getRequestLineContexts() {
        return requestLineContexts;
    }

    @Override
    protected void invalidate() {
        // Optionally clear internal data
    }

    @Override
    protected boolean processingFinished() {
        return finished;
    }

    private ParseTreeListener createFoldListener() {
        return new HTTPParserBaseListener() {
            // Implement code folding if needed
        };
    }

    private ANTLRErrorListener createErrorListener() {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e) {
                int errorPosition = 0;
                if (offendingSymbol instanceof Token) {
                    Token token = (Token) offendingSymbol;
                    errorPosition = token.getStartIndex();
                }
                errors.add(new DefaultError(
                    "http-error",
                    "Invalid content found: " + msg,
                    null,
                    getFileObject(),
                    errorPosition,
                    errorPosition,
                    Severity.ERROR));
            }
        };
    }

    protected final FileObject getFileObject() {
        return getSnapshot().getSource().getFileObject();
    }
}
