package com.yuyaogc.lowcode.engine.util;

import net.sf.cglib.beans.BeanMap;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

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
                    return (Class<?>) type1.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }


    public static Class<?> getTypeClass(Parameter parameter) {
        Type type = parameter.getParameterizedType();
        if (type instanceof ParameterizedType) {
            ParameterizedType type1 = (ParameterizedType) type;
            // 判断是否有类型参数
            if (type1.getActualTypeArguments().length > 0) {
                // 判断类型参数是否为 Class
                if (type1.getActualTypeArguments()[0] instanceof Class) {
                    Class<?> clazz = (Class<?>) type1.getActualTypeArguments()[0];
                    // 转换对象为指定的类
                    return clazz;
                }
            }
        }
        return null;
    }


    /**
     * 将对象转换为指定的类
     *
     * @param parameter 要转换的参数
     * @param arg       要转换的对象
     * @return 转换后的对象
     */
    public static Object toClass(Parameter parameter, Object arg) throws IOException {
        if (arg != null) {
            // 判断是否为 List<?>
            String json = JsonUtil.objectToString(arg);

            Class parameterType = parameter.getType();

            if (List.class.isAssignableFrom(parameterType) ||
                    Set.class.isAssignableFrom(parameterType) ||
                    Vector.class.isAssignableFrom(parameterType) ||
                    Hashtable.class.isAssignableFrom(parameterType)) {
                // 判断是否与参数的类型相匹配
                Class<?> clazz = getTypeClass(parameter);
                return JsonUtil.stringToObject(json, clazz);
            }
            // 判断是否与参数的类型相匹配
            if (!parameter.getType().isAssignableFrom(arg.getClass())) {
                // 转换对象为参数的类型
                return JsonUtil.ObjectToClass(json, parameter.getType());
            }
        }
        // 返回原始对象
        return arg;
    }
}
