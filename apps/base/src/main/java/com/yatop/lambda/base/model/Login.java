package com.yatop.lambda.base.model;

import cn.dev33.satoken.secure.BCrypt;
import com.yatop.lambda.api.common.Constants;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Table(name = "base_login")
public class Login extends Model<Login> {



    @Service
    public Map<String,Object> getInfo(){
        Map<String, Object> ajax = new HashMap<>();
        return ajax;
    }


    @Service
    private boolean checkLogin(String loginPass, String password) {
       return !BCrypt.checkpw(password, loginPass);
    }

    @Service
    public Map<String, Object> login() {
        return null;
    }

    @Service
    public void logout(){

    }



    private List<Map<String,Object>> buildMenus(List<IrUiMenu> menus){
        return null;
    }

    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, "."},
                new String[]{"", "", "", "/"});
    }

    @Service
    public List<Map<String,Object>> getRouters(){
        List<IrUiMenu> menus = new IrUiMenu().search(Criteria.equal("parentId", 0), 0,0,"orderNum desc");
        return buildMenus(menus);
    }


}
