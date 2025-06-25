/*
 * Copyright 2025 Christian Lenz <christian.lenz@gmx.net>.
 *
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
package com.javierllorente.netbeans.rest.client.http.editor.navigator;

import com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring.HTTPTokenId;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import org.antlr.v4.runtime.ParserRuleContext;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.StructureItem;
import org.netbeans.modules.csl.spi.ParserResult;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Christian Lenz <christian.lenz@gmx.net>
 */
/**
 * HTTPLangStructureItem represents a single request line in an HTTP file.\n It
 * is used by the StructureScanner to display items in the Navigator.\n
 */
public class HTTPLangStructureItem implements StructureItem, ElementHandle {

    private final String name;
    private final int startPosition;
    private final int stopPosition;
    private final FileObject fo;

    /**
     * Constructs a new structure item with the given name and offset range.
     *
     * @param name The display name for this structure item.\n
     */
    // Im Konstruktor:
    public HTTPLangStructureItem(FileObject fo, String name, ParserRuleContext context) {
        this.name = name;
        // Add null checks for context and its start/stop tokens
        this.startPosition = (context != null && context.start != null) ? context.start.getStartIndex() : 0; // Default to 0 if null
        this.stopPosition = (context != null && context.stop != null) ? context.stop.getStopIndex() + 1 : this.startPosition; // Default to start if stop is null
        this.fo = fo;

        // Log warning if context or tokens were null
        if (context == null || context.start == null || context.stop == null) {
            // Use your logger if available, otherwise System.out
            System.err.println("Warning: HTTPLangStructureItem created with null context or tokens for name: " + name);
        }
    }

    /**
     * Returns the display name of the structure item.
     *
     * @return The name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the kind of element. Hier kann man beispielsweise REQUEST als\n
     * eigenen Typ definieren, hier nutzen wir OTHER als Platzhalter.
     *
     * @return The element kind.
     */
    @Override
    public ElementKind getKind() {
        return ElementKind.OTHER;
    }

    /**
     * Returns the text used for sorting structure items.
     *
     * @return The sort text.
     */
    @Override
    public String getSortText() {
        return name;
    }

    /**
     * Returns the HTML representation of the structure item, used in the
     * Navigator.\n Hier wird der Name in einfachem Text dargestellt.
     *
     * @param formatter The HTML formatter.
     * @return The HTML text.
     */
    @Override
    public String getHtml(HtmlFormatter formatter) {
        formatter.appendText(name);
        return formatter.getText();
    }

    /**
     * Returns any nested structure items. Da wir nur RequestLines anzeigen,\n
     * gibt es keine Verschachtelung.
     *
     * @return An empty list.
     */
    @Override
    public List<? extends StructureItem> getNestedItems() {
        return Collections.emptyList();
    }

    @Override
    public ElementHandle getElementHandle() {
        return this;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return Collections.emptySet();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public long getPosition() {
        return startPosition;
    }

    @Override
    public long getEndPosition() {
        return stopPosition;
    }

    @Override
    public ImageIcon getCustomIcon() {
        return new ImageIcon(ImageUtilities.loadImage("com/javierllorente/netbeans/rest/client/http/editor/http.png"));
    }

    @Override
    public FileObject getFileObject() {
        return fo;
    }

    @Override
    public String getMimeType() {
        return HTTPTokenId.MIME_TYPE;
    }

    @Override
    public String getIn() {
        return null;
    }

    @Override
    public boolean signatureEquals(ElementHandle eh) {
        return false;
    }

    @Override
    public OffsetRange getOffsetRange(ParserResult pr) {
        return new OffsetRange(startPosition, stopPosition);
    }
}
