package com.gabriel.backend.crawl.normalization;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryParamNormalizer implements UrlNormalizationStep {

    @Override
    public URI apply(URI uri) {
        String query = uri.getQuery();
        if (query == null || query.isEmpty()) return uri;

        Map<String, List<String>> params = Arrays.stream(query.split("&"))
                .map(p -> p.split("=", 2))
                .filter(p -> p.length > 0 && !p[0].isBlank())
                .collect(Collectors.groupingBy(
                        p -> decode(p[0]),
                        Collectors.mapping(p -> p.length > 1 ? decode(p[1]) : "", Collectors.toList())
                ));

        String newQuery = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getValue().stream()
                        .filter(v -> !v.isBlank())
                        .sorted()
                        .map(v -> encode(e.getKey()) + "=" + encode(v))
                        .collect(Collectors.joining("&"))
                )
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("&"));

        try {
            return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    uri.getPath(), newQuery, null);
        } catch (Exception e) {
            return uri;
        }
    }

    private String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    private String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    @Override
    public int getOrder() {
        return 3;
    }

}
