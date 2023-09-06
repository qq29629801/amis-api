package com.yatop.lambda.base.model;

import cn.dev33.satoken.stp.StpUtil;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Map;

@Table(name = "base_user")
public class BaseUser extends Model<BaseUser> {
    @Id
    public Long id;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private String nickName;


    @Service
    public Map<String, Object> login(Map<String, String[]> args) {
        StpUtil.login(1);

        return null;
    }

}
