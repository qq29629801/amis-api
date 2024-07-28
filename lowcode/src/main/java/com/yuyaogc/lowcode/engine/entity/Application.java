package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.enums.AppStateEnum;
import com.yuyaogc.lowcode.engine.enums.AppTypeEnum;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Column;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 应用
 */
public class Application extends Entity{
    private static Logger log = LoggerFactory.getLogger(Application.class);
    private ClassLoader classLoader;
    private Container container;
    private Map<String, EntityClass> entityClasss = new HashMap<>();
    private String[] dependencies;
    private String version;
    private String category;
    private double weight = 1.0;
    private AppStateEnum stateEnum;

    private AppTypeEnum typeEnum;

    private String fileName;
    private List<Application> depends;

    public AppTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(AppTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

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

    public void buildModel(String name, EntityClass entity) {
        entityClasss.put(name, entity);
    }

    public void remove(String name){
        entityClasss.remove(name);
    }



    public void setApplication(APP appInfo){
        this.setName(appInfo.name());
        this.setDisplayName(appInfo.displayName());
        this.setDependencies(appInfo.depends());
        this.setVersion(appInfo.version());
        this.setTypeEnum(appInfo.type());
    }


    public void onDestroy(){
        for (EntityClass entity : getModels()) {
            for(EntityMethod entityMethod: entity.getDestroys()){
                try {
                    entityMethod.invoke();
                } catch (Exception e) {
                    log.error("销毁事件 {}",e);
                }
            }
        }
    }


    public void onEvent(){
        for (EntityClass entity : getModels()) {
            for(EntityMethod entityMethod: entity.getEvents()){
                try {
                    entityMethod.invoke();
                } catch (Exception e) {
                    log.error("启动事件 {}",e);
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
                        config.dialect.createColumn(config, entity.getTableName(), field.getColumnName(), config.dialect.getColumnType(field.getDataType().getType(), field.getDataType().getSize(field), null), field.getDisplayName(), false);
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
