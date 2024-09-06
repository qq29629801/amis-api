package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.lang.reflect.Method;
import java.util.*;

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


    @Service(displayName = "获取模型权限")
    public List<Map<String,Object>> getModelPermissions(Long roleId){
        List<Map<String,Object>> permissions = new ArrayList<>();

        for(Application app: Container.me().getApps()){
            Map<String,Object> appItem = new HashMap<>();
            appItem.put("label",app.getDisplayName());

            List<Map<String,Object>> modelItemList = new ArrayList<>();
            appItem.put("children",modelItemList);
            for(EntityClass entityClass: app.getModels()){
                Map<String,Object> modelItem = new HashMap<>();
                modelItem.put("label",entityClass.getDisplayName());
                modelItem.put("value", entityClass.getName());
                modelItemList.add(modelItem);
            }
            permissions.add(appItem);
        }

      return permissions;
    }
    @Service(displayName = "获取服务权限")
    public List<Map<String,Object>> getServicePermissions(Long roleId){
        SystemPermissions dao = new SystemPermissions();


        LambdaQueryWrapper<SystemPermissions> lambdaQueryWrapper = Wrappers.lambdaQuery();
        List<SystemPermissions> systemPermissionsList = dao.search(lambdaQueryWrapper.eq(SystemPermissions::getRole, roleId), 0,0,null);



        List<Map<String,Object>> permissions = new ArrayList<>();

        for(Application app: Container.me().getApps()){

            for(EntityClass entityClass: app.getModels()){


                Map<String,Object> modelItem = new HashMap<>();
                modelItem.put("label",entityClass.getDisplayName());

                List<Map<String,Object>> modelItems = new ArrayList<>();
                modelItem.put("children",modelItems);


                for(LinkedList<EntityMethod> methods: entityClass.getMethods()){

                    for(EntityMethod method: methods){

                        Map<String,Object> methodItem = new HashMap<>();
                        methodItem.put("label",method.getDisplayName());
                        methodItem.put("value", method.getName());

                        modelItems.add(methodItem);
                    }

                }

                permissions.add(modelItem);
            }

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
