package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;


@Table(name = "base_user")
public class User extends Model<User> {
    @Id
    public Long id;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private String nickName;

    @Column
    private String loginType;


    public Long getId(){
        return getLong("id");
    }


}
