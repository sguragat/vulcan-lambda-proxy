package com.vulcan.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sg on 31/07/2018.
 */
public class TypedMap extends HashMap<String, Object> {

    public TypedMap() {
    }

    public TypedMap(Map<String, Object> map) {
        super(map);
    }

    public UUID getOptionalUUID(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof UUID) {
            return (UUID) value;
        }
        try {
            return UUID.fromString(getString(key));
        } catch (IllegalStateException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to UUID", e);
        }
    }

    public String getString(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to Integer", e);
        }
    }

    public Long getLong(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to Long", e);
        }
    }

    public Double getDouble(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        try {
            return Double.valueOf(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to Double", e);
        }
    }

    public Float getFloat(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        try {
            return Float.valueOf(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to Float", e);
        }
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value.toString().equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (value.toString().equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        throw new IllegalStateException("The value of key '%s' cannot be converted to Boolean");
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return BigDecimal.valueOf(getLong(key));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to BigDecimal", e);
        }
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        try {
            return BigInteger.valueOf(getLong(key));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("The value of key '%s' cannot be converted to BigInteger", e);
        }
    }

    public Map<String, Object> getMap(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        throw new IllegalStateException("The value of key '%s' cannot be converted to Map");
    }
}