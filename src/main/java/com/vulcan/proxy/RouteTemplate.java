package com.vulcan.proxy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sg on 28/07/2018.
 */
public class RouteTemplate {

    private final String path;

    private final Route route;

    private final Pattern pattern;

    private final String[] parameterNames;

    public RouteTemplate(String pathTemplate, Route r) {
        // must start with slash
        if (!pathTemplate.startsWith("/")) {
            throw new IllegalStateException("Invalid route path ( must start with '/' )");
        }
        // must not end with slash
        if (pathTemplate.endsWith("/")) {
            throw new IllegalStateException("Invalid route path ( must not end with '/' )");
        }
        path = pathTemplate;
        route = r;
        parameterNames = new String[getParametersCount(pathTemplate)];
        pattern = Pattern.compile(getPattern(pathTemplate, parameterNames));
    }

    public boolean requestMatches(String requestPath, Map<String, Object> pathParameters) {

        Matcher matcher = pattern.matcher(requestPath);
        if (!matcher.matches()) { // request path does not match route's template
            return false;
        }
        // extract path parameters
        for (String parameterName : parameterNames) {
            /**
             * In case {proxy+} was used in the API Gateway, then
             * pathParameters will be empty. Hence, here
             * we extract the parameter from current request path.
             */
            if (!pathParameters.containsKey(parameterName)) {
                String groupName = groupName(parameterName);
                String parameterValue = matcher.group(groupName);
                pathParameters.put(parameterName, parameterValue);
            }
        }
        return true;
    }

    public String getPath() {
        return path;
    }

    public Route getRoute() {
        return route;
    }

    public static int getParametersCount(String template) {
        int count = 0;
        int index = -1;
        while (((index = template.indexOf('{', ++index)) > -1)
                && (index = template.indexOf('}', ++index)) > -1) {
            count++;
        }
        return count;
    }

//    public static int getParametersCount(String template) {
//        int count = 0;
//        int index = -1;
//        while ((index = template.indexOf(':', ++index)) > -1) {
//            count++;
//        }
//        return count;
//    }
//
    public static String getPattern(String template, String[] parameterNames) {
        int paramIndex = 0;
        int openIndex = -1;
        int closeIndex = -1;
        StringBuilder pathPattern = new StringBuilder(template);
        while (((openIndex = template.indexOf('{', ++openIndex)) > -1)
                && (closeIndex = template.indexOf('}', ++closeIndex)) > -1) {
            String pathParameter = template.substring(openIndex + 1, closeIndex);
            parameterNames[paramIndex] = pathParameter; // remember parameter name

            // create group name within the pattern
            String groupName = groupName(pathParameter);
            pathPattern.replace(openIndex, closeIndex + 1, "(?<" + groupName + ">.+)");
        }

//        StringBuilder pathPattern = new StringBuilder(template);
//        int colonIndex;
//        while ((colonIndex = pathPattern.indexOf(":")) > -1) { // path has parameters
//            int slashIndex = pathPattern.indexOf("/", colonIndex);
//            if (slashIndex == -1) { // we hit the end of path
//                // slashIndex
//                slashIndex = pathPattern.length();
//            }
//            String pathParameter = pathPattern.substring(colonIndex, slashIndex);
//            parameterNames[paramIndex] = pathParameter; // remember parameter name
//
//            // create group name within the pattern
//            String groupName = groupName(pathParameter);
//            pathPattern.replace(colonIndex, slashIndex, "(?<" + groupName + ">.+)");
//        }
        return pathPattern.toString();
    }

//    public static String getPattern(String template, String[] parameterNames) {
//        StringBuilder pathPattern = new StringBuilder(template);
//        int colonIndex;
//        int paramIndex = 0;
//        while ((colonIndex = pathPattern.indexOf(":")) > -1) { // path has parameters
//            int slashIndex = pathPattern.indexOf("/", colonIndex);
//            if (slashIndex == -1) { // we hit the end of path
//                // slashIndex
//                slashIndex = pathPattern.length();
//            }
//            String pathParameter = pathPattern.substring(colonIndex, slashIndex);
//            parameterNames[paramIndex] = pathParameter; // remember parameter name
//
//            // create group name within the pattern
//            String groupName = groupName(pathParameter);
//            pathPattern.replace(colonIndex, slashIndex, "(?<" + groupName + ">.+)");
//        }
//        return pathPattern.toString();
//    }
//
    private static String groupName(String parameter) {
        return parameter
                .replace(":", "")
                .replace("_", "")
                .replace("-", "");
    }
}
