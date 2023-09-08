package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.JoinColumn;
import com.yuyaogc.lowcode.engine.annotation.ManyToOne;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
@Table(name = "base_role_menu")
public class RoleMenu extends Model<RoleMenu> {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menuId;

    public String getPerms(){
        return getStr("perms");
    }
}
