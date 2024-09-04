package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "base_role",displayName = "角色")
public class Role extends Model<Role> {




    @Id
    private Long id;
    @Column(label = "角色编码")
    private String roleKey;
    @Column(label = "角色名称")
    private String roleName;
    @Column(label = "是否管理员")
    private Boolean admin;
    @ManyToMany
    @JoinTable(name = "base_role_user",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;
    @ManyToMany
    @JoinTable(name = "base_role_permission",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permissions> permissionsList;


    @Service
    public List<Map<String,Object>> getPermissionsByRole(Long roleId){
        List<Map<String,Object>> permissions = new ArrayList<>();

        for(Application app: Container.me().getApps()){
            Map<String,Object> appItem = new HashMap<>();
            appItem.put("label",app.getDisplayName());

            List<Map<String,Object>> modelItems = new ArrayList<>();
            appItem.put("children",modelItems);
            for(EntityClass entityClass: app.getModels()){
                Map<String,Object> modelItem = new HashMap<>();
                modelItem.put("label",entityClass.getDisplayName());
                modelItem.put("value", entityClass.getName());
                modelItems.add(modelItem);
            }
            permissions.add(appItem);
        }

      return permissions;
    }




    public Role setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getRoleKey() {
        return (String) this.get("roleKey");
    }

    public Role setRoleKey(String roleKey) {
        this.set("roleKey", roleKey);
        return this;
    }

    public String getRoleName() {
        return (String) this.get("roleName");
    }

    public Role setRoleName(String roleName) {
        this.set("roleName", roleName);
        return this;
    }

    public Long getId(){
        return getLong("id");
    }

    public boolean isAdmin(){
        return getBoolean("admin");
    }
}
