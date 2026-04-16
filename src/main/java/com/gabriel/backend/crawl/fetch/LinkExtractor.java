package com.gabriel.backend.crawl.fetch;

import java.util.Collection;

public interface LinkExtractor<K, E> {

    Collection<E> extractLinks(K text);

}
