package com.gabriel.backend.crawl.fetch;

import com.gabriel.backend.config.ConfigRegistry;
import com.gabriel.backend.config.FetchConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FetcherFactory {

    private static final Logger logger = Logger.getLogger(FetcherFactory.class.getName());
    private static final FetcherFactory INSTANCE = new FetcherFactory();

    private FetcherFactory() {
    }

    public static FetcherFactory getInstance() {
        return INSTANCE;
    }

    public Fetcher createFetcher(FetcherType fetcherType) {
        FetchConfig merged = ConfigRegistry.getInstance().getMergedConfig(FetchConfig.class);

        return switch (fetcherType) {
            case HTTP -> {
                HttpFetcher fetcher = new HttpFetcher();
                fetcher.setUserAgent(merged.getUserAgent());
                fetcher.setConnectionTimeoutMs(merged.getConnectionTimeoutMs());
                fetcher.setFetchTimeoutMs(merged.getFetchTimeoutMs());
                yield fetcher;
            }

            default -> {
                IllegalArgumentException illegalArgumentException =
                        new IllegalArgumentException("Fetcher type is not supported: " + fetcherType);
                logger.log(Level.SEVERE, "Fetch type requested : " + fetcherType, illegalArgumentException);
                throw illegalArgumentException;
            }
        };
    }

}
