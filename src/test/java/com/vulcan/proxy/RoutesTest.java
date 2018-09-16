package com.vulcan.proxy;

import org.junit.Test;

/**
 * Created by sg on 31/07/2018.
 */
public class RoutesTest {

    @Test
    public void builder_with_single_route() {
        new Routes.Builder()
                .get("/product", new DummyRoute())
                .build();
    }

    @Test
    public void builder_with_many_routes_of_same_method() {
        new Routes.Builder()
                .get("/product/collection", new DummyRoute())
                .get("/product/{id}", new DummyRoute())
                .build();
    }

    @Test
    public void builder_with_many_routes_of_different_methods() {
        new Routes.Builder()
                .post("/product", new DummyRoute())
                .get("/product/{id}", new DummyRoute())
                .build();
    }
}
