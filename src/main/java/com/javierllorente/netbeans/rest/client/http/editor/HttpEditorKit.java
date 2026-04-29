/*
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
package com.javierllorente.netbeans.rest.client.http.editor;

import javax.swing.Action;
import javax.swing.text.EditorKit;
import javax.swing.text.TextAction;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.editor.ext.ExtKit.ToggleCommentAction;
import org.netbeans.modules.editor.NbEditorKit;

@MimeRegistration(mimeType = HttpEditorKit.HTTP_MIME_TYPE, service = EditorKit.class)
public class HttpEditorKit extends NbEditorKit {

    @StaticResource
    public static final String HTTP_ICON = "com/javierllorente/netbeans/rest/client/http/editor/http.png";
    public static final String HTTP_MIME_TYPE = "text/x-http"; // NOI18N

    @Override
    public String getContentType() {
        return HTTP_MIME_TYPE;
    }

    @Override
    protected Action[] createActions() {
        Action[] actions = new Action[]{
            new ToggleCommentAction("#"), //NOI18N
        };
        return TextAction.augmentList(super.createActions(), actions);
    }

    @Override
    public Object clone() {
        return super.clone();
    }
}
