package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.loader.AppStateEnum;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Column;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;

import java.util.*;

/**
 * 应用
 */
public class Application extends Entity{
    private ClassLoader classLoader;
    private Container container;
    private Map<String, EntityClass> entityClasss = new HashMap<>();
    private String[] dependencies;
    private String version;
    private String category;
    private double weight = 1.0;
    private AppStateEnum stateEnum;
    private String fileName;
    private List<Application> depends;

    public List<Application> getDepends() {
        return depends;
    }

    public void setDepends(List<Application> depends) {
        this.depends = depends;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isBaseApp() {
        return false;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }




    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    public boolean containsKey(String className){
        return entityClasss.containsKey(className);
    }

    public Collection<EntityClass> getModels() {
        return entityClasss.values();
    }

    public EntityClass getEntity(String name) {
        return entityClasss.get(name);
    }

    public void addModel(String name, EntityClass entity) {
        entityClasss.put(name, entity);
    }

    public void remove(String name){
        entityClasss.remove(name);
    }





    public void onEvent(Context context){
        for (EntityClass entity : getModels()) {
            for(EntityMethod entityMethod: entity.getEvents()){
                try {
                    Map<String,Object> args = new LinkedHashMap<>();

                    entityMethod.invoke(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void autoTableInit(Config config) {

        for (EntityClass entity : getModels()) {
            boolean tableExists = config.dialect.tableExists(config, entity.getTableName());
            if (!tableExists) {
                config.dialect.createModelTable(config, entity.getTableName(), entity.getDisplayName());
            }
            Map<String, Column> columns = config.dialect.tableColumns(config, entity.getTableName());
            entity.getFields().forEach(field -> {

                Column column = columns.get(field.getColumnName());
                if (Objects.isNull(column)) {
                    if (field.getDataType() instanceof DataType.One2manyField || field.getDataType() instanceof DataType.Many2manyField) {
                    } else {
                        config.dialect.createColumn(config, entity.getTableName(), field.getColumnName(), config.dialect.getColumnType(field.getDataType().getType(), field.getDataType().getSize(field), null), null, false);
                    }
                } else {
                    if (!column.getLength().equals(field.getLength()) ||
                            !config.dialect.getColumnType(field.getDataType().getType()).equals(column.getType())) {
                        config.dialect.modifyColumn(config, entity.getTableName(), field.getColumnName(), config.dialect.getColumnType(field.getDataType().getType(), field.getLength(), field.getScale()), field.getDisplayName(), false, null);
                    }
                }
            });
        }
    }

    public AppStateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(AppStateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }

    @Override
    public String toString() {
        return this.getName();
    }


}
