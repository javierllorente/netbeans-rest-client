package com.javierllorente.netbeans.rest.client.http.editor.completion;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring.HTTPTokenId;
import java.util.Collection;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;

/**
 * CompletionProvider für HTTP-Requests, angelehnt an HtmlCompletionProvider.
 */
@org.netbeans.api.editor.mimelookup.MimeRegistration(
    mimeType = HTTPTokenId.MIME_TYPE,
    service = CompletionProvider.class
)
public class HTTPCompletionProvider implements CompletionProvider {

    @Override
    public int getAutoQueryTypes(JTextComponent comp, String typedText) {
        if (typedText != null && typedText.length() == 1) {
            char c = typedText.charAt(0);
            if (Character.isLetterOrDigit(c) || c == ' ' || c == '/') {
                return COMPLETION_QUERY_TYPE;
            }
        }
        return 0;
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent comp) {
        if ((queryType & COMPLETION_QUERY_TYPE) != 0) {
            return new AsyncCompletionTask(new HTTPCompletionQuery(comp), comp);
        }
        return null;
    }

    /**
     * Asynchronous query that delegates to HTTPCompletionLogicNew.
     */
    private static class HTTPCompletionQuery extends AsyncCompletionQuery {

        protected final JTextComponent comp;
        protected int anchor;

        HTTPCompletionQuery(JTextComponent comp) {
            this.comp = comp;
        }

        private Collection<? extends CompletionItem> items;

        @Override
        protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
            try {
                HTTPCompletionLogicNew.CompletionResult res
                    = HTTPCompletionLogicNew.compute(doc, caretOffset);
                if (res != null) {
                    resultSet.setAnchorOffset(res.anchor);
                    for (CompletionItem item : res.items) {
                        resultSet.addItem(item);
                    }
                }
            } catch (BadLocationException ex) {
                // ignore
            } finally {
                resultSet.finish();
            }
        }

        @Override
        public boolean canFilter(JTextComponent component) {
            // disable incremental filtering to avoid unsupported exceptions
            return false;
        }

        @Override
        protected void filter(CompletionResultSet rs) {
            try {
                int cur = comp.getCaretPosition();
                int curPrefixLen = cur - anchor;
                String prefix = comp.getDocument()
                    .getText(anchor, curPrefixLen);
                for (CompletionItem it : items) {
                    if (it.getInsertPrefix().toString()
                        .regionMatches(true, 0, prefix, 0, prefix.length())) {
                        rs.addItem(it);
                    }
                }
                rs.setAnchorOffset(anchor);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                rs.finish();
            }
        }
    }
}
