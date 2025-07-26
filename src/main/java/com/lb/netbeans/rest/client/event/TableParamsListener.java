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

import com.lb.netbeans.rest.client.ui.UrlPanel;
import com.lb.netbeans.rest.client.ui.TablePanel;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Javier Llorente
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
public class TableParamsListener implements TableModelListener {

    private static final Logger logger = Logger.getLogger(TableParamsListener.class.getName());
    private final TablePanel paramsPanel;
    private final UrlPanel urlPanel;
    private final DocumentListener urlDocumentListener;

    public TableParamsListener(TablePanel paramsPanel, UrlPanel urlPanel, 
            DocumentListener urlDocumentListener) {
        this.paramsPanel = paramsPanel;
        this.urlPanel = urlPanel;
        this.urlDocumentListener = urlDocumentListener;
    }

    @Override
    public void tableChanged(TableModelEvent tme) {
        if (tme.getType() == TableModelEvent.INSERT || tme.getType() == TableModelEvent.DELETE) {
            updateUrlField();
        }
    }

    private void updateUrlField() {
        // urlField.setText() invokes urlDocumentListener.removeUpdate() with
        // an empty text in the DocumentEvent, having all rows removed
        // even if the urlField is not empty.
        // Removing the listener avoids this situation
        logger.log(Level.INFO, "urlField = {0}", urlPanel.getUrl());
        urlPanel.removeUrlDocumentListener(urlDocumentListener);
        urlPanel.setUrl(getUrl() + getParams());
        urlPanel.addUrlDocumentListener(urlDocumentListener);
        
        logger.log(Level.INFO, "updated urlField = {0}", urlPanel.getUrl());
    }
    
    private String getUrl() {
        List<String> url = Arrays.asList(urlPanel.getUrl().split("\\?", 2));
        return url.get(0);
    }

    private String getParams() {
        String params = paramsPanel.getRowCount() > 0 ? "?" : "";
        
        for (int i = 0; i < paramsPanel.getRowCount(); i++) {
                params += paramsPanel.getKey(i) + "=" + paramsPanel.getValue(i);
                if (i != paramsPanel.getRowCount() - 1) {
                    params += "&";
                }            
        }
        
        return params;
    }
    
}
