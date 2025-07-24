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
package com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring;

import com.javierllorente.netbeans.rest.client.http.editor.navigator.HTTPLangStructureScanner;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.HTTPLangLexer;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.HTTPLangParser;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.HTTPLangParserResult;
import com.javierllorente.netbeans.rest.client.http.editor.syntax.coloring.semantic.HTTPSemanticAnalyzer;
import java.util.Collection;
import java.util.EnumSet;
import org.netbeans.api.lexer.Language;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.modules.csl.api.SemanticAnalyzer;
import org.netbeans.modules.csl.api.StructureScanner;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.MIMEResolver;
import org.openide.util.*;
import org.openide.windows.TopComponent;

/**
 *
 * @author Christian Lenz <christian.lenz@gmx.net>
 */
@NbBundle.Messages(
    "HTTPResolver=HTTP File"
)
@MIMEResolver.ExtensionRegistration(
    displayName = "#HTTPResolver",
    extension = {"http", "HTTP", "rest", "REST"},
    mimeType = HTTPLanguage.MIME_TYPE,
    position = 315
)

@ActionReferences({
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
        position = 100,
        separatorAfter = 200
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
        position = 300
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
        position = 400
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.PasteAction"),
        position = 550,
        separatorAfter = 600
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
        position = 700
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
        position = 800,
        separatorAfter = 900
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
        position = 1000,
        separatorAfter = 1100
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
        position = 1200,
        separatorAfter = 1300
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
        position = 1400
    ),
    @ActionReference(
        path = "Loaders/text/x-http/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
        position = 1500
    )
})
@LanguageRegistration(mimeType = HTTPLanguage.MIME_TYPE, useMultiview = true)
public class HTTPLanguage extends DefaultLanguageConfig {

    public static final String MIME_TYPE = "text/x-http";

    @Override
    public Language getLexerLanguage() {
        return LANGUAGE;
    }

    @Override
    public String getLineCommentPrefix() {
        return "#"; // NOI18N
    }

    @Override
    public String getDisplayName() {
        return "Http"; //NOI18N
    }

    @Override
    public SemanticAnalyzer<HTTPLangParserResult> getSemanticAnalyzer() {
        return new HTTPSemanticAnalyzer();
    }

    @Override
    public Parser getParser() {
        return new HTTPLangParser();
    }

    @Override
    public StructureScanner getStructureScanner() {
        return new HTTPLangStructureScanner();
    }

    @Override
    public boolean hasStructureScanner() {
        return true;
    }

    public static final Language<HTTPTokenId> LANGUAGE = new LanguageHierarchy<HTTPTokenId>() {
        @Override
        protected Collection<HTTPTokenId> createTokenIds() {
            return EnumSet.allOf(HTTPTokenId.class);
        }

        @Override
        protected Lexer<HTTPTokenId> createLexer(LexerRestartInfo<HTTPTokenId> info) {
            return new HTTPLangLexer(info);
        }

        @Override
        protected String mimeType() {
            return HTTPLanguage.MIME_TYPE;
        }
    }.language();

    @NbBundle.Messages("Source=&Source")
    @MultiViewElement.Registration(
        displayName = "#Source",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
        mimeType = HTTPLanguage.MIME_TYPE,
        preferredID = "http.source",
        position = 100
    )
    public static MultiViewEditorElement createMultiViewEditorElement(Lookup context) {
        return new MultiViewEditorElement(context);
    }
}
