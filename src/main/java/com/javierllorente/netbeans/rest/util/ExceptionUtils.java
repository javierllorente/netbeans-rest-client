/*
 * Copyright 2025 Javier Llorente <javier@opensuse.org>.
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
package com.javierllorente.netbeans.rest.util;

import com.javierllorente.netbeans.rest.client.ui.ResponsePanel;
import jakarta.ws.rs.ProcessingException;
import javax.swing.SwingUtilities;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class ExceptionUtils {

    private ExceptionUtils() {
    }    

    public static void handleAndDisplayProcessingException(ProcessingException ex, ResponsePanel responsePanel) {
        String response = (ex.getMessage().contains("PKIX path building failed"))
                ? "Could not get response: failed to verify SSL certificate\n"
                + "SSL certificate verification is enabled. "
                + "You may disable it under Tools->Options->Miscellaneous->REST Client"
                : ex.getMessage();
        SwingUtilities.invokeLater(() -> {
            responsePanel.setContentType("");
            responsePanel.setResponse(response);
            responsePanel.showResponse();
        });
    }
    
}
