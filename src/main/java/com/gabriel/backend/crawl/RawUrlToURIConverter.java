package com.gabriel.backend.crawl;

import java.net.URI;
import java.util.Optional;

public class RawUrlToURIConverter {

    public Optional<URI> convert(URI baseUri, String rawUrl) {
        try {
            System.out.println("Base uri: " + baseUri + " Raw: " + rawUrl);
            URI resolved = baseUri.resolve(rawUrl);
            System.out.println("Resolved : " + resolved);
            return Optional.of(resolved);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<URI> convert(String rawUrl) {
        try {
            URI uri = URI.create(rawUrl);
            return Optional.of(uri);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
