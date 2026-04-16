package com.gabriel.backend.crawl.fetch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HttpFetcher implements Fetcher {

    private static final Logger logger = Logger.getLogger(HttpFetcher.class.getName());
    private String userAgent;
    private int connectionTimeoutMs;
    private int fetchTimeoutMs;

    public HttpFetcher() {
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getFetchTimeoutMs() {
        return fetchTimeoutMs;
    }

    public void setFetchTimeoutMs(int fetchTimeoutMs) {
        this.fetchTimeoutMs = fetchTimeoutMs;
    }
    @Override
    public Optional<String> fetch(URI uri) {
        return fetchWithLimit(uri, 0);
    }

    private Optional<String> fetchWithLimit(URI uri, int redirects) {
        // Limite de segurança para evitar loops infinitos (ex: 5 redirecionamentos)
        if (redirects > 5) {
            logger.log(Level.WARNING, "Limite de redirecionamentos excedido para {0}", uri);
            return Optional.empty();
        }

        HttpURLConnection conn = null;
        try {
            URL url = uri.toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setConnectTimeout(connectionTimeoutMs);
            conn.setReadTimeout(fetchTimeoutMs);
            conn.setInstanceFollowRedirects(true);

            int status = conn.getResponseCode();

            // Trata redirecionamentos manuais (importante para 301/308 e trocas de HTTP para HTTPS)
            if (status == 301 || status == 302 || status == 303 || status == 307 || status == 308) {
                String location = conn.getHeaderField("Location");
                if (location != null) {
                    // Resolve o local (caso seja relativo como "/index.html") baseado na URI atual
                    URI nextUri = uri.resolve(location);
                    return fetchWithLimit(nextUri, redirects + 1);
                }
            }

            if (status != HttpURLConnection.HTTP_OK) {
                return Optional.empty();
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                return Optional.of(reader.lines().collect(Collectors.joining("\n")));
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "Falha ao conectar em {0}: {1}", new Object[]{uri, e.getMessage()});
            return Optional.empty();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public FetcherType getType() {
        return FetcherType.HTTP;
    }

}
