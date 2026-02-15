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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.editor.BaseDocument;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;

/**
 * Completion item for HTTP request elements.
 */
public class HTTPCompletionItem implements CompletionItem {

    private static final Logger LOG = Logger.getLogger(HTTPCompletionItem.class.getName());

    private final String text;
    private final int substituteOffset;
    private final String suffix;
    private final boolean triggerNextCompletion;
    private final boolean obsolete;

    public HTTPCompletionItem(String text, int anchor, String suffix, int caretOffset) {
        this(text, anchor, suffix, caretOffset, false, false);
    }

    public HTTPCompletionItem(String text, int anchor, String suffix, int caretOffset, boolean triggerNextCompletion) {
        this(text, anchor, suffix, caretOffset, triggerNextCompletion, false);
    }

    public HTTPCompletionItem(String text, int anchor, String suffix, int caretOffset, boolean triggerNextCompletion, boolean obsolete) {
        this.text = text;
        this.substituteOffset = anchor;
        this.suffix = suffix != null ? suffix : "";
        this.triggerNextCompletion = triggerNextCompletion;
        this.obsolete = obsolete;
    }

    @Override
    public void defaultAction(JTextComponent component) {
        if (component == null) {
            return;
        }

        Completion.get().hideAll();

        BaseDocument doc = (BaseDocument) component.getDocument();
        String insertText = text + suffix;
        int caretPos = component.getCaretPosition();
        int removeLength = Math.max(0, caretPos - substituteOffset);

        doc.runAtomic(() -> {
            try {
                if (removeLength > 0) {
                    doc.remove(substituteOffset, removeLength);
                }
                doc.insertString(substituteOffset, insertText, null);
            } catch (BadLocationException e) {
                LOG.log(Level.WARNING, "Error during completion", e);
            }
        });

        component.setCaretPosition(substituteOffset + insertText.length());

        if (triggerNextCompletion) {
            SwingUtilities.invokeLater(() -> Completion.get().showCompletion());
        }
    }

    @Override
    public void processKeyEvent(KeyEvent evt) {
    }

    @Override
    public int getPreferredWidth(Graphics g, Font font) {
        return CompletionUtilities.getPreferredWidth(text, null, g, font);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
        String displayText = obsolete ? "<s>" + text + "</s>" : text;
        CompletionUtilities.renderHtml(null, displayText, null, g, defaultFont, (selected ? Color.WHITE : defaultColor), width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return null;
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return obsolete ? 100 : 50;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }
}
