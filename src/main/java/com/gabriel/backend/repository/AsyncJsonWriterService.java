package com.gabriel.backend.repository;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncJsonWriterService implements CrawlRepository {

    private final BlockingQueue<CrawlDocument> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final JsonCrawlRepository repository;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public AsyncJsonWriterService(JsonCrawlRepository repository) {
        this.repository = new JsonCrawlRepository();
        executor.submit(this::processQueue);
    }

    @Override
    public void save(CrawlDocument doc) {
        if (running.get()) {
            queue.offer(doc);
        }
    }

    @Override
    public Optional<CrawlDocument> findById(String id) {
        return repository.findById(id);
    }

    private void processQueue() {
        try {
            while (running.get() || !queue.isEmpty()) {
                CrawlDocument doc = queue.poll(500, TimeUnit.MILLISECONDS);
                if (doc != null) {
                    repository.save(doc);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void shutdown() {
        running.set(false);
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


