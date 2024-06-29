package com.yatop.lambda.base.model;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.yatop.lambda.base.model.views.*;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.Validate;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CustomJsonNodeFactory;
import com.yuyaogc.lowcode.engine.util.CustomParserFactory;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.util.*;

import static com.yatop.lambda.base.model.views.CURD.*;

@Table(name = "base_ui_view")
public class IrUiView extends Model<IrUiView> {
    @Id
    private Long id;
    @Column(label = "视图名")
    private String name;
    @Column(label = "模型")
    private String model;
    @Column(label = "唯一")
    private String key;
    @Column(label = "模式")
    private String mode;
    @Column(label = "激活")
    private boolean active;
//    @ManyToOne
//    @JoinColumn(name = "inherit_id")
//    private IrUiView inherit;

    @Column(type = DataTypeEnum.TEXT,label = "内容")
    private String arch;
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




    public Long getId() {
        return (Long) this.get("id");
    }

    public IrUiView setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public IrUiView setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getModel() {
        return (String) this.get("model");
    }

    public IrUiView setModel(String model) {
        this.set("model", model);
        return this;
    }

    public String getKey() {
        return (String) this.get("key");
    }

    public IrUiView setKey(String key) {
        this.set("key", key);
        return this;
    }

    public String getMode() {
        return (String) this.get("mode");
    }

    public IrUiView setMode(String mode) {
        this.set("mode", mode);
        return this;
    }

    public boolean isActive() {
        return (boolean) this.get("active");
    }

    public IrUiView setActive(boolean active) {
        this.set("active", active);
        return this;
    }

    public IrUiView setArch(String arch) {
        this.set("arch", arch);
        return this;
    }

    public String getArch() {
        return (String) this.get("arch");
    }

    public String getCreateBy() {
        return (String) this.get("createBy");
    }

    public IrUiView setCreateBy(String createBy) {
        this.set("createBy", createBy);
        return this;
    }

    public Date getCreateTime() {
        return (Date) this.get("createTime");
    }

    public IrUiView setCreateTime(Date createTime) {
        this.set("createTime", createTime);
        return this;
    }

    public String getUpdateBy() {
        return (String) this.get("updateBy");
    }

    public IrUiView setUpdateBy(String updateBy) {
        this.set("updateBy", updateBy);
        return this;
    }

    public Date getUpdateTime() {
        return (Date) this.get("updateTime");
    }

    public IrUiView setUpdateTime(Date updateTime) {
        this.set("updateTime", updateTime);
        return this;
    }


    /**
     * 构建过滤条件
     * @param entityClass
     * @return
     */
    private Filter buildfilter(EntityClass entityClass){
        Filter filter = new Filter();
        filter.setMode("inline");
        filter.setClassName("m-b-sm");
        filter.setClassName("m-b-sm");

        List<Controls> controlsList = new ArrayList<>();
        Controls controls = new Controls();
        controls.setName("keywords");
        controls.setType("text");

        AddOn addOn = new AddOn();
        addOn.setLabel("搜索");
        addOn.setType("submit");
        addOn.setClassName("btn-success");
        controls.setAddOn(addOn);
        controlsList.add(controls);
        filter.setControls(controlsList);
        return filter;
    }


    /**
     * 构建字段
     * @param entityClass
     * @return
     */
    private List<Body.Columns> buildBodyColumns(EntityClass entityClass){
        List<Body.Columns> columnsList = new ArrayList<>();
        for(EntityField entityField: entityClass.getFields()){

            Body.Columns column = new Body.Columns();
            column.setName(entityField.getName());
            column.setType("text");
            if(entityField.isPk()){
                column.setType("hidden");
            }
            column.setLabel(entityField.getDisplayName());
            columnsList.add(column);
        }


        Body.Columns column = new Body.Columns();
        column.setType("operation");
        column.setLabel("操作");
        column.setButtons(buildButton(entityClass));
        columnsList.add(column);



        return columnsList;
    }

