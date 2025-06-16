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
package com.javierllorente.netbeans.rest.client.http.editor.syntax;

import javax.swing.event.ChangeListener;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;

/**
 * HTTPLangParser is the NetBeans Parser class that ties the snapshot to our
 * {@link HTTPLangParserResult}.
 *
 * <p>
 * Whenever NetBeans decides a parse is needed, it calls
 * {@link #parse(Snapshot, Task, SourceModificationEvent)}. We then build a new
 * {@link HTTPLangParserResult}, run its parse() method, and store it in
 * {@code lastResult}.</p>
 */
public class HTTPLangParser extends Parser {

    private Result lastResult;

    /**
     * This is invoked by the NetBeans parsing API to parse the snapshot.
     *
     * @param snapshot The current snapshot of the file's contents.
     * @param task The parsing task (unused in this simple example).
     * @param event SourceModificationEvent with modification info (unused
     * here).
     * @throws ParseException if something goes wrong
     */
    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) throws ParseException {
        // Build a new ParserResult and parse the content
        lastResult = new HTTPLangParserResult(snapshot).parse();
    }

    /**
     * Provides the last parser result created by
     * {@link #parse(Snapshot, Task, SourceModificationEvent)}.
     *
     * @param task The task requesting the result.
     * @return The most recent ParserResult for this snapshot.
     * @throws ParseException if something fails
     */
    @Override
    public Result getResult(Task task) throws ParseException {
        return lastResult;
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        // Not used in this minimal example
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        // Not used in this minimal example
    }
}
