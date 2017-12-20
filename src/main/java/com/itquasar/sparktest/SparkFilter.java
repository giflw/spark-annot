package com.itquasar.sparktest;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SparkFilter {

    FilterWhen when();

    String acceptType() default "*/*";

    String path() default "+/*paths";
}
