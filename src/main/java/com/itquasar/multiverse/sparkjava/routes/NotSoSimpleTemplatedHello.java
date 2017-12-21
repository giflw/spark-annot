package com.itquasar.multiverse.sparkjava.routes;

import com.itquasar.multiverse.sparkjava.HttpMethod;
import com.itquasar.multiverse.sparkjava.SparkRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

@SparkRoute(method = HttpMethod.GET, path = "/hello/tmpl2", acceptType = "*/*", engine = NotSoSimpleTemplateEngine.class)
public class NotSoSimpleTemplatedHello implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new ModelAndView("Hello", "World");
    }
}
