package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;


@Table(name = "base_dept", displayName = "部门", parent = "base_tree_model")
public class Dept extends Model<Dept> {
    @Id
    private Long id;
    @Column(label = "部门名称")
    private String name;

    @Tree(label = "父部门")
    private String parentId;

    @Service
    public List<Dept> search(Criteria criteria, Integer offset, Integer limit, String order){
        return getContext().callSuper(TreeModel.class,"search", criteria, offset, limit, order);
    }

}


