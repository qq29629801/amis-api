package com.yatop.lambda.base.model;

import cn.hutool.core.lang.tree.Tree;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

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
    private  Long parentId;


    public Long getParentId(){
        return getLong("parentId");
    }

    public List<IrUiMenu> getChildren(Long id){
        return search(Criteria.equal("parentId", id),0,0,null);
    }


    @Service
    public List<Tree<Long>> treeselect(){
        return null;
    }


    @Service
    public List<Map<String,Object>> loadMenus(){
        List<IrUiMenu> menuList =  this.search(new Criteria(),0,0, null);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(IrUiMenu uiMenu: menuList){
            Map<String,Object> menu = new LinkedHashMap<>();

            menu.put("label",uiMenu.get("name"));
            menu.put("schemaApi","/crud");

            mapList.add(menu);
        }
        //TODO
        return mapList;
    }
}
