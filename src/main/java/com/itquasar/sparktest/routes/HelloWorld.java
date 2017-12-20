package com.itquasar.sparktest.routes;

import com.itquasar.sparktest.HttpMethod;
import com.itquasar.sparktest.SparkRoute;
import spark.Request;
import spark.Response;
import spark.Route;

@SparkRoute(method = HttpMethod.GET, path = "/*")
public class HelloWorld implements Route {

    @Override
    public String handle(Request request, Response response) {
        response.type("text/plain");
        return "HelloWorld";
    }
}
