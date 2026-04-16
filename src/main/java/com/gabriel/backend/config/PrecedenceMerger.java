package com.gabriel.backend.config;

import java.lang.reflect.Field;

import java.util.List;

public abstract class PrecedenceMerger<T> implements Merger<T> {

    @Override
    public T merge(T primary, T fallback) throws ConfigLoadException {
        if (primary == null) return fallback;
        if (fallback == null) return primary;

        try {
            Class<?> cls = primary.getClass();
            while (cls != null && cls != Object.class) {
                for (Field field : cls.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;

                    Object primaryVal = field.get(primary);
                    Object fallbackVal = field.get(fallback);

                    if (primaryVal == null && fallbackVal != null) {
                        field.set(primary, fallbackVal);
                    }
                }
                cls = cls.getSuperclass();
            }
            return primary;
        } catch (Exception e) {
            throw new ConfigLoadException("Error merging configs: " + e.getMessage(), e);
        }
    }

    @Override
    public T mergeSequential(List<T> configs) throws ConfigLoadException {
        if (configs == null || configs.isEmpty()) throw new ConfigLoadException("Config is null");
        T result = null;
        for (T cfg : configs) {
            if (cfg == null) continue;
            if (result == null) result = cfg;
            else result = merge(result, cfg);
        }
        return result;
    }

    @Override
    public void validateNoNulls(T config) throws ConfigLoadException {
        if (config == null) throw new ConfigLoadException("Config is null");

        Class<?> cls = config.getClass();
        while (cls != null && cls != Object.class) {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);

                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
                if (field.getType().isPrimitive()) continue;

                try {
                    if (field.get(config) == null) {
                        throw new ConfigLoadException("Missing config attribute: " + field.getName());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            cls = cls.getSuperclass();
        }
    }
}


