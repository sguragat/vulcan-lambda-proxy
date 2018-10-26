package com.vulcan.converter;

import java.util.List;
import java.util.Map;

/**
 * Created by sg on 26/10/2018.
 */
public interface JsonConverter {

    String objectToJson(Object o);

    Map<String, Object> jsonToMap(String json);

    List<Map<String, Object>> jsonToMapList(String json);

    <E> E jsonToObject(String json);

    <E> List<E> jsonToList(String json);
}
