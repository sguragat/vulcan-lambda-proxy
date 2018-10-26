package com.vulcan.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sg on 26/10/2018.
 */
public interface BodyConverter extends JsonConverter {

    default Map<String, Object> formToMap(String form) {
        Map<String, Object> entity = new HashMap<>();
        int startIndex = 0;
        int equalSignIndex;
        int andSignIndex;

        while ((equalSignIndex = form.indexOf('=', startIndex)) > -1) {
            String key = form.substring(startIndex, equalSignIndex);
            startIndex = equalSignIndex + 1;
            andSignIndex = form.indexOf('&', startIndex);
            String value;
            if (andSignIndex > -1) {
                value = form.substring(startIndex, andSignIndex);
                startIndex = (andSignIndex + 1);
            } else {
                value = form.substring(startIndex); // till end
                startIndex += value.length();
            }
            entity.put(key, value);
        }

        return entity;
    }

    default  <E> E formToObject(String form) {
        Map<String, Object> map = formToMap(form);
        return jsonToObject(objectToJson(map));
    }
}
