package com.gabriel.backend.crawl;

import com.gabriel.backend.crawl.action.CrawlTaskFactory;
import com.gabriel.backend.crawl.action.SearcherActionType;
import com.gabriel.backend.crawl.event.CrawlListener;
import com.gabriel.backend.crawl.fetch.Fetcher;
import com.gabriel.backend.crawl.policy.CrawlPolicy;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {

    private final String crawlId;
    private final LinkManager linkManager;
    private final Fetcher fetcher;
    private final CrawlListener crawlListener;
    private final List<CrawlPolicy> policies;
    private final SearcherActionType actionType;
    private final int maxThreads;

    private final ExecutorService executor;
    private final AtomicInteger activeTasks;
    private final AtomicBoolean running;

    public Crawler(String crawlId,
                   LinkManager linkManager,
                   Fetcher fetcher,
                   CrawlListener crawlListener,
                   List<CrawlPolicy> policies,
                   SearcherActionType actionType,
                   int maxThreads) {
        this.crawlId = crawlId;
        this.linkManager = linkManager;
        this.fetcher = fetcher;
        this.crawlListener = crawlListener;
        this.policies = policies;
        this.actionType = actionType;
        this.maxThreads = maxThreads;
        this.activeTasks = new AtomicInteger(0);
        this.running = new AtomicBoolean(false);
        this.executor = Executors.newFixedThreadPool(maxThreads);
    }

    public void start(String keyword, URI startUri) {
        if (running.getAndSet(true)) return;

        linkManager.addInitialLink(startUri);
        crawlListener.onStart(crawlId);

        new Thread(() -> {
            int idleCycles = 0;
            while (running.get()) {
                scheduleTasks(keyword);

                if (!linkManager.hasNext() && activeTasks.get() == 0) {
                    idleCycles++;
                } else {
                    idleCycles = 0; // Reset se houver atividade
                }

                if (idleCycles > 5) {
                    running.set(false);
                    executor.shutdown();
                    crawlListener.onAllTasksCompleted();
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private void scheduleTasks(String keyword) {
        int slotsAvailable = maxThreads - activeTasks.get();
        if (slotsAvailable <= 0) return;

        for (int i = 0; i < slotsAvailable; i++) {
            Optional<LinkItem> next = linkManager.pollNextLink();
            if (next.isEmpty()) break;

            LinkItem item = next.get();
            activeTasks.incrementAndGet();
            executor.submit(() -> {
                try {
                    CrawlTaskFactory.getInstance()
                            .createTask(item,
                                    keyword,
                                    fetcher,
                                    crawlListener,
                                    linkManager,
                                    policies,
                                    actionType).run();
                } finally {
                    activeTasks.decrementAndGet();
                }
            });
        }
    }

    public void stop() {
        running.set(false);
        executor.shutdownNow();
    }

    public String getCrawlId() {
        return crawlId;
    }
}