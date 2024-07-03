package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



@Table(name = "base_user")
public class User extends Model<User> {
    @Id
    public Long id;

    @Column(label = "用户名")
    private String userName;

    @Column(label = "密码")
    private String password;

    @Column(label = "昵称")
    private String nickName;

    @Column(label = "登录类型")
    private String loginType;


    @ManyToMany(mappedBy = "userList")
    private List<Role> roleList;

    public User setId(Long id) {
        this.set("id", id);
        return this;
    }

    public User setUserName(String userName) {
        this.set("userName", userName);
        return this;
    }

    public String getPassword() {
        return (String) this.get("password");
    }

    public User setPassword(String password) {
        this.set("password", password);
        return this;
    }

    public String getNickName() {
        return (String) this.get("nickName");
    }

    public User setNickName(String nickName) {
        this.set("nickName", nickName);
        return this;
    }

    public User setLoginType(String loginType) {
        this.set("loginType", loginType);
        return this;
    }

    public Long getId(){
        return getLong("id");
    }


    public String getUserName(){
        return getStr("userName");
    }

    public String getLoginType(){
        return getStr("loginType");
    }
}
