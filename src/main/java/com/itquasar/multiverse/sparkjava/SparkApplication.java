package com.itquasar.multiverse.sparkjava;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Route;
import spark.Spark;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@SparkApp(port = 4567, ipAddress = "0.0.0.0", routesPackage = "", registerShutdownHook = true)
public class SparkApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkApplication.class);

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        SparkApplication.launch(SparkApplication.class);
    }

    private static void launch(Class<SparkApplication> sparkAppClass) throws IllegalAccessException, InstantiationException {
        SparkApplication app = sparkAppClass.newInstance();
        SparkApp annotation = sparkAppClass.getAnnotation(SparkApp.class);
        app.start(annotation);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> app.stop()));
    }

    protected void registerRoutes(Reflections reflections) {
        Set<Class<? extends Route>> routes = reflections.getSubTypesOf(Route.class);
        for (Class<? extends Route> route : routes) {
            LOGGER.warn("Found route {}", route);
            SparkRoute routeMeta = route.getAnnotation(SparkRoute.class);
            if (routeMeta == null) {
                LOGGER.error("Route {} wont be registered as dont have {} annotation", route, SparkRoute.class.getCanonicalName());
            } else {
                try {

                    Spark.before();
                    //Spark.get(path, accecptType, route);
                    Method function = Spark.class.getDeclaredMethod(
                            routeMeta.method().name().toLowerCase(),
                            String.class, String.class, Route.class
                    );
                    function.invoke(null, routeMeta.path(), routeMeta.acceptType(), route.newInstance());
                    LOGGER.warn("Route regitered: {} {} [{}]", routeMeta.method(), routeMeta.path(), route);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    LOGGER.error("Error registering route {}", route, e);
                }
            }
        }
    }

    protected void registerFilters(Reflections reflections) {
        Set<Class<? extends Filter>> filters = reflections.getSubTypesOf(Filter.class);
        for (Class<? extends Filter> filter : filters) {
            LOGGER.warn("Found filter {}", filter);
            SparkFilter filterMeta = filter.getAnnotation(SparkFilter.class);
            if (filterMeta == null) {
                LOGGER.error("Filter {} wont be registered as dont have {} annotation", filter, SparkFilter.class.getCanonicalName());
            } else {
                try {
                    // Spark.before(path, acceptType, filters...);
                    Method function = Spark.class.getDeclaredMethod(
                            filterMeta.when().name().toLowerCase(),
                            String.class, String.class, Filter[].class
                    );
                    function.invoke(null, filterMeta.path(), filterMeta.acceptType(), new Filter[]{filter.newInstance()});
                    LOGGER.warn("Filter regitered: {} {} [{}]", filterMeta.when(), filterMeta.path(), filter);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    LOGGER.error("Error registering route {}", filter, e);
                }
            }
        }
    }

    public void start(SparkApp sparkApp) {
        LOGGER.warn("Starting app with {}.", sparkApp);
        Spark.port(sparkApp.port());
        Spark.ipAddress(sparkApp.ipAddress());

        String prefix = sparkApp.routesPackage().isEmpty()
                ? this.getClass().getPackage().getName()
                : sparkApp.routesPackage();

        LOGGER.info("Using [{}] as upper package for routes and filters", prefix);
        {
            Reflections reflections = new Reflections(prefix);
            this.registerFilters(reflections);
            this.registerRoutes(reflections);
        }

        LOGGER.warn("App started");
    }

    public void stop() {
        LOGGER.warn("Stoping app");
        Spark.stop();
        LOGGER.warn("App stopped");
    }
}
