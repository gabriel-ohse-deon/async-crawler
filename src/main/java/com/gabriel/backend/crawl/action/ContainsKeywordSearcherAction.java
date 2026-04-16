package com.gabriel.backend.crawl.action;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ContainsKeywordSearcherAction implements CrawlSearchAction<Boolean> {

    private final ConcurrentHashMap<String, Pattern> patternCache = new ConcurrentHashMap<>();

    public ContainsKeywordSearcherAction() {
    }

    @Override
    public Optional<Boolean> search(String text, String keyword) {
        if (text == null || keyword == null || keyword.isBlank()) return Optional.empty();
        return Optional.of(patternCache
                .computeIfAbsent(keyword,
                        k -> Pattern.compile(Pattern.quote(k), Pattern.CASE_INSENSITIVE)).matcher(text).find());
    }

}
