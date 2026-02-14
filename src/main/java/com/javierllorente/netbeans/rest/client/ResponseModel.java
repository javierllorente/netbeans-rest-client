/*
 * Copyright 2026 Javier Llorente <javier@opensuse.org>.
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
package com.javierllorente.netbeans.rest.client;

import jakarta.annotation.Nullable;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 *
 * @author javier
 */
public class ResponseModel {
    
    @Nullable
    private final String requestedUrl;
    @Nullable
    private final Integer status;
    @Nullable
    private final String statusText;
    private final String body;
    @Nullable
    private final MultivaluedMap<String, Object> headers;
    @Nullable
    private final Long elapsedTimeMs;
    @Nullable
    private final String contentType;
 
    public ResponseModel(String requestedUrl, Integer status, String statusText, String body, 
            MultivaluedMap<String, Object> headers, Long elapsedTimeMs) {
        this.requestedUrl = requestedUrl;
        this.status = status;
        this.statusText = statusText;
        this.body = body;
        this.headers = headers != null ? headers : new MultivaluedHashMap<>();
        this.elapsedTimeMs = elapsedTimeMs;
        this.contentType = extractContentType(this.headers);
    }
    
    public ResponseModel(String error) {
        this(null, null, null, error, null, null);
    }
    
    private String extractContentType(MultivaluedMap<String, Object> headers) {
        List<Object> values = headers.get("content-type");
        if (values != null && !values.isEmpty()) {
            Object first = values.get(0);
            if (first instanceof String) {
                return (String) first;
            }
        }
        return "";
    }
    
    public boolean isClientError() {
        return status == null;
    }

    public String getRequestUrl() {
        return requestedUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getBody() {
        return body;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public Long getElapsedTimeMs() {
        return elapsedTimeMs;
    }

    public String getContentType() {
        return contentType;
    }
    
}
