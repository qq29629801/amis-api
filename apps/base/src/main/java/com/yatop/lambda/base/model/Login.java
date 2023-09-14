package com.yatop.lambda.base.model;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            StpUtil.logout();
        } catch (Exception e) {
        }
    }



    private List<RouterVo> buildMenus(List<Menu> menus){
        List<RouterVo> routers = new LinkedList<>();
        for (Menu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getStr("visible")));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getStr("menuName"), menu.getStr("icon"), StringUtils.equals("1", menu.getStr("isCache")), menu.getStr("path")));
            //TODO one2many   (List<Menu>) menu.get("children");
            List<Menu> cMenus = menu.getChildren(menu.getLong("id"));
            if (CollUtil.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getStr("menuType"))) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getStr("path"));
                children.setComponent(menu.getStr("component"));
                children.setName(StringUtils.capitalize(menu.getStr("path")));
                children.setMeta(new MetaVo(menu.getStr("menuName"), menu.getStr("icon"), StringUtils.equals("1", menu.getStr("isCache")), menu.getStr("path")));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getLong("parentId").intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getStr("menuName"), menu.getStr("icon")));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getStr("path"));
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getStr("menuName"), menu.getStr("icon"), menu.getStr("path")));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, "."},
                new String[]{"", "", "", "/"});
    }

    @Service
    public List<RouterVo> getRouters(){
        List<Menu> menus = new Menu().search(Criteria.equal("parentId", 0), 0,0,"orderNum desc");

        return buildMenus(menus);
    }

    public String getRouteName(Menu menu) {
        String routerName = StringUtils.capitalize(menu.getStr("path"));
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu) {
        String routerPath = menu.getStr("path");
        // 内链打开外网方式
        if (menu.getLong("parentId").intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getLong("parentId").intValue() && UserConstants.TYPE_DIR.equals(menu.getStr("menuType"))
                && UserConstants.NO_FRAME.equals(menu.getStr("isFrame"))) {
            routerPath = "/" + menu.getStr("path");
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getStr("component")) && !isMenuFrame(menu)) {
            component = menu.getStr("component");
        } else if (StringUtils.isEmpty(menu.getStr("component")) && menu.getLong("parentId").intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getStr("component")) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(Menu menu) {
        return menu.getLong("parentId").intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getStr("menuType"))
                && menu.getStr("isFrame").equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(Menu menu) {
        return menu.getStr("isFrame").equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getStr("path"));
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(Menu menu) {
        return menu.getLong("parentId").intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getStr("menuType"));
    }

}
