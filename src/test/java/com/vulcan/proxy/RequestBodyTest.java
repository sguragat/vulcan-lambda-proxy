package com.vulcan.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulcan.converter.BodyConverter;
import com.vulcan.converter.JacksonConverter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by sg on 02/08/2018.
 */
public class RequestBodyTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private BodyConverter bodyConverter = new JacksonConverter(objectMapper, true);

    private BodyConverter pojoConverter = new JacksonConverter(
            objectMapper.readerFor(Pojo.class),
            objectMapper.writer());

    @Test
    public void getEntity_asMap() {
        Request request = RequestSample.jsonRequest("{\"username\":\"johndoe\"}");

        // call method under test
        Map entity = RequestBody.getEntity(request, bodyConverter);

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
    }

    @Test
    public void getEntity_asPojo() {
        Request request = RequestSample.jsonRequest("{\"username\":\"johndoe\"}");

        // call method under test
        Pojo entity = RequestBody.getEntity(request, pojoConverter);

        // assert
        Assert.assertEquals("johndoe", entity.getUsername());
    }

    @Test
    public void getX3WFormEntity() {
        Request request = RequestSample.formRequest("username=johndoe");

        // call method under test
        Pojo entity = RequestBody.getEntity(request, pojoConverter);

        // assert
        Assert.assertEquals("johndoe", entity.getUsername());
    }

    @Test
    public void getX3WFormMap_with_1_key() {
        Request request = RequestSample.formRequest("username=johndoe");

        // call method under test
        Map entity = RequestBody.getEntity(request, bodyConverter);

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
    }

    @Test
    public void getX3WFormMap_with_2_key() {
        Request request = RequestSample.formRequest("username=johndoe&password=A3ddj3w");

        // call method under test
        Map entity = RequestBody.getEntity(request, bodyConverter);

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
        Assert.assertEquals("A3ddj3w", entity.get("password"));
    }

    @Test
    public void getX3WFormMap_with_1_key_and_no_value() {
        Request request = RequestSample.formRequest("username=");

        // call method under test
        Map entity = RequestBody.getEntity(request, bodyConverter);

        // assert
        Assert.assertEquals("", entity.get("username"));
    }

    @Test
    public void getX3WFormMap_with_1_key_and_no_value_and_another_key() {
        Request request = RequestSample.formRequest("username=&password=A3ddj3w");

        // call method under test
        Map entity = RequestBody.getEntity(request, bodyConverter);

        // assert
        Assert.assertEquals("", entity.get("username"));
        Assert.assertEquals("A3ddj3w", entity.get("password"));
    }

    @Test
    public void getX3WFormMap_with_1_key_and_1_key_and_no_value() {
        Request request = RequestSample.formRequest("username=johndoe&password=");

        // call method under test
        Map entity = RequestBody.getEntity(request, bodyConverter);

        // assert
        Assert.assertEquals("johndoe", entity.get("username"));
        Assert.assertEquals("", entity.get("password"));
    }

    private static class Pojo {

        private String username;

        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
