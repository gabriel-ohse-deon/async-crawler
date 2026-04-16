package com.gabriel.backend.config;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConfigRegistry {

    private static final Logger logger = Logger.getLogger(ConfigRegistry.class.getName());
    private static final ConfigRegistry INSTANCE = new ConfigRegistry();

    private final Map<Class<?>, List<ConfigProvider<?>>> providersRegistry;
    private final Map<Class<?>, Merger<?>> mergersRegistry;
    private final Map<Class<?>, Object> mergedConfigs;

    private volatile boolean initialized;

    private ConfigRegistry() {
        providersRegistry = new HashMap<>();
        mergersRegistry = new HashMap<>();
        mergedConfigs = new HashMap<>();
        initialized = false;
    }

    public static ConfigRegistry getInstance() {
        return INSTANCE;
    }

    private void initialize(ConfigSourceOrder order) throws ConfigLoadException {
        if (initialized) return;

        loadProvidersFromServiceLoader();
        loadMergersFromServiceLoader();
        mergeAllConfigs(order);

        initialized = true;
    }

    public void initializeOrExit(ConfigSourceOrder order) {
        try {
            initialize(order);
        } catch (ConfigLoadException e) {
            logger.log(Level.SEVERE, "Critical error: ", e);
            System.exit(1);
        }
    }

    public synchronized void resetForTests() {
        providersRegistry.clear();
        mergersRegistry.clear();
        mergedConfigs.clear();
        initialized = false;
    }

    private void loadProvidersFromServiceLoader() {
        ServiceLoader.load(ConfigProvider.class).forEach(provider -> registerProvider(provider));
    }

    private void loadMergersFromServiceLoader() {
        ServiceLoader.load(Merger.class).forEach(merger -> mergersRegistry.put(merger.getMergeClass(), merger));
    }

    public <T> void registerProvider(ConfigProvider<T> provider) {
        providersRegistry
                .computeIfAbsent(provider.getConfigClass(), k -> new ArrayList<>())
                .add(provider);
    }

    public <T> void registerMerger(Merger<T> merger) {
        mergersRegistry.put(merger.getMergeClass(), merger);
    }

    public void mergeAllConfigs(ConfigSourceOrder order) throws ConfigLoadException {
        for (Class<?> configClass : providersRegistry.keySet()) {
            mergeConfig(configClass, order);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void mergeConfig(Class<T> configClass, ConfigSourceOrder order) throws ConfigLoadException {
        List<ConfigProvider<?>> rawProviders = providersRegistry.get(configClass);
        if (rawProviders == null || rawProviders.isEmpty()) return;

        List<ConfigProvider<?>> providers = new ArrayList<>(rawProviders);
        providers.sort(order.asComparator());

        List<T> orderedConfigs = new ArrayList<>();
        for (ConfigProvider<?> p : providers) {
            Optional<?> loaded = p.loadConfig();
            loaded.ifPresent(obj -> orderedConfigs.add((T) obj));
        }

        if (orderedConfigs.isEmpty()) return;

        Merger<T> merger = (Merger<T>) mergersRegistry.get(configClass);
        if (merger == null) {
            throw new ConfigLoadException("There is no Merger assigned to: " + configClass.getSimpleName());
        }

        T merged = merger.mergeSequential(orderedConfigs);
        merger.validateNoNulls(merged);

        mergedConfigs.put(configClass, merged);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMergedConfig(Class<T> configClass) {
        return (T) mergedConfigs.get(configClass);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<Merger<T>> getMerger(Class<T> configClass) {
        return Optional.ofNullable((Merger<T>) mergersRegistry.get(configClass));
    }

    @SuppressWarnings("unchecked")
    public <T> List<ConfigProvider<T>> getProviders(Class<T> configClass) {
        return (List<ConfigProvider<T>>) (List<?>) providersRegistry.getOrDefault(configClass, List.of());
    }
}


