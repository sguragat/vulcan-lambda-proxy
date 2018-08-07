package com.vulcan.proxy;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.*;

import static com.vulcan.proxy.Routes.HttpMethod.*;

/**
 * Created by sg on 28/07/2018.
 */
public class Routes {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private final Map<HttpMethod, List<RouteTemplate>> routeTemplates = new HashMap<>();

    public Response handleRequest(Request request, Context context) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.getHttpMethod());
        List<RouteTemplate> routes = routeTemplates.get(httpMethod);
        for (RouteTemplate routeTemplate : routes) {
            if (!routeTemplate.requestMatches(request.getPath(), request.getPathParameters())) {
                continue; // check next route
            }
            // route found
            return routeTemplate.getRoute().handleRequest(request, context);
        }

        return RESOURCE_NOT_FOUND_RESPONSE;
    }

    private void register(HttpMethod httpMethod, String path, Route route) {
        List<RouteTemplate> routes = routeTemplates.get(httpMethod);
        if (routes == null) {
            routes = new ArrayList<>();
            routeTemplates.put(httpMethod, routes);
        }
        // add first root
        if (routes.size() == 0) {
            routes.add(new RouteTemplate(path, route));
            return;
        }
        // other routes exists, so check for duplicates
        for (RouteTemplate routeTemplate : new ArrayList<>(routes)) {
            if (routeTemplate.getPath().equals(path)) {
                throw new IllegalStateException(String.format("Duplicate routes not allowed ( route %s %s )", httpMethod, path));
            }
            routes.add(new RouteTemplate(path, route));
        }
    }

    private static final Response RESOURCE_NOT_FOUND_RESPONSE = Response.notFound().build();

    public enum HttpMethod {

        GET, POST, PUT, DELETE

    }

    public static class Builder {

        private Routes routes = new Routes();

        public Builder get(String path, Route route) {
            routes.register(GET, path, route);
            return this;
        }

        public Builder post(String path, Route route) {
            routes.register(POST, path, route);
            return this;
        }

        public Builder put(String path, Route route) {
            routes.register(PUT, path, route);
            return this;
        }

        public Builder delete(String path, Route route) {
            routes.register(DELETE, path, route);
            return this;
        }

        public Routes build() {
            return routes;
        }
    }
}
