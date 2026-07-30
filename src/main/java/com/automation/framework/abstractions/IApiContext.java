package com.automation.framework.abstractions;

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
    IApiResponse get(String path);
    IApiResponse post(String path);
    IApiResponse put(String path);
    IApiResponse patch(String path);
    IApiResponse delete(String path);
    void dispose();
}
