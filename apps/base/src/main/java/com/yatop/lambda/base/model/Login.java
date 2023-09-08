package com.yatop.lambda.base.model;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.yatop.lambda.api.common.*;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Table(name = "base_login")
public class Login extends Model<Login> {



    @Service
    public Map<String,Object> getInfo(){
        LoginUser loginUser = LoginHelper.getLoginUser();
        User user = new User().findById(loginUser.getUserId());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roles", loginUser.getRolePermission());
        ajax.put("permissions", loginUser.getMenuPermission());
        return ajax;
    }


    @Service
    private boolean checkLogin(String loginPass, String password) {
       return !BCrypt.checkpw(password, loginPass);
    }

    @Service
    public Map<String, Object> login(UserVo userVo) {
        if(StringUtil.isEmpty(userVo.getUsername())){
            throw new EngineException(String.format("%s", "用户名为空"));
        }

        if(StringUtil.isEmpty(userVo.getPassword())){
            throw new EngineException(String.format("%s", "密码为空"));
        }

        User user = new User();
        List<User> userList = user.search(Criteria.equal("userName", userVo.getUsername()), 0,1, null);
        if(userList.isEmpty()){

        }
        user = userList.get(0);

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getLong("id"));
        loginUser.setUsername(user.getStr("userName"));
        loginUser.setUserType(user.getStr("loginType"));



        RoleUser roleUser = new RoleUser();
        List<RoleUser> roleUserList = roleUser.search(Criteria.equal("userId", user.getLong("id")), 0,0, null);


        if(roleUserList.isEmpty()){

        }
        roleUser = roleUserList.get(0);





        if("admin".equals(roleUser.getStr("roleKey"))){
            loginUser.setRolePermission(new HashSet<>(Arrays.asList("admin")));
            loginUser.setMenuPermission(new HashSet<>(Arrays.asList("*:*:*")));
        }


        List<Long> roleIds = roleUserList.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
        RoleMenu roleMenu = new RoleMenu();

        List<RoleMenu> roleMenuList = roleMenu.search(Criteria.equal("roleId",roleIds.get(0)),0,0,null);
        if(!roleMenuList.isEmpty()){
            Set<String> permsSet = roleMenuList.stream().map(RoleMenu::getPerms).collect(Collectors.toSet());
            loginUser.setMenuPermission(permsSet);
        }

        List<RoleDTO> roles = BeanUtil.copyToList(roleUserList, RoleDTO.class);
        loginUser.setRoles(roles);

        LoginHelper.loginByDevice(loginUser, DeviceType.PC);




        Map<String,Object> loginResult = new HashMap<>();
        loginResult.put("token", StpUtil.getTokenValue());

        return loginResult;
    }

    @Service
    public void logout(){

    }

}
