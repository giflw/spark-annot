package com.itquasar.multiverse.sparkjava.filters;

import com.itquasar.multiverse.sparkjava.Context;
import com.itquasar.multiverse.sparkjava.ContextAware;
import com.itquasar.multiverse.sparkjava.FilterWhen;
import com.itquasar.multiverse.sparkjava.SparkFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

@SparkFilter(when = FilterWhen.BEFORE)
public class LogRouteFilter implements Filter, ContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRouteFilter.class);

    private Context context;

    @Override
    public void handle(Request request, Response response) {
        LOGGER.info("Accesing route {} at {}", request.pathInfo(), LocalDateTime.now());
    }

    @Override
    public void inject(Context context) {
        this.context = context;
        System.out.println("CONTEXT " + context);
    }
}
