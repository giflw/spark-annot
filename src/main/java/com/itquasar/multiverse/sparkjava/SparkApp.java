package com.itquasar.multiverse.sparkjava;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SparkApp {

    int port() default 4567;

    String ipAddress() default "0.0.0.0";

    /**
     * Defines the package to scan for routes and filters. Routes/filters can be placed in subpackages.
     * By default, uses the application class package.
     *
     * @return The upper package name containing routes/filters
     */
    String routesPackage() default "";

    boolean registerShutdownHook() default true;
}
