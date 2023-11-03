package com.yuyaogc.lowcode.engine.util;

import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import net.sf.cglib.beans.BeanMap;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 *
 */
public final class BeanUtils {

    private BeanUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object bean) {
        return null == bean ? null : BeanMap.create(bean);
    }


    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
        T bean = ClassUtils.newInstance(clazz);
        try {
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }


    public static <T> List<Map<String, Object>> beansToMaps(List<T> beans) {
        if (beans == null) {
            return Collections.emptyList();
        }
        return beans.stream().map(BeanUtils::beanToMap).collect(toList());
    }


    public static <T> List<T> mapsToBeans(List<? extends Map<String, ?>> maps, Class<T> clazz) {
        if (maps == null) {
            return Collections.emptyList();
        }
        return maps.stream().map(e -> mapToBean(e, clazz)).collect(toList());
    }


    public static Class<?> getGenericTypeClass(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        return null;

    }

    public static Class<?> getTypeClass(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType type1 = (ParameterizedType) type;
            if (type1.getActualTypeArguments().length > 0) {
                if (type1.getActualTypeArguments()[0] instanceof Class) {
                    Class<?> clazz = (Class<? extends Object>) type1.getActualTypeArguments()[0];
                    return clazz;
                }
            }
        }
        return null;
    }


    public static Object toClass(Parameter parameter, Object arg){
        if (arg != null) {
            // List<?>
            if (!parameter.getType().isAssignableFrom(Map.class)) {
                if (parameter.getType().isAssignableFrom(arg.getClass())) {
                    String json = JsonUtil.objectToString(arg);
                    Type type = parameter.getParameterizedType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType type1 = (ParameterizedType) type;
                        if (type1.getActualTypeArguments().length > 0) {
                            if (type1.getActualTypeArguments()[0] instanceof Class) {
                                Class<?> clazz = (Class<? extends Object>) type1.getActualTypeArguments()[0];
                                try {
                                    return JsonUtil.stringToList(json, clazz);
                                } catch (IOException e) {
                                    // log.error(EngineErrorEnum.JsonProcessingException, e);
                                }
                            }
                        }
                    }
                }
            }
            // Class<?>
            if (!parameter.getType().isAssignableFrom(arg.getClass())) {
                try {
                    String json = JsonUtil.objectToString(arg);
                    return JsonUtil.stringToObject(json, parameter.getType());
                } catch (IOException e) {
                    //log.error(EngineErrorEnum.JsonProcessingException, e);
                }
            }
        }
        return arg;
    }
}
