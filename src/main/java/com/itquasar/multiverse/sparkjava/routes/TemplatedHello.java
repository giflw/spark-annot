package com.itquasar.multiverse.sparkjava.routes;

import com.itquasar.multiverse.sparkjava.HttpMethod;
import com.itquasar.multiverse.sparkjava.SparkRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;

@SparkRoute(method = HttpMethod.GET, path = "/hello/tmpl", acceptType = "*/*", engine = SimpleTemplateEngine.class)
public class TemplatedHello implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new ModelAndView("Hello World", "hello-world.template");
    }
}
