/*
 * Copyright 2024 Christian Lenz <christian.lenz@gmx.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lb.netbeans.rest.client.ui;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author Christian Lenz <christian.lenz@gmx.net>
 */
public class LineNumberComponent extends JComponent {

    private final JTextComponent textComponent;
    private int lastDigits = 0;

    public LineNumberComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
        Font font = textComponent.getFont();
        setFont(font);
        setPreferredSize(new Dimension(40, textComponent.getHeight()));

//        adjustWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g2d.setColor(Color.GRAY);

        try {
            JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, textComponent);
            if (viewport != null) {
                Point viewPosition = viewport.getViewPosition();
                Rectangle clip = g2d.getClipBounds();

                int startOffset = textComponent.viewToModel2D(new Point(0, clip.y + viewPosition.y));
                int endOffset = textComponent.viewToModel2D(new Point(0, clip.y + clip.height + viewPosition.y));

                Document doc = textComponent.getDocument();
                Element root = doc.getDefaultRootElement();

                int startLine = root.getElementIndex(startOffset);
                int endLine = root.getElementIndex(endOffset);

                FontMetrics fontMetrics = g2d.getFontMetrics();
                int fontAscent = fontMetrics.getAscent();

                for (int line = startLine; line <= endLine; line++) {
                    int lineStartOffset = root.getElement(line).getStartOffset();
                    Rectangle2D r = textComponent.modelToView2D(lineStartOffset);

                    if (r != null && r.getY() - viewPosition.y + r.getHeight() > clip.y) {
                        String lineNumber = String.valueOf(line + 1);
                        int y = (int) Math.round(r.getY() - viewPosition.y + fontAscent);
                        g2d.drawString(lineNumber, getWidth() - fontMetrics.stringWidth(lineNumber) - 5, y);
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void adjustWidth() {
        int lines = getLineCount();
        int digits = Math.max(String.valueOf(lines).length(), 1);

        if (digits != lastDigits) {
            int width = getFontMetrics(getFont()).stringWidth("0") * digits + 16;
            setPreferredSize(new Dimension(width, getHeight()));
            lastDigits = digits;
            revalidate();
        }
    }

    private int getLineCount() {
        return textComponent.getDocument().getDefaultRootElement().getElementCount();
    }
}
