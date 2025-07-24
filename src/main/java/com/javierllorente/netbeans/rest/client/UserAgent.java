/*
 * Copyright (C) 2022-2023 Javier Llorente <javier@opensuse.org>
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
package com.javierllorente.netbeans.rest.client;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class UserAgent {

    public final static String NAME = "RestClient";
    public final static String VERSION = "0.7.0";
    public final static String FULL = NAME + "/" + VERSION 
            + " (" + System.getProperty("os.name") + " " 
            + System.getProperty("os.version") + "; " 
            + System.getProperty("os.arch") + ") "
            + System.getProperty("netbeans.productversion");

    private UserAgent() {
    }
    
}
