package com.yatop.lambda.base.model;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.yatop.lambda.api.common.DeviceType;
import com.yatop.lambda.api.common.LoginHelper;
import com.yatop.lambda.api.common.LoginUser;
import com.yatop.lambda.api.common.RoleDTO;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "base_login")
public class Login extends Model<Login> {



    @Service
    private boolean checkLogin(String loginPass, String password) {
       return !BCrypt.checkpw(password, loginPass);
    }

    @Service
    public Map<String, Object> login(Map<String, String[]> args) {
        String[] login = args.get("login");
        if(StringUtil.isEmpty(login[0])){
        }

        String[] pass = args.get("pass");
        if(StringUtil.isEmpty(pass[0])){

        }

        User user = new User();
        List<User> userList = user.search(Criteria.equal("userName", login[0]), 0,1, null);
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


        Role role = new Role();
        List<Role> roleList = role.search(Criteria.equal("id", roleUser.getLong("userId")),0,0,null);
        if(roleList.isEmpty()){
        }

        List<Long> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());
        RoleMenu roleMenu = new RoleMenu();
        List<RoleMenu> roleMenuList = roleMenu.search(Criteria.equal("roleId",roleIds.get(0)),0,0,null);

        roleMenu = roleMenuList.get(0);

        Menu menu = new Menu();
        List<Menu> menuList = menu.search(Criteria.equal("id", roleMenu.getLong("menuId")),0,0,null);
        Set<String> permsSet = menuList.stream().map(Menu::getPerms).collect(Collectors.toSet());

        loginUser.setMenuPermission(permsSet);
        loginUser.setRolePermission(null);



        List<RoleDTO> roles = BeanUtil.copyToList(roleList, RoleDTO.class);
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
