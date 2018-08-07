package com.vulcan.proxy;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sg on 28/07/2018.
 */
public class RouteTemplateTest {

    @Test
    public void getPattern() {
        String[] parameterNames = new String[0];
        Assert.assertEquals("/product", RouteTemplate.getPattern("/product", parameterNames));
        Assert.assertEquals(0, parameterNames.length);

        parameterNames = new String[0];
        Assert.assertEquals("/product/", RouteTemplate.getPattern("/product/", parameterNames));
        Assert.assertEquals(0, parameterNames.length);

        parameterNames = new String[1];
        Assert.assertEquals("/product/(?<id>.+)", RouteTemplate.getPattern("/product/{id}", parameterNames));
        Assert.assertEquals(1, parameterNames.length);
        Assert.assertEquals("id", parameterNames[0]);

        parameterNames = new String[1];
        Assert.assertEquals("/product/(?<productid>.+)", RouteTemplate.getPattern("/product/{product-id}", parameterNames));
        Assert.assertEquals(1, parameterNames.length);
        Assert.assertEquals("product-id", parameterNames[0]);

        parameterNames = new String[1];
        Assert.assertEquals("/product/(?<productid>.+)", RouteTemplate.getPattern("/product/{product_id}", parameterNames));
        Assert.assertEquals(1, parameterNames.length);
        Assert.assertEquals("product_id", parameterNames[0]);
//        String[] parameterNames = new String[0];
//        Assert.assertEquals("/product", RouteTemplate.getPattern("/product", parameterNames));
//        Assert.assertEquals(0, parameterNames.length);
//
//        parameterNames = new String[0];
//        Assert.assertEquals("/product/", RouteTemplate.getPattern("/product/", parameterNames));
//        Assert.assertEquals(0, parameterNames.length);
//
//        parameterNames = new String[1];
//        Assert.assertEquals("/product/(?<id>.+)", RouteTemplate.getPattern("/product/:id", parameterNames));
//        Assert.assertEquals(1, parameterNames.length);
//        Assert.assertEquals(":id", parameterNames[0]);
//
//        parameterNames = new String[1];
//        Assert.assertEquals("/product/(?<productid>.+)", RouteTemplate.getPattern("/product/:product-id", parameterNames));
//        Assert.assertEquals(1, parameterNames.length);
//        Assert.assertEquals(":product-id", parameterNames[0]);
//
//        parameterNames = new String[1];
//        Assert.assertEquals("/product/(?<productid>.+)", RouteTemplate.getPattern("/product/:product_id", parameterNames));
//        Assert.assertEquals(1, parameterNames.length);
//        Assert.assertEquals(":product_id", parameterNames[0]);
    }

    @Test
    public void requestMatches_when_path_parameter_resolved_by_aws() {
        RouteTemplate routeTemplate = new RouteTemplate("/product/{product-id}", null);
        Map<String, Object> pathParameters = new HashMap<>();
        pathParameters.put("product-id", "12345678"); // path parameter resolved by AWS

        // call method under test
        boolean matches = routeTemplate.requestMatches("/product/12345678", pathParameters);

        // assert
        Assert.assertEquals(true, matches);
        Assert.assertEquals(1, pathParameters.size());
        Assert.assertEquals("12345678", pathParameters.get("product-id"));
    }

    @Test
    public void requestMatches_when_greedy_proxy_used() {
        RouteTemplate routeTemplate = new RouteTemplate("/product/{product-id}", null);
        // in case of greedy {proxy+} the path parameters remains empty
        Map<String, Object> pathParameters = new HashMap<>();

        // call method under test
        boolean matches = routeTemplate.requestMatches("/product/12345678", pathParameters);

        // assert
        Assert.assertEquals(true, matches);
        Assert.assertEquals(1, pathParameters.size());
        Assert.assertEquals("12345678", pathParameters.get("product-id"));
    }

    @Test
    public void getParametersCount() {
        Assert.assertEquals(0, RouteTemplate.getParametersCount("/product"));
        Assert.assertEquals(1, RouteTemplate.getParametersCount("/product/{product-id}"));
        Assert.assertEquals(1, RouteTemplate.getParametersCount("/{product-id}"));
        Assert.assertEquals(2, RouteTemplate.getParametersCount("/product/{product-id}/{sub-product-id}"));
        Assert.assertEquals(2, RouteTemplate.getParametersCount("/product/{product-id}/sub-product/{sub-product-id}"));
//        Assert.assertEquals(0, RouteTemplate.getParametersCount("/product"));
//        Assert.assertEquals(1, RouteTemplate.getParametersCount("/product/:product-id"));
//        Assert.assertEquals(1, RouteTemplate.getParametersCount("/:product-id"));
//        Assert.assertEquals(2, RouteTemplate.getParametersCount("/product/:product-id/:sub-product-id"));
//        Assert.assertEquals(2, RouteTemplate.getParametersCount("/product/:product-id/sub-product/:sub-product-id"));
    }
}
