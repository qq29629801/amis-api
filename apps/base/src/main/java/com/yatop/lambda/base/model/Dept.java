package com.yatop.lambda.base.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yatop.lambda.api.common.TreeBuildUtils;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "base_dept")
public class Dept extends Model<Dept> {

    @Id
    private Long id;

    private String deptName;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 祖级列表
     */
    private String ancestors;


    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;


    private Long getParentId(){
        return getLong("parentId");
    }

    @Service
    public List<Tree<Long>> deptTreeSelect(){
        List<Dept> depts = this.search(new Criteria(), 0,0, null);

        if (CollUtil.isEmpty(depts)) {
            return CollUtil.newArrayList();
        }

        return TreeBuildUtils.build(depts, (dept, tree) ->
                tree.setId(dept.getLong("id"))
                        .setParentId(dept.getLong("parentId"))
                        .setName(dept.getStr("deptName"))
                        .setWeight(dept.getLong("orderNum")));
    }
}
