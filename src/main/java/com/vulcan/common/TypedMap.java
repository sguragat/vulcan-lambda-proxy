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

    public String getOptionalString(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    public Integer getOptionalInteger(String key) {
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

    public Long getOptionalLong(String key) {
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

    public Double getOptionalDouble(String key) {
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

    public Float getOptionalFloat(String key) {
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

    public Boolean getOptionalBoolean(String key) {
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

    public BigDecimal getOptionalBigDecimal(String key) {
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

    public BigInteger getOptionalBigInteger(String key) {
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

    public Map<String, Object> getOptionalMap(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        throw new IllegalStateException("The value of key '%s' cannot be converted to Map");
    }

    public String getString(String key) {
        String value = getOptionalString(key);
        checkNotNull(key, value);
        return value;
    }

    public Integer getInteger(String key) {
        Integer value = getOptionalInteger(key);
        checkNotNull(key, value);
        return value;
    }

    public Long getLong(String key) {
        Long value = getOptionalLong(key);
        checkNotNull(key, value);
        return value;
    }

    public Double getDouble(String key) {
        Double value = getOptionalDouble(key);
        checkNotNull(key, value);
        return value;
    }

    public Float getFloat(String key) {
        Float value = getOptionalFloat(key);
        checkNotNull(key, value);
        return value;
    }

    public Boolean getBoolean(String key) {
        Boolean value = getOptionalBoolean(key);
        checkNotNull(key, value);
        return value;
    }

    public BigDecimal getBigDecimal(String key) {
        BigDecimal value = getOptionalBigDecimal(key);
        checkNotNull(key, value);
        return value;
    }

    public BigInteger getBigInteger(String key) {
        BigInteger value = getOptionalBigInteger(key);
        checkNotNull(key, value);
        return value;
    }

    public Map<String, Object> getMap(String key) {
        Map<String, Object> value = getOptionalMap(key);
        checkNotNull(key, value);
        return value;
    }

    private void checkNotNull(String key, Object value) {
        if (value == null) {
            throw new IllegalStateException(String.format("Required key '%s' does not exist or has null value", key));
        }
    }
}