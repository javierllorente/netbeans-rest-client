/*
 * Copyright 2022 Javier Llorente <javier@opensuse.org>.
 * Copyright 2025        Luca Bartoli <lbdevweb@gmail.com>
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
package com.lb.netbeans.rest.client.event;

import com.lb.netbeans.rest.client.parsers.UrlParamsParser;
import com.lb.netbeans.rest.client.ui.TablePanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.openide.util.Exceptions;

/**
 *
 * @author Javier Llorente
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
public class UrlDocumentListener implements DocumentListener {
    
    private final UrlParamsParser parser;

    public UrlDocumentListener(TablePanel paramsPanel) {
        parser = new UrlParamsParser(paramsPanel);
    }    
    
    @Override
    public void insertUpdate(DocumentEvent de) {
        try {
            String urlFieldUpdate = de.getDocument().getText(0, de.getDocument().getLength());
            parser.processChanges(urlFieldUpdate);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Override
    public void removeUpdate(DocumentEvent de) {        
        try {
            String urlFieldUpdate = de.getDocument().getText(0, de.getDocument().getLength());
            parser.processChanges(urlFieldUpdate);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        
    }
}
