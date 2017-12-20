package com.itquasar.multiverse.sparkjava;

import spark.ResponseTransformer;
import spark.TemplateEngine;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SparkRoute {

    HttpMethod method();

    String path();

    String acceptType() default "*/*";

    Class<? extends ResponseTransformer> transformer() default ResponseTransformer.class;

    Class<? extends TemplateEngine> engine() default TemplateEngine.class;
}
