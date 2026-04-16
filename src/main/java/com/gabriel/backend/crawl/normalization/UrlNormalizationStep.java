package com.gabriel.backend.crawl.normalization;

import java.net.URI;

public interface UrlNormalizationStep {
    URI apply(URI uri);
    int getOrder();
}
