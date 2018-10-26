package com.vulcan.converter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sg on 26/10/2018.
 */
public class JSONObjectConverter implements BodyConverter {

    @Override
    public String objectToJson(Object o) {
        try {
            String json;
            if (o instanceof Map) {
                json = new JSONObject((Map) o).toString();
            } else {
                json = new JSONObject(o).toString();
            }
            if (json == null) {
                throw new IllegalStateException("JSONObject implementation returned null ( object = " + o + " )");
            }
            return json;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert Object into Json-String ( object = " + o + " )", e);
        }
    }

    @Override
    public Map<String, Object> jsonToMap(String json) {
        try {
            return new JSONObject(json).toMap();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert Json-String into Map ( json = " + json + " )", e);
        }
    }

    @Override
    public List<Map<String, Object>> jsonToMapList(String json) {
        try {
            List<Map<String, Object>> list = new LinkedList<>();
            new JSONArray(json).iterator().forEachRemaining(o -> list.add((Map<String, Object>) o));
            return list;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert Json-String into List<Map> ( json = " + json + " )", e);
        }
    }

    @Override
    public <E> E jsonToObject(String json) {
        // TODO: For now just return a Map. Ideally the Map must be transformed into a Pojo.
        return (E) jsonToMap(json);
    }

    @Override
    public <E> List<E> jsonToList(String json) {
        // TODO: Ror now just return a List<Map>. Ideally the Map must be transformed into a Pojo.
        return jsonToList(json);
    }
}
