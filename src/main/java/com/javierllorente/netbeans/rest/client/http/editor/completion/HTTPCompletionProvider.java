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

/**
 * CompletionProvider for HTTP files.
 */
@org.netbeans.api.editor.mimelookup.MimeRegistration(
    mimeType = HTTPTokenId.MIME_TYPE,
    service = CompletionProvider.class
)
public class HTTPCompletionProvider implements CompletionProvider {

    @Override
    public int getAutoQueryTypes(JTextComponent comp, String typedText) {
        if (typedText == null || typedText.length() != 1) {
            return 0;
        }

        char c = typedText.charAt(0);
        boolean isCompletionTrigger = Character.isLetterOrDigit(c) || c == ' ' || c == '/';

        if (isCompletionTrigger && !isInRequestBody(comp)) {
            return COMPLETION_QUERY_TYPE;
        }

        return 0;
    }

    private boolean isInRequestBody(JTextComponent comp) {
        try {
            Document doc = comp.getDocument();
            int caretOffset = comp.getCaretPosition();
            String text = doc.getText(0, caretOffset);

            int requestStart = text.lastIndexOf("###");
            if (requestStart != -1) {
                int lineEnd = text.indexOf('\n', requestStart);
                requestStart = (lineEnd != -1) ? lineEnd + 1 : text.length();
            } else {
                requestStart = 0;
            }

            String currentRequest = text.substring(requestStart);

            return currentRequest.contains("\n\n") || currentRequest.contains("\r\n\r\n");
        } catch (BadLocationException e) {
            return false;
        }
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent comp) {
        if ((queryType & COMPLETION_QUERY_TYPE) != 0) {
            return new AsyncCompletionTask(new HTTPCompletionQuery(comp), comp);
        }
        return null;
    }

    /**
     * Asynchronous query that delegates to HTTPCompletionLogic.
     */
    private static class HTTPCompletionQuery extends AsyncCompletionQuery {

        private final JTextComponent comp;
        private Collection<? extends CompletionItem> items;
        private int queryAnchor;

        HTTPCompletionQuery(JTextComponent comp) {
            this.comp = comp;
        }

        @Override
        protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
            try {
                CompletionResult res = HTTPCompletionLogic.compute(doc, caretOffset);
                if (res != null) {
                    this.items = res.items;
                    this.queryAnchor = res.anchor;
                    resultSet.setAnchorOffset(res.anchor);

                    for (CompletionItem item : res.items) {
                        resultSet.addItem(item);
                    }
                } else {
                    this.items = null;
                }
            } catch (BadLocationException ex) {
                // ignore
            } finally {
                resultSet.finish();
            }
        }

        @Override
        protected boolean canFilter(JTextComponent component) {
            if (items == null || items.isEmpty()) {
                return false;
            }

            return component.getCaretPosition() >= queryAnchor;
        }

        @Override
        protected void filter(CompletionResultSet rs) {
            try {
                if (items == null) {
                    return;
                }

                int cur = comp.getCaretPosition();
                int curPrefixLen = cur - queryAnchor;

                if (curPrefixLen < 0) {
                    return;
                }

                String prefix = comp.getDocument().getText(queryAnchor, curPrefixLen);

                for (CompletionItem it : items) {
                    if (it.getInsertPrefix().toString()
                        .regionMatches(true, 0, prefix, 0, prefix.length())) {
                        rs.addItem(it);
                    }
                }

                rs.setAnchorOffset(queryAnchor);
            } catch (BadLocationException ex) {
                // ignore
            } finally {
                rs.finish();
            }
        }
    }
}
