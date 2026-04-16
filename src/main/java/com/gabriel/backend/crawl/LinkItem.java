package com.gabriel.backend.crawl;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

public class LinkItem {
    private static final String HAS_PATH_LINK_REGEX = "^[a-zA-Z][a-zA-Z0-9+\\-.]*:.*";

    private final URI rootUri;
    private final String originalRawUri;
    private final URI parentUri;
    private final URI absoluteNormalizedUri;
    private final AtomicBoolean visited;
    private final boolean wasRelative;

    public LinkItem(String originalRawUri, URI baseUri, URI absoluteNormalizedUri, URI rootUri) {
        this.originalRawUri = originalRawUri;
        this.parentUri = baseUri;
        this.absoluteNormalizedUri = absoluteNormalizedUri;
        this.visited = new AtomicBoolean(false);
        this.rootUri = rootUri;
        wasRelative = !originalRawUri.matches(HAS_PATH_LINK_REGEX);
    }

    public boolean markVisitedIfNot() {
        return visited.compareAndSet(false, true);
    }

    public URI getAbsoluteNormalizedUri() {
        return absoluteNormalizedUri;
    }

    public String getoriginalRawUri() {
        return originalRawUri;
    }

    public URI getParentUri() {
        return parentUri;
    }

    public boolean isVisited() {
        return visited.get();
    }

    public URI getRootUri() {
        return rootUri;
    }

    public boolean wasRelative() {
        return wasRelative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkItem)) return false;
        LinkItem other = (LinkItem) o;
        return this.absoluteNormalizedUri.toString().equalsIgnoreCase(other.absoluteNormalizedUri.toString());
    }

    @Override
    public int hashCode() {
        return this.absoluteNormalizedUri.toString().toLowerCase().hashCode();
    }

}

