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
    /**
     * 父菜单ID
     */
    @Tree(label = "父")
    private String parentId;


    private Long getParentId(){
        return getLong("parentId");
    }

    @Service(displayName = "搜索")
    public  List<Dept> search(Criteria criteria, Integer offset, Integer limit, String order) {
        List<Dept> deptList =  super.search(new Criteria(), 0, 0,null);

        List<Dept> results = getContext().get("base.base_tree_model").call("trees", deptList, "parentId","id");

        return results;
    }
}


