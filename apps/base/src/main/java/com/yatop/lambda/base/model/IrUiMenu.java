package com.yatop.lambda.base.model;

import cn.hutool.core.lang.tree.Tree;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.KvMap;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.util.*;

@Table(name = "base_menu")
public class IrUiMenu extends Model<IrUiMenu> {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String url;
    @Column
    private String model;
    @Column
    private String click;

    @Column
    private String css;

    @Column
    private String view;

    @Column
    private Integer sequence;

    /**
     * 菜单状态（0正常 1停用）
     */
    @Column
    private String status;

    /**
     * 权限字符串
     */
    @Column
    private String perms;

    /**
     * 菜单图标
     */
    @Column
    private String icon;
    /**
     * 备注
     */
    @Column
    private String remark;

    @Column
    private String key;


    public String getPerms(){
        return getStr("perms");
    }

    //@OneToMany
    //private List<Menu> children;


    @Column(name = "parent_id")
    private  String parentId;


    public String getParentId(){
        return getStr("parentId");
    }

    public List<IrUiMenu> getChildren(String id){
        return search(Criteria.equal("parentId", id),0,0,null);
    }


    @Service
    public List<Tree<Long>> treeselect(){
        return null;
    }


    @Service
    public Map<String,Object> loadMenus(){
        List<IrUiMenu> menus =  this.search(new Criteria(),0,0, null);

        List<KvMap> menuList = new ArrayList<>();
        KvMap result = new KvMap();
        result.put("pages", menuList);


        KvMap index = new KvMap();
        index.put("label", "Home");
        index.put("url", "/");
        index.put("redirect", "/index/1");
        menuList.add(index);

        for(IrUiMenu uiMenu: menus){

            String parentId = uiMenu.getParentId();
            if(StringUtils.isEmpty(parentId)){
                KvMap menu = new KvMap();
                menu.put("label",uiMenu.get("name"));
                menu.put("schemaApi","/api/rpc/views");

                List<KvMap> menu1List = new ArrayList<>();
                List<IrUiMenu> parents = getChildren(uiMenu.getStr("key"));
                //TODO
                for(IrUiMenu irUiMenu: parents){
                    KvMap menu1 = new KvMap();
                    menu1.put("label",irUiMenu.get("name"));
                    menu1.put("schemaApi","/api/rpc/views");
                    menu1List.add(menu1);
                }
                menu.put("children",menu1List);
                menuList.add(menu);
            }

        }
        //TODO
        return result;
    }
}
