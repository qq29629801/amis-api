package com.yatop.lambda.base.model;

import cn.hutool.core.lang.tree.Tree;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "base_dept", displayName = "部门")
public class Dept extends Model<Dept> {

    @Id
    private Long id;

    @Column(label = "部门名称")
    private String name;
    /**
     * 父菜单ID
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Dept parentId;


    private Long getParentId(){
        return getLong("parentId");
    }

    @Service
    public List<Tree<Long>> deptTreeSelect(){
       return null;
    }
}
