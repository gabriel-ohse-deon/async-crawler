package com.gabriel.backend.crawl.event;

import com.gabriel.backend.crawl.poller.PollerManager;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlEventManager implements CrawlListener {

    private static final Logger logger = Logger.getLogger(CrawlEventManager.class.getName());
    private final String crawlId;
    private final PollerManager pollerManager;

    public CrawlEventManager(String crawlId, PollerManager poolManager) {
        this.crawlId = crawlId;
        this.pollerManager = poolManager;
    }

    @Override
    public void onStart(String crawlId) {
        pollerManager.enqueue(new CrawlUpdate.Builder()
                .withCrawlId(crawlId)
                .build());
    }

    @Override
    public void onFound(URI uri) {
        pollerManager.enqueue(new CrawlUpdate.Builder()
                .withCrawlId(crawlId)
                .withFoundUri(uri)
                .build());
    }

    @Override
    public void onFailed(URI uri) {
        pollerManager.enqueue(new CrawlUpdate.Builder()
                .withCrawlId(crawlId)
                .withFailedUri(uri)
                .build());
    }

    @Override
    public void onError(URI uri, Exception e) {
        onFailed(uri);
        logger.log(Level.SEVERE, "Error crawling URI " + uri, e);
    }

    @Override
    public void onAllTasksCompleted() {
        logger.log(Level.INFO, "Finishing polling process for saving.");
        pollerManager.enqueue(new CrawlUpdate.Builder()
                .withCrawlId(crawlId)
                .markDone()
                .build());

        pollerManager.shutdown(crawlId);
    }

}

