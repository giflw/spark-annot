package com.itquasar.sparktest;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SparkRoute {

    HttpMethod method();

    String path();
}
