package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Table(name = "base_tree_model",displayName = "树模型")
public class TreeModel extends Model<TreeModel> {
    @Id
    private Long id;

    @Service(displayName = "获取子")
    public List<Model> getChildren(String key, List<Model> all, String parent) {
        return all.stream()
                .filter(treeModel -> StringUtils.equals(key, treeModel.getStr(parent)))
                .collect(Collectors.toList());
    }

    @Service(displayName = "递归子")
    public void children(List<Model> all, Model parentModel, List<Model> childList, String parent, String primary) {
        List<Model> children = getChildren(parentModel.getStr(primary), all, parent);
        for (Model model : children) {
            List<Model> subMenuList = new ArrayList<>();
            children(all, model, subMenuList, parent, primary);
            model.put("children", subMenuList);
            childList.add(model);
        }
    }

    @Service(displayName = "获取树")
    public  List<Model> trees(List<Model> all ,String parent,String primary){
       List<Model> parents = getChildren(null, all, parent);
       List<Model> results = new ArrayList<>();
       for(Model model: parents){
            List<Model> subChildList = new ArrayList<>();
            children(all, model, subChildList, parent, primary);
            model.put("children",subChildList);
           results.add(model);
        }
       return results;
    }

}
