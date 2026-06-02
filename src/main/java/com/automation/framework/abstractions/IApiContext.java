package com.automation.framework.abstractions;

import java.util.Map;

/**
 * Framework-agnostic API interaction context.
 * Supports combined UI + API tests (e.g., create data via API, verify via UI).
 */
public interface IApiContext {
    IApiContext baseUrl(String url);
    IApiContext header(String name, String value);
    IApiContext bearerToken(String token);
    IApiContext queryParam(String name, String value);
    IApiContext body(Object body);
    ApiResponse get(String path);
    ApiResponse post(String path);
    ApiResponse put(String path);
    ApiResponse patch(String path);
    ApiResponse delete(String path);
    void dispose();
}
