package com.yuyaogc.lowcode.engine.plugin.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import com.yuyaogc.lowcode.engine.util.BeanUtils;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.JarEntry;

public final class ClassPlus {

    public static void processField(EntityClass entity, Class<?> entityClass) {
        List<EntityField> fields = _getFields(entity, entityClass, null, null);
    }


    private Map<String, Class<?>> _getGenericTypeMap(Class<?> entityClass) {
        Map<String, Class<?>> genericMap = new HashMap<String, Class<?>>();
        if (entityClass == Object.class) {
            return genericMap;
        }
        //获取父类和泛型信息
        Class<?> superClass = entityClass.getSuperclass();
        //判断superClass
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(TableName.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            if (entityClass.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments();
                TypeVariable[] typeVariables = superClass.getTypeParameters();
                if (typeVariables.length > 0) {
                    for (int i = 0; i < typeVariables.length; i++) {
                        if (types[i] instanceof Class) {
                            genericMap.put(typeVariables[i].getName(), (Class<?>) types[i]);
                        }
                    }
                }
            }
            genericMap.putAll(_getGenericTypeMap(superClass));
        }
        return genericMap;
    }

    public static List<PropertyDescriptor> getProperties(Class<?> entityClass) {
        List<PropertyDescriptor> entityFields = new ArrayList<PropertyDescriptor>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(entityClass);
        } catch (IntrospectionException e) {
            throw new EngineException(e);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            if (!desc.getName().equals("class")) {
                entityFields.add(desc);
            }
        }
        return entityFields;
    }

    public static List<EntityField> _getFields(EntityClass entity, Class<?> entityClass, List<EntityField> fieldList, Integer level) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        if (level == null) {
            level = 0;
        }
        java.lang.reflect.Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            java.lang.reflect.Field field = fields[i];
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                String columnName = StringUtils.camelToUnderline(field.getName());

                if (field.isAnnotationPresent(TableField.class)) {
                    TableField column = field.getAnnotation(TableField.class);
                    columnName = StringUtils.camelToUnderline(column.value());
                }
                EntityField entityField = new EntityField(entity);
                entityField.setStore(true);

                DataType dataType = null;
                Class<?> typeClass = BeanUtils.getTypeClass(field);
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        columnName = joinColumn.name();
                    }
                    //TODO MAP CLASS
                } else if (field.isAnnotationPresent(ManyToMany.class)) {
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                } else {
                    dataType = DataType.create(field.getType().getSimpleName());
                }
                entityField.setDataType(dataType);
                if (field.isAnnotationPresent(TableId.class)) {
                    entityField.setPk(true);
                    entityField.setDataType(DataType.create(Constants.LONG));
                    entity.addPkField(field.getName(), entityField);
                }
                entityField.setName(field.getName());
                if (StringUtil.isEmpty(columnName)) {
                    columnName = field.getName();
                }
                entityField.setColumnName(columnName);
                if (level.intValue() != 0) {
                    fieldList.add(index, entityField);
                    entity.addField(field.getName(), entityField);
                    index++;
                } else {
                    fieldList.add(entityField);
                    entity.addField(field.getName(), entityField);
                }
            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(TableName.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return _getFields(entity, entityClass.getSuperclass(), fieldList, ++level);
        }
        return fieldList;
    }


    public static void processClass(Class<?> entityClass, Application application) {
        TableName table = entityClass.getAnnotation(TableName.class);

        EntityClass entity = new EntityClass(application);

        if (entityClass.isAnnotationPresent(Extend.class)) {
            Extend extend = entityClass.getAnnotation(Extend.class);
            entity.setParent(extend.parent());
        }

        if (entityClass.isAnnotationPresent(TableName.class)) {
            entity.setName(entityClass.getSimpleName());
            entity.setTableName(table.value());

            processField(entity, entityClass);

            processMethod(entity, entityClass);

            application.addModel(entityClass.getSimpleName(), entity);
        }
    }

    public static void processMethod(EntityClass entity, Class<?> entityClass) {
        Method[] methods = entityClass.getDeclaredMethods();

        for (Method method : methods) {
            method.setAccessible(true);
            if (Modifier.isPublic(method.getModifiers())) {
                EntityMethod entityMethod = new EntityMethod(entity);
                entityMethod.setName(method.getName());
                entityMethod.setApplication(entity.getApplication());
                entityMethod.setClassName(entityClass.getName());

                entityMethod.setDisplayName(method.getName());
                entity.addMethod(method.getName(), entityMethod);
            }
        }


        if (entityClass.getSuperclass() != null) {
            processMethod(entity, entityClass.getSuperclass());
        }
    }

    public static Application processApp(Container container, Application application, List<Class<?>> classList, AppClassLoader jarLauncher) throws IOException {
        try {

            for (Class<?> clazz : classList) {
                if (clazz.isAnnotationPresent(APPInfo.class)) {
                    APPInfo appInfo = clazz.getAnnotation(APPInfo.class);
                    application.setName(appInfo.name());
                    application.setDisplayName(appInfo.displayName());
                    application.setDependencies(appInfo.depends());
                    application.setVersion(appInfo.version());
                    application.setClassLoader(jarLauncher);
                }
                processClass(clazz, application);
            }

            container.add(application.getName(), application);

            application.setContainer(container);

            return application;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Class<?>> scanPackage(String basePackage, AppClassLoader classLoader) {
        List<Class<?>> result = new ArrayList<>();
        Enumeration<JarEntry> entries = classLoader.jarFile.entries();
        String basePackage1 = basePackage.replaceAll("\\.", "/");
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.contains(basePackage1) && name.startsWith("BOOT-INF/classes") && name.endsWith(".class")) {
                String className = name.replace("BOOT-INF/classes/", "").
                        replace("\\", ".").
                        replace("/", ".").
                        replace(".class", "");
                try {
                    Class clazz = classLoader.loadClass(className);
                    if (clazz.isAnnotationPresent(TableName.class)
                            || clazz.isAnnotationPresent(APPInfo.class)) {
                        result.add(classLoader.loadClass(className));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("实例化对象时出现错误,请尝试给 %s 添加无参的构造方法", clazz.getName()), e);
        }
    }

    private static Class<?> getTargetClass(Class<?> clazz) {
        boolean isCglibProxy = clazz.getName().contains("$$");
        return isCglibProxy ? clazz.getSuperclass() : clazz;
    }


    public static String getName(Class<?> clazz) {
        Class<?> class1 = getTargetClass(clazz);
        return class1.getSimpleName();
    }

}
