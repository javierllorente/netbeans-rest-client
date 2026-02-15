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
package com.javierllorente.netbeans.rest.client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.ServerErrorException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;

/**
 * Initializes the REST client eagerly at module load to avoid cold-start
 * delays. The Jersey client and its dependencies are loaded in the background
 * so that the first HTTP request doesn't suffer from class loading latency.
 */
public class RestClientInitializer extends ModuleInstall {

    private static final Logger logger = Logger.getLogger(RestClientInitializer.class.getName());

    @Override
    public void restored() {
        Thread warmupThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();

            try {
                logger.info("RestClient warmup starting...");
                RestClient client = new RestClient();
                client.request("http://127.0.0.1:1/warmup", "GET");
            } catch (ClientErrorException | ProcessingException | ServerErrorException ignored) {
                // Expected to fail - we just want to warm up the HTTP stack
            }

            long elapsed = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "RestClient warmup completed in {0} ms", elapsed);
        }, "RestClient-Warmup");

        warmupThread.setDaemon(true);
        warmupThread.setPriority(Thread.MIN_PRIORITY);
        warmupThread.start();
    }

    public static RestClient createClient() {
        return new RestClient();
    }
}
