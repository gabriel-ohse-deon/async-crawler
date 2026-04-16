package com.gabriel.backend.crawl.fetch;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawLinkExtractorFromRawHTML implements LinkExtractor<String, String> {

    private static final Pattern LINK_PATTERN =
            Pattern.compile("(?i)<a\\b[^>]*?\\bhref\\s*=\\s*([\"']?)([^\\s\"'>]+)\\1", Pattern.CASE_INSENSITIVE);

    @Override
    public Collection<String> extractLinks(String rawHtml) {
        Collection<String> links = new HashSet<>();
        if (rawHtml == null) return links;

        Matcher matcher = LINK_PATTERN.matcher(rawHtml);
        while (matcher.find()) {
            String link = matcher.group(2);
            if (link != null) {
                links.add(preprocess(link));
            }
        }

        return links;
    }

    private String preprocess(String link) {
        return link.trim().replace("&amp;", "&");
    }
}
