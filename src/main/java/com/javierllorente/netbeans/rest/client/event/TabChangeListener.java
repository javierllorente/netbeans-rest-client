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
package com.javierllorente.netbeans.rest.client.event;

import com.javierllorente.netbeans.rest.client.ui.AuthPanel;
import com.javierllorente.netbeans.rest.client.ui.TablePanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class TabChangeListener implements ChangeListener {
    
    private final int AUTHORISATION_TAB = 1;
    
    private final TablePanel headersPanel;
    private final AuthPanel authPanel;
    private final DocumentListener tokenDocumentListener;

    public TabChangeListener(TablePanel headersPanel, AuthPanel authPanel,
            DocumentListener tokenDocumentListener) {
        this.headersPanel = headersPanel;
        this.authPanel = authPanel;
        this.tokenDocumentListener = tokenDocumentListener;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        JTabbedPane pane = (JTabbedPane) ce.getSource();
        if (pane.getSelectedIndex() == AUTHORISATION_TAB) {
            int index = headersPanel.containsKey("Authorization");
            String token = "";

            if (index != -1 && headersPanel.getValue(index).startsWith("Bearer")) {
                token = headersPanel.getValue(index).replaceFirst("Bearer ", "");
            }

            authPanel.removeTokenDocumentListener(tokenDocumentListener);
            authPanel.setToken(token);
            authPanel.addTokenDocumentListener(tokenDocumentListener);
        }
    }
    
}
