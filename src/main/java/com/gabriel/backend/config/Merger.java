package com.gabriel.backend.config;

import java.util.List;

public interface Merger<T> {
    Class<T> getMergeClass();
    T merge(T primary, T fallback) throws ConfigLoadException;
    T mergeSequential(List<T> configs) throws ConfigLoadException;
    void validateNoNulls(T config) throws ConfigLoadException;
}

