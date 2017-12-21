package com.itquasar.multiverse.sparkjava.routes;

import spark.ModelAndView;
import spark.TemplateEngine;

public class NotSoSimpleTemplateEngine extends TemplateEngine {

    private final String sufix;

    public NotSoSimpleTemplateEngine(String sufix) {
        this.sufix = sufix;
    }

    @Override
    public String render(ModelAndView modelAndView) {
        return modelAndView.getModel() + " " + modelAndView.getViewName() + "." + sufix;
    }
}
