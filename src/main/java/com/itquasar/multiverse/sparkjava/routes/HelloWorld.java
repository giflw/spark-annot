package com.itquasar.multiverse.sparkjava.routes;

import com.itquasar.multiverse.sparkjava.HttpMethod;
import com.itquasar.multiverse.sparkjava.SparkRoute;
import spark.Request;
import spark.Response;
import spark.Route;

@SparkRoute(method = HttpMethod.GET, path = "/*", acceptType = "*/*")
public class HelloWorld implements Route {

    @Override
    public String handle(Request request, Response response) {
        response.type("text/plain");
        return "HelloWorld";
    }
}
