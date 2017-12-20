package com.itquasar.sparktest.filters;

import com.itquasar.sparktest.FilterWhen;
import com.itquasar.sparktest.SparkFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

@SparkFilter(when = FilterWhen.BEFORE)
public class LogRouteFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRouteFilter.class);

    @Override
    public void handle(Request request, Response response) throws Exception {
        LOGGER.info("Accesing route " + request.pathInfo());
    }
}
