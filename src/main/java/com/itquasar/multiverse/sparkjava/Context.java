package com.itquasar.multiverse.sparkjava;

import java.util.Map;
import java.util.Optional;

public class Context {

    private final SparkApplication application;
    private final Map<String, Object> properties;

    public Context(SparkApplication application, Map<String, Object> properties) {
        this.application = application;
        this.properties = properties;
    }

    public static <T> String typeToPropertyName(Class<T> type) {
        return typeToPropertyName(type, null);
    }

    public static <T> String typeToPropertyName(Class<T> type, String classifier) {
        classifier = classifier == null ? "" : classifier;
        return type.getCanonicalName() + (classifier.isEmpty() ? "" : "_" + classifier);
    }

    public SparkApplication application() {
        return this.application;
    }

    public <T> Optional<T> property(String name, Class<T> type) {
        return Optional.ofNullable(type.cast(properties.get(name)));
    }

    public <T> T property(String name, T defaultValue) {
        return property(name, (Class<T>) defaultValue.getClass()).orElse(defaultValue);
    }

    public <T> Optional<T> property(Class<T> type) {
        return property(typeToPropertyName(type), type);
    }

    public <T> Optional<T> property(Class<T> type, String classifier) {
        return property(typeToPropertyName(type, classifier), type);
    }

    public <T> T property(Class<T> type, T defaultValue) {
        return property(typeToPropertyName(type), defaultValue);
    }

    public <T> T property(Class<T> type, String classifier, T defaultValue) {
        return property(typeToPropertyName(type, classifier), defaultValue);
    }
}
