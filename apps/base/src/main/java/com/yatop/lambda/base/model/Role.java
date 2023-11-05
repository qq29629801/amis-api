package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "base_role")
public class Role extends Model<Role> {
    @Id
    private Long id;

    @Column
    private String roleKey;
    @Column
    private String roleName;


    public Long getId(){
        return getLong("id");
    }

    public boolean isAdmin(){
        return "admin".equals(getStr("roleKey"));
    }
}
