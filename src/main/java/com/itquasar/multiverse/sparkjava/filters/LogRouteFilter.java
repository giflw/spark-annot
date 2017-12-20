package com.itquasar.multiverse.sparkjava.filters;

import com.itquasar.multiverse.sparkjava.FilterWhen;
import com.itquasar.multiverse.sparkjava.SparkFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

@SparkFilter(when = FilterWhen.BEFORE)
public class LogRouteFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRouteFilter.class);

    @Override
    public void handle(Request request, Response response) {
        LOGGER.info("Accesing route {} at {}", request.pathInfo(), LocalDateTime.now());
    }
}
