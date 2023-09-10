package com.yatop.lambda.base.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yatop.lambda.api.common.TreeBuildUtils;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "base_menu")
public class Menu extends Model<Menu> {
    @Id
    private Long id;

    /**
     * 菜单名称
     */
    @Column
    private String menuName;

    /**
     * 显示顺序
     */
    @Column
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Column
    private String path;

    /**
     * 组件路径
     */
    @Column
    private String component;

    /**
     * 路由参数
     */
    @Column
    private String queryParam;

    /**
     * 是否为外链（0是 1否）
     */
    @Column
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @Column
    private String isCache;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @Column
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @Column
    private String visible;

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

    public List<Menu> getChildren(Long id){
        return search(Criteria.equal("parentId", id),0,0,null);
    }


    @Service
    public List<Tree<Long>> treeselect(){
        List<Menu> menus = this.search(new Criteria(), 0, 0, null);
        if (CollUtil.isEmpty(menus)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(menus, (menu, tree) ->
                tree.setId(menu.getLong("id"))
                        .setParentId(menu.getLong("parentId"))
                        .setName(menu.getStr("menuName"))
                        .setWeight(menu.getInt("orderNum")));
    }
}
