package com.itquasar.multiverse.sparkjava.routes;

import com.itquasar.multiverse.sparkjava.HttpMethod;
import com.itquasar.multiverse.sparkjava.SparkRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;

@SparkRoute(method = HttpMethod.GET, path = "/hello/html", acceptType = "*/*")
public class TemplatedHello implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
        return new ModelAndView(
                new HashMap<String, String>() {{
                    put("msg", "Hello World");
                }}, "hello-world.hbs"
        );
    }
}
