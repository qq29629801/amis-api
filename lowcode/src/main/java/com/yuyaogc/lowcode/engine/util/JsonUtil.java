package com.yuyaogc.lowcode.engine.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //忽略属性为null
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略多余属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String objectToString(Object object) {

        try {
            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {
            // TODO: handle exception
        }
        return null;
    }

    public static <T> T stringToObject(String json, Class<T> object) throws IOException {
        return objectMapper.readValue(json, object);
    }

    public static <T> List<T> stringToList(String json, Class<T> object) throws IOException {
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, object);
        List<T> list = objectMapper.readValue(json, listType);
        return list;
    }
}
