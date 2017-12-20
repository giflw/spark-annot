package com.itquasar.multiverse.sparkjava;

import java.util.Map;
import java.util.Optional;

public class Context {

    private final SparkApplication application;
    private final Map<String, Object> properties;
    private final Map<Class, Object> instances;

    public Context(SparkApplication application, Map<String, Object> properties, Map<Class, Object> instances) {
        this.application = application;
        this.properties = properties;
        this.instances = instances;
    }

    public SparkApplication application() {
        return this.application;
    }

    public <T> Optional<T> instance(Class<T> type) {
        return Optional.ofNullable((T) instances.get(type));
    }

    public <T> T instance(Class<T> type, T defaultValue) {
        return instance(type).orElse(defaultValue);
    }

    public <T> Optional<T> property(String name, Class<T> type) {
        Object o = properties.get(name);
        return o == null ? Optional.empty() : Optional.of((T) o);
    }

    public <T> T property(String name, T defaultValue) {
        return property(name, (Class<T>) defaultValue.getClass()).orElse(defaultValue);
    }
}
