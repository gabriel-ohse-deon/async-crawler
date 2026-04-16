package com.gabriel.backend.repository;

import java.util.Optional;

public interface CrawlRepository {

    Optional<CrawlDocument> findById(String crawlId);

    void save(CrawlDocument data);

    void shutdown();

}
