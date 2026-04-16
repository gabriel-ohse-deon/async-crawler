package com.gabriel.backend.config;

public class PollerConfig {
    private Integer flushIntervalMs;
    private Integer maxPendingUpdates;

    public PollerConfig() {
    }

    public Integer getFlushIntervalMs() {
        return flushIntervalMs;
    }

    public void setFlushIntervalMs(Integer flushIntervalMs) {
        this.flushIntervalMs = flushIntervalMs;
    }

    public Integer getMaxPendingUpdates() {
        return maxPendingUpdates;
    }

    public void setMaxPendingUpdates(Integer maxPendingUpdates) {
        this.maxPendingUpdates = maxPendingUpdates;
    }

}
