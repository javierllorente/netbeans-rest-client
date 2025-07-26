/*
 * Copyright 2023 Javier Llorente <javier@opensuse.org>.
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
package com.lb.netbeans.rest.client.editor;

import javax.swing.text.EditorKit;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.NbEditorKit;

/**
 *
 * @author Javier Llorente
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
@MimeRegistration(mimeType = RestMediaType.XML, service = EditorKit.class)
public class XmlEditorKit extends NbEditorKit {

    @Override
    public String getContentType() {
        return RestMediaType.XML;
    }
    
}
