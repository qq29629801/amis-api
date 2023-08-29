package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.entity.datatype.DataType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型字段
 */
public class EntityField extends Entity implements Serializable {
    private EntityClass entity;
    private boolean pk;
    private String columnName;
    private DataType dataType;
    private boolean store;
    private boolean inherited;
    private Map<String, Validate> validates = new HashMap<>();

    private String relModel;
    private String relApp;
    private String relTable;

    public EntityField() {
    }

    public EntityField(EntityClass entity) {
        this.entity = entity;
    }

    public EntityClass getEntity() {
        return entity;
    }

    public void setEntity(EntityClass entity) {
        this.entity = entity;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }



    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }



    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getLength() {
        return dataType.getSize(this);
    }

    public Map<String, Validate> getValidates() {
        return validates;
    }

    public String getRelApp() {
        return relApp;
    }

    public void setRelApp(String relApp) {
        this.relApp = relApp;
    }

    public String getRelTable() {
        return relTable;
    }

    public void setRelTable(String relTable) {
        this.relTable = relTable;
    }

    public String getRelModel() {
        return relModel;
    }

    public void setRelModel(String relModel) {
        this.relModel = relModel;
    }

    public void setValidates(Map<String, Validate> validates) {
        this.validates = validates;
    }

    public void addValidate(Validate validate) {
        validates.put(this.getName(), validate);
    }


    public void removeValidate() {
        validates.remove(this.getName());
    }

    protected List<EntityField> resolveMro(EntityClass model, String name) {
        List<EntityField> result = new ArrayList<>();
        for (EntityClass cls : model.getMro()) {
            if (cls.getApplication() == null) {
                EntityField field = cls.getField(name);
                if (field != null) {
                    result.add(field);
                }
            }
        }
        return result;
    }


    @Override
    public String toString() {
        return "EntityField{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
