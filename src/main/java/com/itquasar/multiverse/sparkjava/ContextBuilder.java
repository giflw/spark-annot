package com.itquasar.multiverse.sparkjava;

import spark.TemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContextBuilder {

    private final SparkApplication application;
    private final Map<String, Object> properties;

    private ContextBuilder(SparkApplication application, Map<String, Object> properties) {
        this.application = application;
        this.properties = properties;
    }

    public static ContextBuilder of(SparkApplication application) {
        return new ContextBuilder(application, new HashMap<>());
    }

    public ContextBuilder addProperty(String name, Object value) {
        if (this.properties.containsKey(name)) {
            throw new IllegalArgumentException("PRoperty with name " + name + " is already added! Properties can be added only once!");
        }
        this.properties.put(name, value);
        return this;
    }

    public ContextBuilder addProperty(Class type, Object value) {
        return addProperty(type, null, value);
    }

    public ContextBuilder addProperty(Class type, String classifier, Object value) {
        this.addProperty(Context.typeToPropertyName(type, classifier), value);
        return this;
    }

    public ContextBuilder setDefaultTemplateEngine(TemplateEngine templateEngine) {
        this.addProperty(TemplateEngine.class, templateEngine);
        return this;
    }

    public Context build() {
        return new Context(this.application, Collections.unmodifiableMap(this.properties));
    }

}
