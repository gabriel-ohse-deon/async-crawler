package com.gabriel.backend.crawl.rest;

import com.gabriel.backend.crawl.Crawler;
import com.gabriel.backend.crawl.CrawlerFactory;
import com.gabriel.backend.crawl.IdGenerator;
import com.gabriel.backend.crawl.action.SearcherActionType;
import com.gabriel.backend.crawl.event.CrawlEventManager;
import com.gabriel.backend.crawl.fetch.FetcherType;
import com.gabriel.backend.crawl.poller.PollerManager;
import com.gabriel.backend.crawl.rest.validation.*;
import com.gabriel.backend.repository.CrawlDocument;
import com.gabriel.backend.repository.CrawlRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Response;
import spark.Service;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlRestService {

    private static final Logger logger = Logger.getLogger(CrawlRestService.class.getName());

    private final Gson gson = new Gson();
    private final CrawlRepository repository;
    private final PollerManager poolManager;
    private final IdGenerator idGenerator;
    private final ConcurrentMap<String, Crawler> runningCrawls = new ConcurrentHashMap<>();
    private final Service http;

    public CrawlRestService(
            CrawlRepository repository,
            PollerManager pollerManager,
            IdGenerator idGenerator,
            Integer port
    ) {
        this.repository = repository;
        this.poolManager = pollerManager;
        this.idGenerator = idGenerator;
        if (port == null || port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }
        this.http = Service.ignite().port(port);
    }

    public void initRoutes() {
        http.post("/crawl", (req, res) -> handleStartCrawl(req.body(), res));
        http.get("/crawl/:id", (req, res) -> handleGetCrawl(req.params("id"), res));
        http.post("/crawl/:id/stop", (req, res) -> {
            stopCrawl(req.params("id"));
            res.status(200);
            return gson.toJson(Map.of("status", "stopping"));
        });
        logger.log(Level.INFO, "API REST started on http://localhost:{0}",  http.port());
    }

    private String handleStartCrawl(String body, Response res) {
        res.type("application/json");

        try {
            JsonObject json = gson.fromJson(body, JsonObject.class);
            String keyword = json.get("keyword").getAsString();
            String baseUrlStr = json.has("baseUrl") ? json.get("baseUrl").getAsString() : null;

            ValidationResult result = new RequestValidator()
                    .field("keyword", keyword, f -> f
                            .addValidator(new NullValidator<>("keyword"))
                            .addValidator(new NotEmptyValidator("keyword")))
                    .field("baseUrl", baseUrlStr, f -> f
                            .addValidator(new NullValidator<>("baseUrl"))
                            .addValidator(new UrlFormatValidator("baseUrl")))
                    .validate();

            if (!result.isValid()) {
                res.status(400);
                return gson.toJson(new ApiError("Invalid parameters", result.getErrors()));
            }

            URI baseUrl = URI.create(baseUrlStr);
            String crawlId = idGenerator.createId();

            CrawlEventManager eventManager = new CrawlEventManager(crawlId, poolManager);
            Crawler crawler = CrawlerFactory.getInstance().createCrawler(
                    crawlId,
                    FetcherType.HTTP,
                    eventManager,
                    SearcherActionType.CONTAINS_KEYWORD
            );

            runningCrawls.put(crawlId, crawler);

            CompletableFuture.runAsync(() -> crawler.start(keyword, baseUrl))
                    .whenComplete((v, t) -> runningCrawls.remove(crawlId));

            res.status(200);
            return gson.toJson(new CrawlResponse(crawlId, null, null));

        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ApiError("Internal error: " + e.getMessage(), null));
        }
    }

    private String handleGetCrawl(String id, Response res) {
        res.type("application/json");
        ValidationResult result = new RequestValidator()
                .field("id", id, f -> f
                        .addValidator(new NullValidator<>("id"))
                        .addValidator(new NotEmptyValidator("id"))
                        .addValidator(new AlphanumericValidator("id"))
                        .addValidator(new LengthBetweenValidator("id", 8, 8)))
                .validate();

        if (!result.isValid()) {
            res.status(404);
            return gson.toJson(new ApiError("Invalid id:" + id , result.getErrors()));
        }

        Optional<CrawlDocument> optDoc = repository.findById(id);

        if (optDoc.isEmpty()) {
            res.status(404);
            return gson.toJson(new ApiError("Crawl ID not found: " + id, null));
        }

        CrawlDocument doc = optDoc.get();
        CrawlResponse response = new CrawlResponse(
                doc.getCrawlId(),
                doc.getStatus().toString().toLowerCase(),
                doc.getFoundUris().stream().map(URI::toString).toList()
        );
        return gson.toJson(response);
    }

    public void stopCrawl(String crawlId) {
        Crawler crawler = runningCrawls.remove(crawlId);
        if (crawler != null) {
            crawler.stop();
            poolManager.shutdown(crawlId);
        }
    }

    public void stopAll() {
        runningCrawls.values().forEach(c -> {
            c.stop();
            poolManager.shutdown(c.getCrawlId());
        });
        runningCrawls.clear();
        http.stop();
    }
}


