package com.javierllorente.netbeans.rest.client.http.editor.navigator;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.HTTPLangParserResult;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.antlr.HTTPParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.StructureItem;
import org.netbeans.modules.csl.api.StructureScanner;
import org.netbeans.modules.csl.spi.ParserResult;
import org.openide.filesystems.FileObject;

/**
 * HTTPLangStructureScanner scans the parsed HTTP file (HTTPLangParserResult)
 * and produces structure items for each request line.
 *
 * <p>
 * These structure items are instances of HTTPLangStructureItem and will be
 * displayed in the Navigator (Outline) of the editor. Each item represents a
 * request line in the HTTP file.</p>
 */
public class HTTPLangStructureScanner implements StructureScanner {

    /**
     * Scans the parser result and returns a list of structure items.
     *
     * @param result the ParserResult (expected to be a HTTPLangParserResult)
     * @return a list of structure items, or an empty list if none found
     */
    @Override
    public List<? extends StructureItem> scan(ParserResult result) {
        // Überprüfen, ob das ParserResult vom erwarteten Typ ist
        if (!(result instanceof HTTPLangParserResult)) {
            return Collections.emptyList();
        }
        HTTPLangParserResult httpResult = (HTTPLangParserResult) result;

        // Stelle sicher, dass der Parser bereits ausgeführt wurde
        httpResult.parse();

        List<StructureItem> items = new ArrayList<>();
        // Hole das FileObject aus dem Snapshot des ParserResults
        FileObject fo = httpResult.getSnapshot().getSource().getFileObject();

        // Hole alle RequestLine-Kontexte, die im ParserResult gefunden wurden
        List<HTTPParser.RequestLineContext> requestLines = httpResult.getRequestLineContexts();
        if (requestLines != null) {
            for (HTTPParser.RequestLineContext reqLine : requestLines) {
                // Nutze den Text der RequestLine als Display-Namen. Kürze ihn, falls er zu lang ist.
                String displayName = reqLine.getText();
                if (displayName.length() > 40) {
                    displayName = displayName.substring(0, 40) + "...";
                }
                // Erstelle ein StructureItem für diese RequestLine.
                items.add(new HTTPLangStructureItem(fo, displayName, reqLine));
            }
        }
        return items;
    }

    @Override
    public Map<String, List<OffsetRange>> folds(ParserResult pr) {
        return Collections.emptyMap();
    }

    @Override
    public Configuration getConfiguration() {
        return new Configuration(true, false);
    }
}
