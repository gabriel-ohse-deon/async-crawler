package com.gabriel.backend.config;

import java.util.Comparator;
import java.util.List;

public enum ConfigSourceOrder {
    ENV_THEN_ARQ(List.of(ConfigSourceType.ENV, ConfigSourceType.JSON_FILE)),
    ARQ_THEN_ENV(List.of(ConfigSourceType.JSON_FILE, ConfigSourceType.ENV));


    private final List<ConfigSourceType> order;


    ConfigSourceOrder(List<ConfigSourceType> order) {
        this.order = order;
    }


    public int compare(ConfigSourceType a, ConfigSourceType b) {
        return Integer.compare(order.indexOf(a), order.indexOf(b));
    }


    public Comparator<ConfigProvider<?>> asComparator() {
        return Comparator.comparingInt(p -> order.indexOf(p.getSourceType()));
    }
}

