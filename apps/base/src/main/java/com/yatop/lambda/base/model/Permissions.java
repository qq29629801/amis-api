package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.IdWorker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "base_permissions",displayName = "权限")
public class Permissions extends Model<Permissions> {

    @Id
    private Long id;

    @Column(label = "名称")
    private String name;

    @Column(label = "权限码")
    private String auth;

    @Column(label = "value")
    private int value;



    @Service
    public long count(Criteria criteria) {
        return 100l;
    }


    @Service
    public List<Permissions> search(Criteria criteria, Integer offset, Integer limit, String order) {
        //TODO 批量


        List<Permissions> permissionsList = new ArrayList<>();
        for(Application application: Container.me().getApps()){
            for(EntityClass entityClass: application.getModels()){
                Permissions permissionsEntity = new Permissions();
                permissionsEntity.setName(entityClass.getDisplayName());
                permissionsEntity.setAuth(String.format("%s.%s@EntityClass", application.getName(), entityClass.getName()));
                permissionsEntity.setId(IdWorker.getId());
                permissionsList.add(permissionsEntity);

                for(EntityField entityField: entityClass.getFields()){
                    Permissions permissionsField = new Permissions();
                    permissionsField.setName(entityClass.getDisplayName());
                    permissionsField.setAuth(String.format("%s.%s.%s@EntityField",application.getName() ,entityClass.getName(), entityField.getName()));
                    permissionsField.setId(IdWorker.getId());
                    permissionsList.add(permissionsField);
                }

                for(LinkedList<EntityMethod> entityMethods: entityClass.getMethods()){
                    for(EntityMethod entityMethod: entityMethods){
                        Permissions permissionsMethod = new Permissions();
                        permissionsMethod.setName(entityClass.getDisplayName());
                        permissionsMethod.setAuth(String.format("%s.%s.%s@EntityMethod",application.getName() ,entityClass.getName(), entityMethod.getName()));
                        permissionsMethod.setId(IdWorker.getId());
                        permissionsList.add(permissionsMethod);
                    }

                }
            }
        }
      return   permissionsList.stream().skip(offset).collect(Collectors.toList());
    }

    public Long getId() {
        return (Long) this.get("id");
    }

    public Permissions setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public Permissions setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getAuth() {
        return (String) this.get("auth");
    }

    public Permissions setAuth(String auth) {
        this.set("auth", auth);
        return this;
    }

    public int getValue() {
        return (int) this.get("value");
    }

    public Permissions setValue(int value) {
        this.set("value", value);
        return this;
    }
}
