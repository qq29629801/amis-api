package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.JoinColumn;
import com.yuyaogc.lowcode.engine.annotation.ManyToOne;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
@Table(name = "base_role_user")
public class RoleUser extends Model<RoleUser> {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;



    public Long getRoleId(){
        return getLong("roleId");
    }
}
