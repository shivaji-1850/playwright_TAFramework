package com.automation.framework.playwright;

import com.automation.framework.abstractions.IApiResponse;
import com.automation.framework.abstractions.IApiContext;
import com.automation.framework.logging.FrameworkLogger;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PlaywrightApiContext implements IApiContext {

    private static final Logger log = FrameworkLogger.getLogger(PlaywrightApiContext.class);

    private final Playwright playwright;
    private APIRequestContext apiContext;
    private String baseUrl = "";
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private Object requestBody;

    public PlaywrightApiContext(Playwright playwright, String baseUrl) {
        this.playwright = playwright;
        this.baseUrl = baseUrl;
        this.apiContext = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL(baseUrl));
        log.info("API Context initialized for baseUrl: {}", baseUrl);
    }

    @Override
    public IApiContext baseUrl(String url) {
        this.baseUrl = url;
        if (apiContext != null) {
            apiContext.dispose();
        }
        this.apiContext = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL(baseUrl));
        return this;
    }
    @Override public IApiContext header(String k, String v)      { headers.put(k, v); return this; }
    @Override public IApiContext bearerToken(String token)       { headers.put("Authorization", "Bearer " + token); return this; }
    @Override public IApiContext queryParam(String k, String v)  { queryParams.put(k, v); return this; }
    @Override public IApiContext body(Object body)               { this.requestBody = body; return this; }

    @Override
    public IApiResponse get(String path) {
        log.info("GET {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.get(path, buildOptions()));
    }

    @Override
    public IApiResponse post(String path) {
        log.info("POST {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.post(path, buildOptions()));
    }

    @Override
    public IApiResponse put(String path) {
        log.info("PUT {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.put(path, buildOptions()));
    }

    @Override
    public IApiResponse patch(String path) {
        log.info("PATCH {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.patch(path, buildOptions()));
    }

    @Override
    public IApiResponse delete(String path) {
        log.info("DELETE {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.delete(path, buildOptions()));
    }

    @Override
    public void dispose() {
        if (apiContext != null) {
            apiContext.dispose();
        }
    }

    private RequestOptions buildOptions() {
        RequestOptions opts = RequestOptions.create();
        if (!headers.isEmpty()) {
            headers.forEach(opts::setHeader);
        }
        if (!queryParams.isEmpty()) {
            queryParams.forEach(opts::setQueryParam);
        }
        if (requestBody != null) {
            opts.setData(requestBody);
        }
        // Reset per-request body
        requestBody = null;
        return opts;
    }
}
