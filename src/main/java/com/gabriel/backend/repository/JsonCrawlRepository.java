package com.gabriel.backend.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class JsonCrawlRepository implements CrawlRepository {

    private final File baseDir;
    private final Gson gson;
    private final CrawlStoragePathProvider crawlStoragePathProvider;

    public JsonCrawlRepository() {
        this.crawlStoragePathProvider = new CrawlStoragePathProvider();
        this.baseDir = new File(crawlStoragePathProvider.getBasePath());
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public Optional<CrawlDocument> findById(String crawlId) {
        File file = fileFor(crawlId);
        if (!file.exists()) return Optional.empty();

        // Faz uma cópia temporária para leitura
        try {
            File tempCopy = File.createTempFile("crawl-read-", ".json");
            Files.copy(file.toPath(), tempCopy.toPath(), StandardCopyOption.REPLACE_EXISTING);

            try (FileReader reader = new FileReader(tempCopy)) {
                CrawlDocument doc = gson.fromJson(reader, CrawlDocument.class);
                return Optional.ofNullable(doc);
            } finally {
                tempCopy.delete();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void save(CrawlDocument data) {
        File file = fileFor(data.getCrawlId());
        File tmpFile = new File(file.getAbsolutePath() + ".tmp");

        try (FileWriter writer = new FileWriter(tmpFile)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Falha ao salvar CrawlDocument: " + e.getMessage());
            return;
        }

        try {
            Files.move(tmpFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            System.err.println("Falha ao substituir arquivo final: " + e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        // nada a fazer
    }

    private File fileFor(String crawlId) {
        return new File(baseDir, "crawl-" + crawlId + ".json");
    }
}



