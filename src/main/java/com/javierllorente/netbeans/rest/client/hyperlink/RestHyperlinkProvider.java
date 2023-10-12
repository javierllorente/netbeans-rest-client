/*
 * Copyright 2017-2022 The Apache Software Foundation
 * Copyright 2023 Javier Llorente
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
package com.javierllorente.netbeans.rest.client.hyperlink;

import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.editor.document.LineDocumentUtils;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.editor.BaseDocument;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import com.javierllorente.netbeans.rest.client.parsers.Parser;
import com.javierllorente.netbeans.rest.client.editor.RestMediaType;

/**
 *
 * @author Jan Lahoda
 * @author Javier Llorente
 */
@MimeRegistrations({
    @MimeRegistration(mimeType = RestMediaType.JSON, service = HyperlinkProviderExt.class),
    @MimeRegistration(mimeType = RestMediaType.XML, service = HyperlinkProviderExt.class)
})
public class RestHyperlinkProvider implements HyperlinkProviderExt {

    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }

    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType type) {
        return getHyperlinkSpan(doc, offset, type) != null;
    }

    @Override
    public int[] getHyperlinkSpan(Document doc, int offset, HyperlinkType type) {
        if (!(doc instanceof BaseDocument)) {
            return null;
        }        

        try {
            BaseDocument bdoc = (BaseDocument) doc;
            int start = LineDocumentUtils.getLineStart(bdoc, offset);
            int end = LineDocumentUtils.getLineEnd(bdoc, offset);

            for (int[] span : Parser.recognizeURLs(DocumentUtilities.getText(doc, start, end - start))) {
                if (span[0] + start <= offset && offset <= span[1] + start) {
                    return new int[]{
                        span[0] + start,
                        span[1] + start
                    };
                }
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }

    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType type) {
        int[] span = getHyperlinkSpan(doc, offset, type);
        
        if (span == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        RestURLDisplayer urlDisplayer = Lookup.getDefault().lookup(RestURLDisplayer.class);
        
        try {
            String urlText = doc.getText(span[0], span[1] - span[0]);
            urlDisplayer.showURL(new URL(urlText));
        } catch (BadLocationException | MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType type) {
        return null;
    }
}
