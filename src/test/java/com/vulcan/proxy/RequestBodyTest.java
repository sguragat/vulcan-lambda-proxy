package com.vulcan.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by sg on 02/08/2018.
 */
public class RequestBodyTest {

    @Test
    public void getX3WFormEntity_with_1_key() {
        Request request = new Request();
        request.setBody("username=johndoe");

        // call method under test
        Map entity = RequestBody.getX3WFormEntity(request, Map.class, new ObjectMapper());

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
    }

    @Test
    public void getX3WFormEntity_with_2_key() {
        Request request = new Request();
        request.setBody("username=johndoe&password=A3ddj3w");

        // call method under test
        Map entity = RequestBody.getX3WFormEntity(request, Map.class, new ObjectMapper());

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
        Assert.assertEquals("A3ddj3w", entity.get("password"));
    }

    @Test
    public void getX3WFormEntity_with_1_key_and_no_value() {
        Request request = new Request();
        request.setBody("username=");

        // call method under test
        Map entity = RequestBody.getX3WFormEntity(request, Map.class, new ObjectMapper());

        // assert
        Assert.assertEquals("", entity.get("username"));
    }

    @Test
    public void getX3WFormEntity_with_1_key_and_no_value_and_another_key() {
        Request request = new Request();
        request.setBody("username=&password=A3ddj3w");

        // call method under test
        Map entity = RequestBody.getX3WFormEntity(request, Map.class, new ObjectMapper());

        // assert
        Assert.assertEquals("", entity.get("username"));
        Assert.assertEquals("A3ddj3w", entity.get("password"));
    }

    @Test
    public void getX3WFormEntity_with_1_key_and_1_key_and_no_value() {
        Request request = new Request();
        request.setBody("username=johndoe&password=");

        // call method under test
        Map entity = RequestBody.getX3WFormEntity(request, Map.class, new ObjectMapper());

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
        Assert.assertEquals("", entity.get("password"));
    }
}
