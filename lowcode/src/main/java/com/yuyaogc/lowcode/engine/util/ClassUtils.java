package com.yuyaogc.lowcode.engine.util;

import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.annotation.Max;
import com.yuyaogc.lowcode.engine.annotation.Min;
import com.yuyaogc.lowcode.engine.annotation.NotBlank;
import com.yuyaogc.lowcode.engine.annotation.Pattern;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.*;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.JarEntry;

public final class ClassUtils {


    public static void addClass(Class<?> entityClass, Application application) {
        Table table = entityClass.getAnnotation(Table.class);
        EntityClass entity = new EntityClass(application);
        if (entityClass.isAnnotationPresent(Table.class)) {
            entity.setParent(table.parent());
            entity.setName(entityClass.getSimpleName());
            entity.setId(entityClass.getName());
            entity.setClassName(entityClass.getName());
            entity.setTableName(table.name());
            entity.setAppName(application.getName());
            entity.setVersion(application.getVersion());
            addField(entity, entityClass);
            addMethod(entity, entityClass);
            application.addModel(entityClass.getSimpleName(), entity);
        }
    }

    public static void addMethod(EntityClass entity, Class<?> entityClass) {
        Method[] methods = entityClass.getDeclaredMethods();

        for (Method method : methods) {
            method.setAccessible(true);
            if (Modifier.isPublic(method.getModifiers())) {
                if (method.isAnnotationPresent(Service.class)) {
                    EntityMethod entityMethod = new EntityMethod(entity);
                    entityMethod.setName(method.getName());
                    entityMethod.setApplication(entity.getApplication());
                    entityMethod.setClassName(entityClass.getName());

                    Service service = method.getAnnotation(Service.class);
                    if (service.event()) {
                        entity.putEvent(entityMethod.getName(), entityMethod);
                    } else  if(service.stop()){
                        entity.putDestroy(entityMethod.getName(), entityMethod);
                    }


                    entityMethod.setDisplayName(service.displayName());
                    entity.addMethod(method.getName(), entityMethod);
                }
            }
        }


        if (entityClass.getSuperclass() != null) {
            addMethod(entity, entityClass.getSuperclass());
        }
    }

    public static Application addApp(Container container, Application application, List<Class<?>> classList) throws IOException {
        try {
            classList.forEach(clazz->  {
                if (clazz.isAnnotationPresent(APPInfo.class)) {
                    APPInfo appInfo = clazz.getAnnotation(APPInfo.class);
                    application.setApplication(appInfo);
                }
                addClass(clazz, application);
            });
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
                    if (clazz.isAnnotationPresent(Table.class)
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


    public static void addField(EntityClass entity, Class<?> entityClass) {
        List<EntityField> fields = _getFields(entity, entityClass, null, null);
    }


    public static void processValidate(Validate validate, java.lang.reflect.Field field) {
        if (field.isAnnotationPresent(NotBlank.class)) {
            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            validate.setEmpty(false);
            validate.setMessage(notBlank.message());
        }
        if (field.isAnnotationPresent(Max.class)) {
            Max max = field.getAnnotation(Max.class);
            validate.setMax(max.value());
        }
        if (field.isAnnotationPresent(Min.class)) {
            Min min = field.getAnnotation(Min.class);
            validate.setMin(min.value());
        }
        if (field.isAnnotationPresent(Pattern.class)) {
            Pattern pattern = field.getAnnotation(Pattern.class);
            validate.setRegexp(pattern.regexp());
        }
        validate.getField().addValidate(validate);
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
                && (superClass.isAnnotationPresent(Table.class)
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

                Integer length = null;
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    columnName = StringUtils.camelToUnderline(column.name());
                    length = column.length();
                }
                EntityField entityField = new EntityField(entity);
                entityField.setStore(true);
                entityField.setLength(length);

                DataType dataType = null;

                Class<?> typeClass;
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    typeClass = field.getType();
                    entityField.setRelModel(typeClass.getSimpleName());
                    dataType = DataType.create(Constants.MANY2ONE);
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        columnName = joinColumn.name();
                    }
                    //TODO MAP CLASS
                } else if (field.isAnnotationPresent(ManyToMany.class)) {
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                    dataType = DataType.create(Constants.ONE2MANY);
                    typeClass = BeanUtils.getTypeClass(field);
                    entityField.setRelModel(typeClass.getSimpleName());
                } else if(field.isAnnotationPresent(Column.class)){
                    Column column = field.getAnnotation(Column.class);
                    if(column.type().equals(DataTypeEnum.TEXT)){
                        dataType =  DataType.create(Constants.TEXT);
                    } else {
                        dataType = DataType.create(field.getType().getSimpleName());
                    }
                } else {
                    dataType = DataType.create(field.getType().getSimpleName());
                }
                entityField.setDataType(dataType);
                if (field.isAnnotationPresent(Id.class)) {
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


                if (field.isAnnotationPresent(Column.class)) {
                    Validate validate = new Validate();
                    validate.setField(entityField);
                    processValidate(validate, field);
                }

            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Table.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return _getFields(entity, entityClass.getSuperclass(), fieldList, ++level);
        }
        return fieldList;
    }

}
