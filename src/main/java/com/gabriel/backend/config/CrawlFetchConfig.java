package com.gabriel.backend.config;

public class CrawlFetchConfig {
    private String type;
    private Integer timeoutMs;
    private String userAgent;

    public CrawlFetchConfig() {
    }

    public CrawlFetchConfig(String type, Integer timeoutMs, String userAgent) {
        this.type = type;
        this.timeoutMs = timeoutMs;
        this.userAgent = userAgent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
