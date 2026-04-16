package com.gabriel.backend.crawl.fetch;

import java.net.URI;
import java.util.Optional;

public interface Fetcher {
    Optional<String> fetch(URI uri);
    FetcherType getType();
}
