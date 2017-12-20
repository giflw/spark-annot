package com.itquasar.multiverse.sparkjava;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContextBuilder {

    private final SparkApplication application;
    private final Map<String, Object> properties;
    private final Map<Class, Object> instances;

    private ContextBuilder(SparkApplication application, Map<String, Object> properties, Map<Class, Object> instances) {
        this.application = application;
        this.properties = properties;
        this.instances = instances;
    }

    public static ContextBuilder of(SparkApplication application) {
        return new ContextBuilder(application, new HashMap<>(), new HashMap<>());
    }

    public ContextBuilder addProperty(String name, Object value) {
        this.properties.put(name, value);
        return this;
    }

    public <T> ContextBuilder addInstance(T object) {
        this.instances.put(object.getClass(), object);
        return this;
    }

    public <T, S extends T> ContextBuilder addInstance(Class<T> type, S object) {
        this.instances.put(type, object);
        return this;
    }

    public Context build() {
        return new Context(this.application,
                Collections.unmodifiableMap(this.properties), Collections.unmodifiableMap(this.instances)
        );
    }

}
