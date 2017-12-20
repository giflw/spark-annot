package com.itquasar.multiverse.sparkjava;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SparkRoute {

    HttpMethod method();

    String path();

    String acceptType() default "*/*";
}