    private List<Dialog.Columns> buildDialogColumns(EntityClass entityClass, CURD curd){
        List<Dialog.Columns> columnsList = new ArrayList<>();
        for(EntityField entityField: entityClass.getFields()){

            Dialog.Columns column = new Dialog.Columns();
            column.setName(entityField.getName());
            column.setType(curd.getType());
            if(entityField.isPk()){
                column.setType("hidden");
            }
            column.setLabel(entityField.getDisplayName());

            for(Validate validate: entityField.getValidates().values()){
                column.setRequired(validate.isEmpty());
            }

            columnsList.add(column);
        }
        return columnsList;
    }


    /**
     * 构建内容
     * @param entityClass
     * @return
     */
    private Body buildBody(EntityClass entityClass,String module){
        Body body = new Body();
        body.setFilter(buildfilter(entityClass));
        body.setColumns(buildBodyColumns(entityClass));
        body.setType("crud");
        body.setApi("/api/rpc/search?module=" + module + "&model=" + entityClass.getName());
        body.setColumnsTogglable("auto");
        body.setTableClassName("table-db table-striped");
        body.setHeaderClassName("crud-table-header");
        body.setFooterClassName("crud-table-footer");
        body.setToolbarClassName("crud-table-toolbar");
        body.setBodyClassName("panel-default");
        body.setCombineNum(0);
        body.setName("sample");
        body.setAffixHeader(true);
        return body;
    }


    /**
     * 按钮
     * @param entityClass
     * @return
     */
    private List<Button> buildButton(EntityClass entityClass){
        Button buttonUpdate = new Button();
        buttonUpdate.setActionType("dialog");
        buttonUpdate.setLabel("编辑");
        buttonUpdate.setType("button");
        buttonUpdate.setIcon("fa fa-pencil");
        buttonUpdate.setDialog(buildDialog(entityClass, UPDATE));

        Button buttonView = new Button();
        buttonView.setActionType("dialog");
        buttonView.setLabel("查看");
        buttonView.setType("button");
        buttonView.setIcon("fa fa-eye");
        buttonView.setDialog(buildDialog(entityClass, READ));


        Button buttonDelete = new Button();
        buttonDelete.setIcon("fa fa-times text-danger");
        buttonDelete.setActionType("ajax");
        String module = entityClass.getApplication().getName();
        buttonDelete.setApi("delete:/api/rpc/delete?module="+module+"&model="+entityClass.getName()+"&id=$id");
        buttonDelete.setTooltip("删除");
        buttonDelete.setConfirmText("您确认要删除?");

        return Arrays.asList(buttonView, buttonUpdate, buttonDelete);
    }

    private Dialog buildDialog(EntityClass entityClass, CURD curd){
        Dialog dialog = new Dialog();
        dialog.setTitle(curd.getTitle());
        dialog.setName("new");

        Dialog.Body body = new Dialog.Body();

        String module = entityClass.getApplication().getName();
        switch (curd){
            case CREATE:
                body.setApi("/api/rpc/create?module="+module+"&model="+ entityClass.getName());
                break;
            case UPDATE:
                body.setApi("/api/rpc/update?module="+module+"&model="+ entityClass.getName());
                break;
            case READ:
        }


        body.setType("form");
        body.setName("sample-edit-form");

        body.setBody(buildDialogColumns(entityClass, curd));
        dialog.setBody(body);
        return dialog;
    }

    /**
     * 工具栏
     * @param entityClass
     * @return
     */
    private Toolbar buildToolbar(EntityClass entityClass){
        Toolbar toolbar = new Toolbar();
        toolbar.setType("button");
        toolbar.setActionType("dialog");
        toolbar.setLabel("新增");
        toolbar.setPrimary(true);
        toolbar.setDialog(buildDialog(entityClass, CREATE));
        return toolbar;
    }

