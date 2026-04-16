package com.gabriel.backend.crawl.action;

import java.util.Optional;

public interface CrawlSearchAction<T> {

    Optional<T> search(String text, String keyword);

}
