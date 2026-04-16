package com.gabriel.backend.config;

import java.net.URI;

public class CrawlConfig {

    private Integer maxDepth;
    private Integer maxUrls;
    private Boolean followRelativeLinks;
    private URI baseUrl;
    private Integer maxThreads;
    private Integer port;

    public CrawlConfig() {
    }

    public CrawlConfig(Integer maxDepth,
                       Integer maxUrls,
                       Boolean followRelativeLinks,
                       URI baseUrl,
                       Integer maxThreads,
                       Integer port) {
        this.maxDepth = maxDepth;
        this.maxUrls = maxUrls;
        this.followRelativeLinks = followRelativeLinks;
        this.baseUrl = baseUrl;
        this.maxThreads = maxThreads;
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getMaxThreads() {
        return maxThreads;
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public Integer getMaxUrls() {
        return maxUrls;
    }

    public Boolean isFollowRelativeLinks() {
        return followRelativeLinks;
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void setMaxUrls(Integer maxUrls) {
        this.maxUrls = maxUrls;
    }

    public void setFollowRelativeLinks(Boolean followRelativeLinks) {
        this.followRelativeLinks = followRelativeLinks;
    }

    public void setBaseUri(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
