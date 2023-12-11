package com.yuyaogc.lowcode.engine.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

    public static <T> T string2Object(String json, Class<T> object) throws IOException {
        return objectMapper.readValue(json, object);
    }

    public static <T> T string2Collection(String json, Class<? extends Collection> collectionClass , Class<?> elementClass) throws IOException {
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
        return objectMapper.readValue(json, listType);
    }

    public static void readerForUpdating(Object instance, ObjectInput objectInput) {
        try {
            objectMapper.readerForUpdating(instance).readValue(objectInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeObjectOutput(ObjectOutput out, Object object){
        try {
            objectMapper.writeValue(out, object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T ObjectToClass(Object object, Class<T> clazz){
        return  objectMapper.convertValue(object, clazz);
    }

}
