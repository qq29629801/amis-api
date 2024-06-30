package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "base_role")
public class Role extends Model<Role> {
    @Id
    private Long id;

    @Column(label = "角色编码")
    private String roleKey;
    @Column(label = "角色名称")
    private String roleName;
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<User> userList;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permissions> permissionsList;



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
        return "admin".equals(getStr("roleKey"));
    }
}
