package com.itquasar.multiverse.sparkjava.routes;

import spark.ModelAndView;
import spark.TemplateEngine;

public class SimpleTemplateEngine extends TemplateEngine {

    @Override
    public String render(ModelAndView modelAndView) {
        return "<html><body><p>" + modelAndView.getModel() + "<p>" + modelAndView.getViewName();
    }
}
