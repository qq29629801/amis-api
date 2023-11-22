package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;
import java.util.Optional;
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
}
