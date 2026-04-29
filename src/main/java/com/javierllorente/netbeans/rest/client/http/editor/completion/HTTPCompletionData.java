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
package com.javierllorente.netbeans.rest.client.http.editor.completion;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides completion data for HTTP methods, URLs, versions, header keys, and
 * header values.
 */
public final class HTTPCompletionData {

    private static final Logger LOG = Logger.getLogger(HTTPCompletionData.class.getName());

    private static final List<String> METHODS = List.of(
        "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "TRACE", "CONNECT"
    );

    private static final List<String> URLS = List.of(
        "http://", "https://", "http://localhost:", "google.de", "/", "*"
    );

    private static final List<String> VERSIONS = List.of("HTTP/1.0", "HTTP/1.1", "HTTP/2", "HTTP/3");

    private static final List<String> HEADER_KEYS = new ArrayList<>();
    private static final Set<String> OBSOLETE_HEADER_KEYS = new HashSet<>();

    static {
        loadHeaders(HEADER_KEYS, OBSOLETE_HEADER_KEYS);
    }

    private static final Map<String, List<String>> HEADER_VALUES = Map.ofEntries(
        Map.entry("Content-Type", List.of("application/atom+xml",
            "application/graphql",
            "application/json",
            "application/octet-stream",
            "application/pdf",
            "application/sql",
            "application/stream+json",
            "application/svg+xml",
            "application/xhtml+xml",
            "application/xml",
            "application/x-ndjson",
            "application/x-www-form-urlencoded",
            "application/x-yaml",
            "application/zip",
            "audio/mpeg",
            "audio/vorbis",
            "image/gif",
            "image/jpeg",
            "image/png",
            "image/svg+xml",
            "image/webp",
            "multipart/form-data",
            "text/csv",
            "text/event-stream",
            "text/html",
            "text/json",
            "text/plain",
            "text/xml",
            "*/*",
            "boundary",
            "charset")),
        Map.entry(
            "Accept", List.of("application/atom+xml",
                "application/graphql",
                "application/json",
                "application/octet-stream",
                "application/pdf",
                "application/sql",
                "application/stream+json",
                "application/svg+xml",
                "application/xhtml+xml",
                "application/xml",
                "application/x-ndjson",
                "application/x-www-form-urlencoded",
                "application/x-yaml",
                "application/zip",
                "audio/mpeg",
                "audio/vorbis",
                "image/gif",
                "image/jpeg",
                "image/png",
                "image/svg+xml",
                "image/webp",
                "multipart/form-data",
                "text/csv",
                "text/event-stream",
                "text/html",
                "text/json",
                "text/plain",
                "text/xml",
                "*/*")
        ),
        Map.entry("Accept-Encoding", List.of("br",
            "bzip2",
            "compress",
            "deflate",
            "exi",
            "gzip",
            "identity",
            "lzma",
            "pack200-gzip",
            "peerdist",
            "sdch",
            "express",
            "xz"
        ))
    );

    private HTTPCompletionData() {
    }

    public static List<String> getMethods() {
        return METHODS;
    }

    public static List<String> getUrls() {
        return URLS;
    }

    public static List<String> getVersions() {
        return VERSIONS;
    }

    public static List<String> getHeaderKeys() {
        return HEADER_KEYS;
    }

    public static boolean isObsoleteHeader(String headerKey) {
        return OBSOLETE_HEADER_KEYS.contains(headerKey);
    }

    public static List<String> getHeaderValues(String headerKey) {
        return HEADER_VALUES.getOrDefault(headerKey, Collections.emptyList());
    }

    private static void loadHeaders(List<String> keys, Set<String> obsolete) {
        try (InputStream headersInputStream = HTTPCompletionData.class.getResourceAsStream("headers.json")) {
            if (headersInputStream == null) {
                LOG.warning("headers.json not found on classpath");
                return;
            }

            try (JsonReader reader = Json.createReader(headersInputStream)) {
                JsonArray array = reader.readArray();

                for (int i = 0; i < array.size(); i++) {
                    JsonObject obj = array.getJsonObject(i);
                    String name = obj.getString("name", null);

                    if (name != null && !name.isBlank()) {
                        keys.add(name);

                        if (obj.getBoolean("obsolete", false)) {
                            obsolete.add(name);
                        }
                    }
                }

                keys.sort(String.CASE_INSENSITIVE_ORDER);
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to load headers.json", e);
        }
    }
}