    /**
     * 构建默认视图
     * @param entityClass
     * @return
     */
    private ViewBuilder buildDefaultView(EntityClass entityClass){
        ViewBuilder view = new ViewBuilder();
        String module = entityClass.getApplication().getName();
        view.setBody(Arrays.asList(buildBody(entityClass, module)));
        view.setType("page");
        view.setName(entityClass.getName());
        view.setTitle(entityClass.getDisplayName());
        view.setToolbar(Arrays.asList(buildToolbar(entityClass)));
        return view;
    }

    @Service
    public String loadView(String key, String model, String module) {
        IrUiView uiView = new IrUiView();
        List<IrUiView> views = uiView.search(Criteria.equal("key", key), 0, 0, "");
        IrUiView primary = null;
        List<IrUiView> extension = new ArrayList<>();
        for (IrUiView view : views) {
          //  if (primary == null && "primary".equals(view.get("mode"))) {
                primary = view;
//            }
//            if ("extension".equals(view.get("mode"))) {
//                extension.add(view);
//            }
        }
        if (primary == null) {
            Container container = Container.me();
            Application app = container.get(module);
            EntityClass entityClass =  app.getEntity(model);
            ViewBuilder view =   buildDefaultView(entityClass);
            return JSON.toJSONString(view);
            //throw new EngineException("找不到视图");
        }

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(primary.getArch().getBytes());
            CustomParserFactory customParserFactory = new CustomParserFactory();
            ObjectMapper mapper = new ObjectMapper(customParserFactory);
            CustomJsonNodeFactory factory = new CustomJsonNodeFactory(mapper.getDeserializationConfig().getNodeFactory(),
                    customParserFactory);
            mapper.setConfig(mapper.getDeserializationConfig().with(factory));
            Configuration config = Configuration.builder().mappingProvider(new JacksonMappingProvider(mapper))
                    .jsonProvider(new JacksonJsonNodeJsonProvider(mapper)).options(Option.ALWAYS_RETURN_LIST)
                    .options(Option.SUPPRESS_EXCEPTIONS).build();
            JsonContext context = (JsonContext) JsonPath.parse(inputStream, config);
            return context.jsonString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void combined(Elements base, Elements data) {
        for (Element el : data) {
            String position = el.attr("position");
            if ("xpath".equals(el.tagName())) {
                String has = el.attr("has");
                if (StringUtils.isNotEmpty(has)) {
                    Elements found = base.select(has);
                    if (found.size() == 0) {
                        continue;
                    }
                }
                String hasNot = el.attr("has_not");
                if (StringUtils.isNotEmpty(hasNot)) {
                    Elements found = base.select(hasNot);
                    if (found.size() > 0) {
                        continue;
                    }
                }
                String expr = el.attr("expr");
                Elements selects = base.select(expr);
                for (Element select : selects) {
                    combined(position, select, el.childNodes().toArray(new Node[el.childNodeSize()]));
                }
            } else {
                for (Element select : base) {
                    combined(position, select, el);
                }
            }
        }
    }

    static void combined(String position, Element target, Node... nodes) {
        if ("before".equals(position)) {
            for (Node node : nodes) {
                target.before(node);
            }
        } else if ("after".equals(position)) {
            for (Node node : nodes) {
                target.after(node);
            }
        } else if ("replace".equals(position)) {
            boolean first = true;
            for (Node node : nodes) {
                if (first) {
                    first = false;
                    target.replaceWith(node);
                } else {
                    target.after(node);
                }
            }
        } else if ("inside".equals(position)) {
            target.appendChildren(Arrays.asList(nodes));
        } else if ("attribute".equals(position)) {
            for (Node node : nodes) {
                target.attributes().addAll(node.attributes());
            }
        }
    }

    Elements getData(Document doc) {
        for (Element el : doc.children()) {
            if ("data".equals(el.tagName())) {
                return el.children();
            }
        }
        return doc.children();
    }
}
