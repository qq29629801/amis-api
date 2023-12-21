package com.yatop.lambda.base.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatop.lambda.base.model.view.Input;
import com.yatop.lambda.base.model.view.Options;
import com.yatop.lambda.base.model.view.Rules;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Table(name = "base_ui")
public class IrUiView extends Model<IrUiView> {
    @Id
    private Long id;
    private String app;
    private String model;
    private String type;
    @Column(type = DataTypeEnum.TEXT)
    private String body;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;



    @Service
    public Map<String, Object> loadView(String app, String model) {
        Application application = Container.me().get(app);
        EntityClass entityClass = application.getEntity(model);


        JSONObject result = new JSONObject();


        JSONArray inputs = new JSONArray();


        for (EntityField field : entityClass.getFields()) {
            Input input = new Input();

            input.setKey(field.getName());
            input.setModel(field.getName());

            String displayName = field.getDisplayName();
            if(StringUtils.isEmpty(displayName)){
                displayName = field.getName();
            }
            input.setLabel(displayName);
            switch (field.getDataType().getName()){
                case Constants.STRING:
                    input.setType("input");

                    Options options = new Options();
                    options.setDisabled(false);
                    options.setWidth("100%");
                    options.setPlaceholder(String.format("请输入%s内容"  , displayName));
                    input.setOptions(options);
                    break;
                case Constants.INTEGER:
                    input.setType("number");
                    break;
                case Constants.DATE:
                    input.setType("time");
                default:{

                }
            }


            Rules rules = new Rules();
            rules.setRequired(true);
            rules.setMessage("");
            List<Rules> rulesList = new ArrayList<>();
            rulesList.add(rules);
            input.setRules(rulesList);

            inputs.add(input);
        }

        result.put("list", inputs);

        JSONObject configJson = new JSONObject();

        JSONObject spanJson = new JSONObject();
        spanJson.put("span",4);

        JSONObject span2Json = new JSONObject();
        span2Json.put("span",18);



        configJson.put("layout", "horizontal");
        configJson.put("labelCol", spanJson);
        configJson.put("wrapperCol", span2Json);
        configJson.put("hideRequiredMark", false);
        configJson.put("customStyle", "");

        result.put("config",configJson);

        return result;
    }
}
