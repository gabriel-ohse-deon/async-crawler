package com.gabriel.backend.crawl.normalization;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

public class MalformedSchemeNormalizerStep implements UrlNormalizationStep {

    @Override
    public URI apply(URI uri) {
        if (uri == null) return null;

        try {
            String scheme = uri.getScheme();
            String ssp = uri.getSchemeSpecificPart();
            String fragment = uri.getFragment();

            if (ssp != null) {
                ssp = encodeInvalidChars(ssp);
            }

            return new URI(
                    scheme,
                    ssp,
                    fragment
            );
        } catch (URISyntaxException e) {
            return uri;
        }
    }

    private String encodeInvalidChars(String input) {
        input = input.replace(" ", "%20");
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c <= 31 || c >= 127 || c == '%') {
                sb.append(URLEncoder.encode(String.valueOf(c), StandardCharsets.UTF_8));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
