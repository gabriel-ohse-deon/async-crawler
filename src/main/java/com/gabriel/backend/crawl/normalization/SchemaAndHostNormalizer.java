package com.gabriel.backend.crawl.normalization;

import java.net.URI;

public class SchemaAndHostNormalizer implements UrlNormalizationStep {

    @Override
    public URI apply(URI uri) {
        try {
            String scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "http";
            String host = uri.getHost() != null ? uri.getHost().toLowerCase() : "";
            return new URI(
                    scheme, uri.getUserInfo(), host, uri.getPort(),
                    uri.getPath(), uri.getQuery(), null
            ).normalize();
        } catch (Exception e) {
            return uri;
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
