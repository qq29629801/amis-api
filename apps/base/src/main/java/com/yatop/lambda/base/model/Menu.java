package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_menu")
public class Menu extends Model<Menu> {
    @Id
    private Long id;
    @Column
    private String menuName;

    @Column
    private String component;

    @Column
    private String icon;

    @Column
    private String perms;

    @Column
    private Integer orderNum;


    public String getPerms(){
        return getStr("perms");
    }
}
