/*
 * Copyright 2022 Javier Llorente <javier@opensuse.org>.
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
package com.javierllorente.netbeans.rest.client.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class StatusLabel extends LayerUI<JComponent> {

    private final JLabel label;
    private final JPanel panel;

    public StatusLabel() {
        super();
        label = new JLabel();
        panel = new JPanel();
    }
    
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        Dimension d = label.getPreferredSize();
        int x = c.getWidth() - d.width - 10;
        SwingUtilities.paintComponent(g, label, panel, x, 8, d.width, d.height);
    }
}
