/*
 * Copyright (C) 2025 Christian Lenz <christian.lenz@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javierllorente.netbeans.rest.client.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.StyledDocument;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 * Utility class for creating and opening .http files in the editor.
 */
public class HttpFileUtils {

    private HttpFileUtils() {
        // Utility class
    }

    /**
     * Creates a new .http file with the given content and opens it in the editor.
     *
     * @param httpContent The HTTP request content to write to the file
     * @param caretPosition The position where to place the cursor, or -1 to place it at the end
     * @throws IOException if file creation or opening fails
     */
    public static void createAndOpenHttpFile(String httpContent, int caretPosition) throws IOException {
        if (httpContent == null) {
            httpContent = "";
        }

        // Create .http file inside netbeans-rest-client folder in user directory
        File userDir = new File(System.getProperty("user.home"), ".netbeans/netbeans-rest-client");
        userDir.mkdirs();
        String timestamp = new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new Date());
        File httpFile = new File(userDir, "request-" + timestamp + ".http");
        Files.writeString(httpFile.toPath(), httpContent);

        // Open the file in the editor
        FileObject fileObject = FileUtil.toFileObject(httpFile);
        if (fileObject != null) {
            DataObject dataObject = DataObject.find(fileObject);
            OpenCookie openCookie = dataObject.getLookup().lookup(OpenCookie.class);
            EditorCookie editor = dataObject.getLookup().lookup(EditorCookie.class);

            if (openCookie == null) {
                return;
            }

            openCookie.open();
            editor.openDocument();

            // Position cursor if specified
            if (caretPosition >= 0) {
                final int finalCaretPosition = caretPosition;

                SwingUtilities.invokeLater(() -> {
                    StyledDocument doc = editor.getDocument();
                    if (doc == null) {
                        return;
                    }

                    JEditorPane[] panes = editor.getOpenedPanes();
                    if (panes == null || panes.length == 0) {
                        return;
                    }

                    int pos = Math.min(finalCaretPosition, doc.getLength());
                    panes[0].setCaretPosition(pos);
                });
            }
        }
    }
}
