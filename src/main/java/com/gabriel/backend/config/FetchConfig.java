package com.gabriel.backend.config;

public class FetchConfig {
    private String userAgent;
    private Integer fetchTimeoutMs;
    private Integer connectionTimeoutMs;

    public FetchConfig() {
    }

    public FetchConfig(String userAgent, Integer timeoutMs, Integer connectionTimeoutMs) {
        this.userAgent = userAgent;
        this.fetchTimeoutMs = timeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public Integer getFetchTimeoutMs() {
        return fetchTimeoutMs;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setFetchTimeoutMs(Integer fetchTimeoutMs) {
        this.fetchTimeoutMs = fetchTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

}
