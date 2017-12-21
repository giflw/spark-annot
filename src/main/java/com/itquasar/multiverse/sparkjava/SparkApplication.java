package com.itquasar.multiverse.sparkjava;

import com.itquasar.multiverse.sparkjava.routes.NotSoSimpleTemplateEngine;
import com.itquasar.multiverse.sparkjava.util.Pair;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SparkApp(port = 4567, ipAddress = "0.0.0.0", routesPackage = "", registerShutdownHook = true)
public class SparkApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkApplication.class);

    private final TemplateEngine templateEngine;

    private SparkApplication() {
        this(null);
    }

    public SparkApplication(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public static void main(String[] args) {
        SparkApplication.launch();
    }

    public static SparkApplication launch(){
        return launch(SparkApplication.class);
    }

    public static SparkApplication launch(Class<SparkApplication> sparkAppClass) {
        try {
            SparkApplication app = sparkAppClass.newInstance();
            SparkApp annotation = sparkAppClass.getAnnotation(SparkApp.class);
            app.config(annotation);
            app.start(annotation);
            if (annotation.registerShutdownHook()) {
                LOGGER.warn("Registering shutdown hook");
                Runtime.getRuntime().addShutdownHook(new Thread(() -> app.stop()));
            } else {
                LOGGER.warn("Shutdown hook NOT registered");
            }
            return app;
        } catch (Exception ex){
            Spark.stop();
            throw new Error("Exception during application launch", ex);
        }
    }

    protected Context context() {
        return ContextBuilder.of(this).setDefaultTemplateEngine(new NotSoSimpleTemplateEngine("notSoSimple")).build();
    }

    private final void registerRoutes(Reflections reflections, Context context) {
        Set<Class<? extends Route>> routes = reflections.getSubTypesOf(Route.class);
        for (Class<? extends Route> route : routes) {
            LOGGER.warn("Found route {}", route);
            SparkRoute routeMeta = route.getAnnotation(SparkRoute.class);
            if (routeMeta == null) {
                LOGGER.error("Route {} wont be registered as dont have {} annotation", route, SparkRoute.class.getCanonicalName());
            } else {
                try {
                    Route routeInstance = route.newInstance();
                    this.injectContext(routeInstance, context);

                    // Spark.get(path, accecptType, route, responseTransformer);
                    List<Pair<Class, Object>> parameters = new LinkedList<Pair<Class, Object>>() {{
                        add(Pair.of(String.class, routeMeta.path()));
                        add(Pair.of(String.class, routeMeta.acceptType()));
                        add(Pair.of(Route.class, routeInstance));
                        if (!routeMeta.transformer().equals(ResponseTransformer.class)) {
                            add(Pair.of(ResponseTransformer.class, routeMeta.transformer().newInstance()));
                        }
                    }};
                    Method function = Spark.class.getDeclaredMethod(
                            routeMeta.method().name().toLowerCase(),
                            parameters.stream().map(Pair::getFirst).collect(Collectors.toList()).toArray(new Class[0])
                    );
                    function.invoke(null, parameters.stream().map(Pair::getSecond).toArray());
                    LOGGER.warn("Route regitered: {} {} [{}]", routeMeta.method(), routeMeta.path(), route);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Error registering route " + route.getCanonicalName(), e);
                }
            }
        }
    }

    private final void registerTemplateAndViews(Reflections reflections, Context context) {
        Set<Class<? extends TemplateViewRoute>> routes = reflections.getSubTypesOf(TemplateViewRoute.class);
        for (Class<? extends TemplateViewRoute> route : routes) {
            LOGGER.warn("Found template and view route {}", route);
            SparkRoute routeMeta = route.getAnnotation(SparkRoute.class);
            if (routeMeta == null) {
                LOGGER.error("Template and view route {} wont be registered as dont have {} annotation", route, SparkRoute.class.getCanonicalName());
            } else {
                try {
                    TemplateViewRoute routeInstance = route.newInstance();
                    this.injectContext(routeInstance, context);

                    // Spark.get(path, acccepType, TEmpateViewRoute, TemplateEngine);
                    List<Pair<Class, Object>> parameters = new LinkedList<Pair<Class, Object>>() {{
                        add(Pair.of(String.class, routeMeta.path()));
                        add(Pair.of(String.class, routeMeta.acceptType()));
                        add(Pair.of(TemplateViewRoute.class, routeInstance));
                        if (!routeMeta.engine().equals(TemplateEngine.class)) {
                            Optional<? extends TemplateEngine> engine = context.property(routeMeta.engine());
                            if (!engine.isPresent()) {
                                engine = context.property(TemplateEngine.class);
                            }
                            add(Pair.of(TemplateEngine.class, engine.isPresent() ? engine.get() : routeMeta.engine().newInstance()));
                        } else {
                            Set<Class<? extends TemplateEngine>> engines = reflections.getSubTypesOf(TemplateEngine.class);
                            engines = !engines.isEmpty() ? engines : new Reflections("spark").getSubTypesOf(TemplateEngine.class);
                            if (engines.size() == 1) {
                                add(Pair.of(TemplateEngine.class, engines.iterator().next().newInstance()));
                            } else {
                                throw new IllegalArgumentException(
                                        "Found " + engines.size() +
                                                " template engines. Please specify wich engine to use or put just one in the path"
                                );
                            }
                        }
                    }};
                    Method function = Spark.class.getDeclaredMethod(
                            routeMeta.method().name().toLowerCase(),
                            parameters.stream().map(Pair::getFirst).collect(Collectors.toList()).toArray(new Class[0])
                    );
                    function.invoke(null, parameters.stream().map(Pair::getSecond).toArray());
                    LOGGER.warn("Template and view route regitered: {} {} [{}]", routeMeta.method(), routeMeta.path(), route);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Error registering template and view route " + route.getCanonicalName(), e);
                }
            }
        }
    }

    private final void registerFilters(Reflections reflections, Context context) {
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
                    Filter filterInstance = filter.newInstance();
                    this.injectContext(filterInstance, context);
                    function.invoke(null, filterMeta.path(), filterMeta.acceptType(), new Filter[]{filterInstance});
                    LOGGER.warn("Filter regitered: {} {} [{}]", filterMeta.when(), filterMeta.path(), filter);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Error registering route " + filter.getCanonicalName(), e);
                }
            }
        }
    }

    private final void injectContext(Object instance, Context context) {
        if (ContextAware.class.isInstance(instance)) {
            ((ContextAware) instance).inject(context);
        }
    }

    public void config(SparkApp sparkApp) {
        LOGGER.warn("Configuring app with {}.", sparkApp);
        Spark.port(sparkApp.port());
        Spark.ipAddress(sparkApp.ipAddress());
        LOGGER.warn("App configured");
    }

    public void start(SparkApp sparkApp) {
        LOGGER.warn("Starting app with {}.", sparkApp);
        String prefix = sparkApp.routesPackage().isEmpty()
                ? this.getClass().getPackage().getName()
                : sparkApp.routesPackage();

        LOGGER.info("Using [{}] as upper package for routes and filters", prefix);
        {
            Reflections reflections = new Reflections(prefix);
            Context context = context();
            this.registerFilters(reflections, context);
            this.registerRoutes(reflections, context);
            this.registerTemplateAndViews(reflections, context);
        }
        LOGGER.warn("App started");
    }

    public void stop() {
        LOGGER.warn("Stoping app");
        Spark.stop();
        LOGGER.warn("App stopped");
    }

}
