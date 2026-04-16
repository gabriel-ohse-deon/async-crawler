package com.gabriel.backend.crawl.normalization;

import java.net.URI;

public class TrailingSlashNormalizer implements UrlNormalizationStep {

    @Override
    public URI apply(URI uri) {
        if (uri == null) return null;

        try {
            String path = uri.getPath();

            if (path == null || path.isEmpty()) {
                path = "/";
            }

            // Remove barras finais extras, exceto se o caminho for exatamente "/"
            while (path.length() > 1 && path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            return new URI(
                    uri.getScheme(),
                    uri.getUserInfo(),
                    uri.getHost(),
                    uri.getPort(),
                    path,
                    uri.getQuery(),
                    uri.getFragment()
            );
        } catch (Exception e) {
            return uri;
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
