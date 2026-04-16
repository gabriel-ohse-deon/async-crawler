package com.gabriel.backend.crawl.poller;

import com.gabriel.backend.repository.CrawlDocument;
import com.gabriel.backend.crawl.CrawlStatus;
import com.gabriel.backend.crawl.event.CrawlUpdate;
import com.gabriel.backend.repository.CrawlRepository;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

public class CrawlPoller implements Runnable {

    private final CrawlRepository repository;
    private final BlockingQueue<CrawlUpdate> queue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Set<URI> pendingFound = ConcurrentHashMap.newKeySet();
    private final Set<URI> pendingFailed = ConcurrentHashMap.newKeySet();
    private volatile boolean running;
    private CrawlDocument cachedDoc;
    private final int flushIntervalMs;
    private final int maxPendingUpdates;

    public CrawlPoller(String crawlId,
                       CrawlRepository repository,
                       int flushIntervalMs,
                       int maxPendingUpdates) {
        this.repository = repository;
        this.cachedDoc = repository.findById(crawlId).orElseGet(() -> new CrawlDocument(crawlId));
        this.flushIntervalMs = flushIntervalMs;
        this.maxPendingUpdates = maxPendingUpdates;
    }

    public void submitUpdate(CrawlUpdate update) {
        if (!running) return;
        queue.offer(update);
        if (queue.size() >= maxPendingUpdates) {
            flushAsync();
        }
    }

    private void flushAsync() {
        CompletableFuture.runAsync(this::safeFlush);
    }

    private synchronized void safeFlush() {
        if (queue.isEmpty() && pendingFound.isEmpty() && pendingFailed.isEmpty()) return;

        List<CrawlUpdate> drained = new ArrayList<>();
        queue.drainTo(drained);

        for (CrawlUpdate update : drained) {
            update.getFoundUri().ifPresent(pendingFound::add);
            update.getFailedUri().ifPresent(pendingFailed::add);

            if (update.isDone()) {
                cachedDoc.setStatus(CrawlStatus.DONE);
            }
        }

        cachedDoc.getFoundUris().addAll(pendingFound);
        cachedDoc.getFailedUris().addAll(pendingFailed);

        repository.save(cachedDoc);

        pendingFound.clear();
        pendingFailed.clear();
    }

    public void startPooling() {
        scheduler.scheduleAtFixedRate(this::safeFlush, flushIntervalMs, flushIntervalMs, TimeUnit.MILLISECONDS);
        running = true;
    }

    @Override
    public void run() {
        safeFlush();
    }

    public void shutdown() {
        running = false;
        safeFlush();
        scheduler.shutdown();
    }
}

