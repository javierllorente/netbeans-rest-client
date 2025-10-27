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
package com.javierllorente.netbeans.rest.client.http.editor.sidebar;

import com.javierllorente.netbeans.rest.client.http.editor.sidebar.request.IRequestProcessor;
import com.javierllorente.netbeans.rest.client.http.editor.sidebar.request.Request;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.editor.Utilities;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 *
 * @author Chrizzly
 */
public class DrawingPanel extends JPanel {

    private final JTextComponent textComponent;
    private final StyledDocument document;
    private final List<Rectangle2D> taskAreas;
    private final IRequestProcessor taskProcessor;  // Nur ein TaskProcessor
    private final ActionPopup actionPopup;

    public DrawingPanel(JTextComponent editor, IRequestProcessor processor) {  // Nur ein TaskProcessor
        this.textComponent = editor;
        this.document = (StyledDocument) editor.getDocument();
        this.taskAreas = new ArrayList<>();
        this.taskProcessor = processor;  // Setze den übergebenen TaskProcessor
        this.actionPopup = new ActionPopup(taskProcessor);

        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSidebar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSidebar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSidebar();
            }
        });

        this.setPreferredSize(new Dimension(20, HEIGHT));
        try {
            // Initialisiere Request-Liste vom TaskProcessor
            taskProcessor.updateRequestsList(document.getText(0, document.getLength()));
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }

        addMouseListener(new PopupListener());
        addMouseMotionListener(new CursorListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Utilities.runViewHierarchyTransaction(textComponent, true, () -> paintComponentUnderLock(g));
    }

    private void paintComponentUnderLock(Graphics g) {
        Rectangle clip = g.getClipBounds();
        g.setColor(textComponent.getBackground());

        if (clip != null) {
            g.fillRect(clip.x, clip.y, clip.width, clip.height);
        }

        if (taskProcessor == null) {
            return;
        }

        try {
            drawPanel(textComponent, g);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void drawPanel(JTextComponent component, Graphics g) throws BadLocationException {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 170, 0));

        taskAreas.clear();
        List<Request> tasks = taskProcessor.getRequests();  // Hol die Tasks vom TaskProcessor
        tasks.sort((t1, t2) -> Integer.compare(t1.getLineNumber(), t2.getLineNumber()));

        for (Request task : tasks) {
            int lineNumber = task.getLineNumber();
            int startOffset = NbDocument.findLineOffset(document, lineNumber);
            Rectangle2D rect = component.modelToView2D(startOffset);

            if (rect != null) {
                int[] xPoints = {4, 10, 4};
                int[] yPoints = {(int) rect.getY(), (int) rect.getY() + 7, (int) rect.getY() + 14};
                g2.fillPolygon(xPoints, yPoints, 3);
                taskAreas.add(new Rectangle2D.Float(4, (float) rect.getY(), 6, 14));
            }
        }
    }

    private void updateSidebar() {
        try {
            String text = document.getText(0, document.getLength());
            updateSidebar(text);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void updateSidebar(String text) {
        taskProcessor.updateRequestsList(text);
        revalidate();
        repaint();
    }

    private class PopupListener extends java.awt.event.MouseAdapter {

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                showPopup(e);
            }
        }

        private void showPopup(java.awt.event.MouseEvent e) {
            for (int i = 0; i < taskProcessor.getRequests().size(); i++) {
                Request task = taskProcessor.getRequests().get(i);
                Rectangle2D rect = taskAreas.get(i);

                if (rect.contains(e.getPoint())) {
                    javax.swing.JPopupMenu popupMenu = actionPopup.createPopupMenu(task);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    break;
                }
            }
        }
    }

    private class CursorListener extends java.awt.event.MouseAdapter {

        @Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
            for (Rectangle2D rect : taskAreas) {
                if (rect.contains(e.getPoint())) {
                    setCursor(Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                    return;
                }
            }
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
