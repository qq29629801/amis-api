package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
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

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private String nickName;

    @Column
    private String loginType;

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


    @Service(displayName = "查询角色未授权用户列表")
    public List<User> unallocatedUserList(Long roleId){
        RoleUser roleUser = new RoleUser();
        List<RoleUser> roleUserList = roleUser.search(Criteria.equal("roleId", roleId), 0, 0, null);

        List<Long> usrIds = roleUserList.stream().map(RoleUser::getUserId).collect(Collectors.toList());

       List<User> userList = this.search(Criteria.notIn("id", Optional.of(usrIds)), 0, 0, null);

        return userList;
    }


    @Service(displayName = "findUsers")
    public Set<User> findUsers(Set<User> users){
        return users;
    }

    @Service(displayName = "findUser")
    public User findUser(User user){
        return user;
    }

}
