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
package com.javierllorente.netbeans.rest.client.parsers;

import com.javierllorente.netbeans.rest.client.ui.UrlPanel;
import com.javierllorente.netbeans.rest.client.ui.TablePanel;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class CellParamsParser {
    
    private static final Logger logger = Logger.getLogger(CellParamsParser.class.getName());
    private final TablePanel paramsPanel;
    private final UrlPanel urlPanel;
    private final DocumentListener urlDocumentListener;

    public CellParamsParser(TablePanel paramsPanel, UrlPanel urlPanel, 
            DocumentListener urlDocumentListener) {
        this.paramsPanel = paramsPanel;
        this.urlPanel = urlPanel;
        this.urlDocumentListener = urlDocumentListener;
    }

    public void processChanges(String cellText) {
        logger.log(Level.INFO, "processChanges() cellText = {0}", cellText);
        String params = "?";

        for (int i = 0; i < paramsPanel.getRowCount(); i++) {

            if (i != paramsPanel.getSelectedRow()) {
                params += paramsPanel.getKey(i) + "=" + paramsPanel.getValue(i);
            } else {
                params += (paramsPanel.getSelectedColumn() == 1)
                        ? cellText + "=" + paramsPanel.getValue(i)
                        : paramsPanel.getKey(i) + "=" + cellText;
            }

            if (i != paramsPanel.getRowCount() - 1) {
                params += "&";
            }
        }

        List<String> url = Arrays.asList(urlPanel.getUrl().split("\\?", 2));
        urlPanel.removeUrlDocumentListener(urlDocumentListener);
        urlPanel.setUrl(url.get(0) + params);
        urlPanel.addUrlDocumentListener(urlDocumentListener);
        
        logger.log(Level.INFO, "url = {0}", url);
        logger.log(Level.INFO, "params = {0}", params);
    }
        
}
