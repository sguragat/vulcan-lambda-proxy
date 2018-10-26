package com.vulcan.proxy;

import java.util.*;

import static com.vulcan.proxy.Routes.HttpMethod.*;

/**
 * Created by sg on 28/07/2018.
 */
public class Routes {

    private final Set<String> UNIQUE_ENDPOINTS = new HashSet<>();

    private final Map<HttpMethod, List<RouteTemplate>> routeTemplates = new HashMap<HttpMethod, List<RouteTemplate>>() {{
        put(HttpMethod.POST, new LinkedList<>());
        put(HttpMethod.PUT, new LinkedList<>());
        put(HttpMethod.GET, new LinkedList<>());
        put(HttpMethod.DELETE, new LinkedList<>());
    }};

    public Response handleRequest(Context context) {
        Request request = context.getRequest();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getHttpMethod());
        List<RouteTemplate> routes = routeTemplates.get(httpMethod);
        for (RouteTemplate routeTemplate : routes) {
            if (!routeTemplate.requestMatches(request.getPath(), request.getPathParameters())) {
                continue; // check next route
            }
            // route found
            return routeTemplate.getRoute().handleRequest(context);
        }

        return RESOURCE_NOT_FOUND_RESPONSE;
    }

    private void register(HttpMethod httpMethod, String path, Route route) {
        String endpoint = httpMethod.name() + path;
        if (UNIQUE_ENDPOINTS.contains(endpoint)) {
            throw new IllegalStateException(String.format("Duplicate route detected %s %s", httpMethod, path));
        }

        List<RouteTemplate> routes = routeTemplates.get(httpMethod);
        routes.add(new RouteTemplate(path, route));
        UNIQUE_ENDPOINTS.add(endpoint);
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
