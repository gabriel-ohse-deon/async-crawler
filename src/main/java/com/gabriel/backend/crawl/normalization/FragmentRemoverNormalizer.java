package com.gabriel.backend.crawl.normalization;

import java.net.URI;

public class FragmentRemoverNormalizer implements UrlNormalizationStep {

    @Override
    public URI apply(URI uri) {
        if (uri == null || uri.getFragment() == null) return uri;
        try {
            return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
                    uri.getPort(), uri.getPath(), uri.getQuery(), null);
        } catch (Exception e) {
            return uri;
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
