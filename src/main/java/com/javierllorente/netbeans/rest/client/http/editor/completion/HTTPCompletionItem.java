// main/java/com/javierllorente/netbeans/rest/client/http/editor/completion/HTTPCompletionItem.java
package com.javierllorente.netbeans.rest.client.http.editor.completion;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.spi.editor.completion.CompletionItem;

// --- HTTPCompletionItem Placeholder ---
// Use the actual HTTPCompletionItem class provided by the user
public class HTTPCompletionItem implements CompletionItem {

    private static final Logger log = Logger.getLogger(HTTPCompletionItem.class.getName());

    // ... (Implementation provided by user - IMPORTANT: ensure defaultAction is correct!) ...
    private final String text;
    private final int substituteOffset;
    private final int substituteLength;
    private final String suffix;

    // Constructor updated to receive caretOffset
    public HTTPCompletionItem(String text, int anchor, String suffix, int caretOffset) {
        this.text = text;
        this.substituteOffset = anchor;
        this.substituteLength = Math.max(0, caretOffset - anchor); // Ensure non-negative length
        this.suffix = suffix != null ? suffix : "";
    }

    @Override
    public void defaultAction(javax.swing.text.JTextComponent component) {
        // (Implementation provided by user - MUST correctly use substituteOffset/Length)
        log.log(Level.INFO, String.format("Default action called for item: '%s', anchor: %d, caret: %d",
            text, substituteOffset, substituteOffset + substituteLength)); // Log intended replacement range
        try {
            Document doc = component.getDocument();
            // Use runAtomic for safety and undo support if possible in NetBeans context
            // DocumentUtilities.runAtomic(doc, () -> { ... });
            if (substituteLength > 0) {
                doc.remove(substituteOffset, substituteLength);
            }
            doc.insertString(substituteOffset, text + suffix, null);
            component.setCaretPosition(substituteOffset + text.length() + suffix.length()); // Caret after inserted text+suffix
            org.netbeans.api.editor.completion.Completion.get().hideAll();
        } catch (BadLocationException e) {
            log.log(Level.SEVERE, "Error during completion item action", e);
        }
    }

    // ... other CompletionItem methods (render, getSortText, etc.) ...
    @Override
    public void processKeyEvent(java.awt.event.KeyEvent evt) {
    }

    @Override
    public int getPreferredWidth(java.awt.Graphics g, java.awt.Font font) {
        return org.netbeans.spi.editor.completion.support.CompletionUtilities.getPreferredWidth(text, null, g, font);
    }

    @Override
    public void render(java.awt.Graphics g, java.awt.Font defaultFont, java.awt.Color defaultColor, java.awt.Color backgroundColor, int width, int height, boolean selected) {
        org.netbeans.spi.editor.completion.support.CompletionUtilities.renderHtml(null, text, null, g, defaultFont, (selected ? java.awt.Color.WHITE : defaultColor), width, height, selected);
    }

    @Override
    public org.netbeans.spi.editor.completion.CompletionTask createDocumentationTask() {
        return null;
    }

    @Override
    public org.netbeans.spi.editor.completion.CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(javax.swing.text.JTextComponent component) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 50;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    } // Just the main text usually
}
