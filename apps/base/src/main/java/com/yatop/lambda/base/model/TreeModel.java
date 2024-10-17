package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.annotation.Tree;
import com.yuyaogc.lowcode.engine.container.AttrConstants;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Table(name = "base_tree_model",displayName = "树模型")
public class TreeModel extends Model<TreeModel> {


    @Service(displayName = "获取子")
    public List<Model> getChildren(String key, List<Model> all, String parent) {
        return all.stream()
                .filter(treeModel -> StringUtils.equals(key, treeModel.getStr(parent)))
                .collect(Collectors.toList());
    }

    @Service(displayName = "递归子")
    public void children(List<Model> all, Model parentModel, List<Model> childList, String parent, String primary, String labelName) {
        List<Model> children = getChildren(parentModel.getStr(primary), all, parent);
        for (Model treeModel : children) {
            treeModel.put("label", treeModel.getStr(labelName));
            treeModel.put("value", treeModel.getStr(primary));
            List<Model> subMenuList = new ArrayList<>();
            children(all, treeModel, subMenuList, parent, primary, labelName);
            treeModel.put("children", subMenuList);
            childList.add(treeModel);
        }
    }

    @Service(displayName = "搜索树")
    public  List<Model> trees(Criteria criteria, Integer offset, Integer limit, String order){
      List<Model> all =  super.search(criteria, offset, limit, null);
      List<Model> results = new ArrayList<>();
      _getModelClass();

      Context context = Context.getInstance();
      EntityClass rec = context.getEntity();
      Optional<EntityField> entityFieldOp = rec.getFields().stream().filter(field -> field.getDataType() instanceof DataType.TreeField).findFirst();

      if(!entityFieldOp.isPresent()){
            throw new RuntimeException(String.format(" 模型 %s 没有 Tree注解", rec.getName()));
      }
       EntityField entityField = entityFieldOp.get();

       String parent = (String) entityField.getAttrs().get(AttrConstants.PARENT);
       String primary = (String) entityField.getAttrs().get(AttrConstants.PRIMARY);
       String labelName = (String) entityField.getAttrs().get(AttrConstants.LABEL_NAME);

       List<Model> parents = getChildren(null, all, parent);
       for(Model treeModel: parents){
           treeModel.put("label", treeModel.getStr(labelName));
           treeModel.put("value", treeModel.getStr(primary));
           List<Model> subChildList = new ArrayList<>();
           children(all, treeModel, subChildList, parent, primary, labelName);
           treeModel.put("children",subChildList);
           results.add(treeModel);
        }
       return results;
    }

}
