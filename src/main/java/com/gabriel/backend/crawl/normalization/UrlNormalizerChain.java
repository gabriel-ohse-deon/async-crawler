package com.gabriel.backend.crawl.normalization;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

public class UrlNormalizerChain {

    private final List<UrlNormalizationStep> steps;

    public UrlNormalizerChain() {
        ServiceLoader<UrlNormalizationStep> loader = ServiceLoader.load(UrlNormalizationStep.class);
        steps = new ArrayList<>();
        loader.iterator().forEachRemaining(steps::add);

        steps.sort(Comparator.comparingInt(UrlNormalizationStep::getOrder));
    }

    public URI normalize(URI uri) {
        for (UrlNormalizationStep step : steps) {
            uri = step.apply(uri);
        }
        return uri;
    }

}
