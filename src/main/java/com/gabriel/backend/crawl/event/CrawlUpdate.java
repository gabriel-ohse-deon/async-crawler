package com.gabriel.backend.crawl.event;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class CrawlUpdate {

    private final String crawlId;
    private final URI foundUri;
    private final URI failedUri;
    private final boolean done;
    private final Instant timestamp;

    public CrawlUpdate(String crawlId, URI foundUri, URI failedUri, boolean done) {
        this.crawlId = Objects.requireNonNull(crawlId, "crawlId cannot be null");
        this.foundUri = foundUri;
        this.failedUri = failedUri;
        this.done = done;
        this.timestamp = Instant.now();
    }

    public String getCrawlId() {
        return crawlId;
    }

    public Optional<URI> getFoundUri() {
        return Optional.ofNullable(foundUri);
    }

    public Optional<URI> getFailedUri() {
        return Optional.ofNullable(failedUri);
    }

    public boolean isDone() {
        return done;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public boolean isFoundUpdate() {
        return foundUri != null && !done;
    }

    public boolean isFailedUpdate() {
        return failedUri != null && !done;
    }

    public boolean isTerminalUpdate() {
        return done;
    }

    public static class Builder {
        private String crawlId;
        private URI foundUri;
        private URI failedUri;
        private boolean done;

        public Builder withCrawlId(String crawlId) {
            this.crawlId = crawlId;
            return this;
        }

        public Builder withFoundUri(URI uri) {
            this.foundUri = uri;
            return this;
        }

        public Builder withFailedUri(URI uri) {
            this.failedUri = uri;
            return this;
        }

        public Builder markDone() {
            this.done = true;
            return this;
        }

        public CrawlUpdate build() {
            return new CrawlUpdate(crawlId, foundUri, failedUri, done);
        }
    }

}
