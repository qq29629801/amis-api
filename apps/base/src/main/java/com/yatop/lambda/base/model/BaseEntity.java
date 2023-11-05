package com.yatop.lambda.base.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.annotation.NotBlank;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

@Table(name = "base_entity")
public class BaseEntity extends Model<BaseEntity> {
    @Id
    private Long id;
    @Column(name = "app_name")
    @NotBlank
    private String appName;
    @Column(name = "entity_name")
    @NotBlank
    private String entityName;
    @Column(name = "table_name")
    private String tableName;
    @NotBlank
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "parent")
    private String parent;


    @Service
    public void create(BaseEntity baseEntity) {
        Container container = Container.me();

        Application application = container.get(baseEntity.getStr("appName"));
        if (Objects.isNull(application)) {
            return;
        }

        EntityClass entityClass = new EntityClass();
        entityClass.setApplication(application);
        entityClass.setName(baseEntity.getStr("tableName"));
        entityClass.setDisplayName(baseEntity.getStr("displayName"));
        entityClass.setTableName(baseEntity.getStr("tableName"));

        String parent = baseEntity.getStr("parent");
        if (StringUtils.isNotEmpty(parent)) {
            entityClass.setParent(parent.split(","));
        }

        try {
            Class clazz = Class.forName("com.yuyaogc.lowcode.engine.plugin.activerecord.Model");
            ClassUtils.addMethod(entityClass, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        EntityField entityField = new EntityField();
        entityField.setName("id");
        entityField.setEntity(entityClass);
        entityField.setColumnName("id");
        entityField.setPk(true);
        entityField.setStore(true);
        entityField.setDataType(DataType.create(Constants.LONG));
        entityClass.addPkField("id", entityField);
        entityClass.addField("id", entityField);

        application.addModel(baseEntity.getStr("entityName"), entityClass);

        for (EntityClass entityClass1 : application.getModels()) {
            ClassBuilder.buildEntityClass(entityClass1, container);
        }

        try (Context context = new Context(null, Db.getConfig())) {
            application.autoTableInit(context.getConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }

        baseEntity.save();
    }


    @Service
    public Map<String, Object> loadView(String app, String model) {
        Application application = Container.me().get(app);
        EntityClass entityClass = application.getEntity(model);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("app", app);
        jsonObject.set("model", model);

        JSONArray jsonArray = new JSONArray();
        for (EntityField field : entityClass.getFields()) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("dataType", field.getDataType().getName());
            jsonObject1.set("name", field.getName());
            jsonObject1.set("columnName", field.getColumnName());
            jsonObject1.set("id", field.getId());
            jsonArray.add(jsonObject1);
        }
        jsonObject.set("fields", jsonArray);

        return jsonObject;
    }
}
