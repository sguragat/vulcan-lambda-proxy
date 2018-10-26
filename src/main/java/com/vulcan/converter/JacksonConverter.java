package com.vulcan.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by sg on 26/10/2018.
 */
public class JacksonConverter implements BodyConverter {

    private final ObjectReader reader;

    private final ObjectWriter writer;

    public JacksonConverter(boolean pretty) {
        this(new ObjectMapper(), pretty);
    }

    public JacksonConverter(ObjectMapper mapper, boolean pretty) {
        reader = mapper.readerFor(Map.class);
        writer = pretty ? mapper.writer().withDefaultPrettyPrinter() : mapper.writer();
    }

    public JacksonConverter(ObjectReader objectReader, ObjectWriter objectWriter) {
        reader = objectReader;
        writer = objectWriter;
    }

    @Override
    public String objectToJson(Object o) {
        try {
            return writer.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to convert Object into Json-String ( object = " + o + " )", e);
        }
    }

    @Override
    public Map<String, Object> jsonToMap(String json) {
        try {
            return reader.readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert Json-String into Map ( json = " + json + " )", e);
        }
    }

    @Override
    public List<Map<String, Object>> jsonToMapList(String json) {
        try {
            MappingIterator<Map<String, Object>> iterator = reader.readValues(json);
            return iterator.readAll();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert Json-String into List<Map> ( json = " + json + " )", e);
        }
    }

    @Override
    public <E> E jsonToObject(String json) {
        try {
            return reader.readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert Json-String into Object ( json = " + json + " )", e);
        }
    }

    @Override
    public <E> List<E> jsonToList(String json) {
        try {
            MappingIterator<E> iterator = reader.readValues(json);
            return iterator.readAll();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert Json-String into List<Map> ( json = " + json + " )", e);
        }
    }
}
