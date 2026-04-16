package com.gabriel.backend.crawl.poller;

import com.gabriel.backend.crawl.event.CrawlUpdate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PollerManager {

    private final ConcurrentMap<String, CrawlPoller> poolers = new ConcurrentHashMap<>();

    public void enqueue(CrawlUpdate update) {
        poolers
                .computeIfAbsent(update.getCrawlId(), id -> {
                    CrawlPoller poller = CrawlPollerFactory.getInstance().createPooler(id);
                    poller.startPooling();
                    return poller;
                })
                .submitUpdate(update);
    }

    public void shutdown(String crawlId) {
        CrawlPoller pooler = poolers.remove(crawlId);
        if (pooler != null) {
            pooler.shutdown();
        }
    }

    public void shutdownAll() {
        poolers.values().forEach(CrawlPoller::shutdown);
        poolers.clear();
    }
}


