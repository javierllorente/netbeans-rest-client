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
package com.lb.netbeans.rest.client.parsers;

import com.lb.netbeans.rest.client.ui.TablePanel;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier Llorente
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
public class UrlParamsParser {

    private static final Logger logger = Logger.getLogger(UrlParamsParser.class.getName());
    private final TablePanel paramsPanel;

    public UrlParamsParser(TablePanel paramsPanel) {
        this.paramsPanel = paramsPanel;
    }
    
    public void processChanges(String urlFieldUpdate) {        
        logger.log(Level.INFO, "processChanges() urlFieldUpdate = {0}", urlFieldUpdate);        
        List<String> url = Arrays.asList(urlFieldUpdate.split("\\?", 2));

        if (url.size() > 1) {
            String urlFieldParams = url.get(1);
            List<String> params = Arrays.asList(urlFieldParams.split("\\&", -1));         
            
            if (params.size() >= paramsPanel.getRowCount()) {
                // Add/edit rows                
                
                for (int i = 0; i < params.size(); i++) {
                    List<String> entry = Arrays.asList(params.get(i).split("=", 2));

                    if (params.size() > paramsPanel.getRowCount()) {
                        paramsPanel.addRow(entry.get(0), entry.size() > 1 ? entry.get(1) : "");
                        paramsPanel.selectLastItem();
                        paramsPanel.showLastItem();
                    }
                        
                    if (!paramsPanel.getKey(i).equals(entry.get(0))
                            || !paramsPanel.getValue(i).equals(entry.size() > 1 ? entry.get(1) : "")) {
                        paramsPanel.editRow(i, entry.get(0),
                                entry.size() > 1 ? entry.get(1) : "");
                        paramsPanel.changeSelection(i, 1);
                        logger.log(Level.INFO, "changeSelection to row: {0}", i);
                    }
                }
                
            } else if (params.size() < paramsPanel.getRowCount()) {
                // Remove rows
                
                for (int i = 0; i < paramsPanel.getRowCount(); i++) {                    
                    String row = paramsPanel.getKey(i) + "=" + paramsPanel.getValue(i);
                    if (row.equals("=")) {
                        row = "";
                    }

                    if (i <= params.size() - 1 && !params.get(i).equals(row)) {
                        paramsPanel.removeRow(i);
                        paramsPanel.changeSelection(i, 1);

                        if (i <= params.size() - 1 && !params.get(i).equals(row)) {
                            List<String> entry = Arrays.asList(params.get(i).split("=", 2));
                            paramsPanel.editRow(i, entry.get(0),
                                    entry.size() > 1 ? entry.get(1) : "");
                            paramsPanel.changeSelection(i, 1);
                            logger.log(Level.INFO, "changeSelection to row: {0}", i);
                        }

                    } else if (i > params.size() - 1 
                            && params.size() < paramsPanel.getRowCount()) {
                        paramsPanel.removeRow(i);
                        paramsPanel.changeSelection(--i, 1);
                    }
                }
                
            }            
            
            
        } else if (!paramsPanel.isEmpty()) {
            logger.info("removeAllRows!");
            paramsPanel.removeAllRows();
        }

    }

}
