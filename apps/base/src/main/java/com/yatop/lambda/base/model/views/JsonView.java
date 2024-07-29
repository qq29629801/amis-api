package com.yatop.lambda.base.model.views;

import com.yatop.lambda.base.model.views.enums.Action;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.Validate;

import java.util.*;

import static com.yatop.lambda.base.model.views.enums.Action.*;
import static com.yatop.lambda.base.model.views.enums.Action.CREATE;

public class JsonView {




    /**
     * 构建过滤条件
     * @param entityClass
     * @return
     */
    public static Filter buildfilter(EntityClass entityClass){
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
    public static List<Curd1.Columns> buildModel1Columns(EntityClass entityClass){
        List<Curd1.Columns> columnsList = new ArrayList<>();
        for(EntityField entityField: entityClass.getFields()){

            Curd1.Columns column = new Curd1.Columns();
            column.setName(entityField.getName());
            column.setType("text");

            if(entityField.isPk()){
                column.setType("hidden");
            }
            if(Constants.MANY2MANY.equals(entityField.getDataType().getName()) ||
                    Constants.ONE2MANY.equals(entityField.getDataType().getName())){
                column.setType("hidden");
            }

            column.setLabel(entityField.getDisplayName());
            columnsList.add(column);
        }


        Curd1.Columns column = new Curd1.Columns();
        column.setType("operation");
        column.setLabel("操作");

        column.setButtons(buildButton(entityClass));
        columnsList.add(column);



        return columnsList;
    }

    public static List<Object> buildDialogColumns(EntityClass entityClass, Action curd){
        List<Object> columnsList = new ArrayList<>();


        Tabs tabs = new Tabs();
        tabs.setType("tabs");
        tabs.setTabsMode("card");
        List<Tabs.Tab> tabList = new ArrayList<>();
        tabs.setTabs(tabList);


        for(EntityField entityField: entityClass.getFields()){
            if(Constants.FILE.equals(entityField.getDataType().getName())){
                //{
                //          "type": "input-file",
                //          "name": "file",
                //          "label": "文件上传",
                //

                Columns columns = new Columns();
                columns.setType("input-file");
                columns.setName(entityField.getName());
                columns.setLabel(entityField.getDisplayName());

                Map<String,Object> file = new LinkedHashMap<>();
                file.put("type","input-file");
                file.put("name",entityField.getName());
                file.put("label",entityField.getDisplayName());
                file.putAll(entityField.getAttrs());

                columnsList.add(columns);
            }  else if(Constants.DICT.equals(entityField.getDataType().getName())){
                EntityClass relModel =  Container.me().getEntityClass(entityField.getRelModel());
                Select select = new Select();
                select.setLabel(entityField.getDisplayName());
                select.setName(entityField.getName());
                select.setType("select");
                select.setSearchable(true);
                String module = relModel.getApplication().getName();
                String typeCode = (String) entityField.getAttrs().get("typeCode");
                select.setSource("get:/api/rpc/lookup?perPage=10&typeCode="+typeCode+"&page=1&module="+module+"&model=" + entityField.getRelModel());
                columnsList.add(select);
            } else if(Constants.ONE2MANY.equals(entityField.getDataType().getName())){
                EntityClass relModel =   Container.me().getEntityClass(entityField.getRelModel() );
                String module = relModel.getApplication().getName();

                Tabs.Tab tab = new Tabs.Tab();
                tab.setTitle(relModel.getDisplayName());
                tabList.add(tab);


                //TODO 行内编辑
                Dialog.Body body =new Dialog.Body();
                body.setType("service");
                body.setApi("/api/rpc/search?module=" + module + "&model=" + relModel.getName());

                List<Object> bList = new ArrayList<>();
                body.setBody(bList);

                Table table = new Table();
                table.setSource("$rows");
                table.setTitle(relModel.getDisplayName());
                bList.add(table);

                List<Columns> columns =new ArrayList<>();
                 for(EntityField e: relModel.getFields()){
                     Columns columns1 = new Columns();
                     columns1.setLabel(e.getDisplayName());
                     columns1.setName(e.getName());
                     columns1.setQuickEdit(true);
                     columns.add(columns1);
                 }
                table.setColumns(columns);

                tab.setBody(body);
                //MANY2ONE
            } else if(Constants.MANY2ONE.equals(entityField.getDataType().getName())){

                if(curd == CREATE || curd == UPDATE){
                    //TODO 处理静态 字典
                    //动态
                    EntityClass relModel =  Container.me().getEntityClass(entityField.getRelModel());

                    Select select = new Select();
                    select.setLabel(relModel.getDisplayName());
                    select.setName("select");
                    select.setType("select");
                    select.setSearchable(true);
                    // TODO 指定lookup
                    String module = relModel.getApplication().getName();
                    select.setSource("get:/api/rpc/lookup?perPage=10&page=1&module="+module+"&model=" + relModel.getName());
                    columnsList.add(select);
                }
            // MANY2MANY
            } else if(Constants.MANY2MANY.equals(entityField.getDataType().getName())){

                if(curd == CREATE || curd == UPDATE){
                    EntityClass relModel =  Container.me().getEntityClass(entityField.getRelModel2());

                    Picker picker = new Picker();
                    picker.setType("picker");
                    picker.setName(entityField.getName());
                    picker.setJoinValues(true);

                    picker.setValueField("id");
                    picker.setLabelField("id");

                    picker.setLabel(relModel.getDisplayName());
                    picker.setEmbed(false);

                    //    /api/rpc/search?module=" + module + "&model=" + entityClass.getName()
                    String module = relModel.getApplication().getName();
                    picker.setSource(" /api/rpc/search?page=1&perPage=10&module="+module+"&model=" + relModel.getName());
                    picker.setSize("lg");
                    picker.setValue("");
                    picker.setMultiple(true);

                    Picker.PickerSchema pickerSchema = new Picker.PickerSchema();
                    picker.setPickerSchema(pickerSchema);
                    pickerSchema.setMode("table");
                    pickerSchema.setName("thelist");
                    pickerSchema.setQuickSaveApi("/");
                    pickerSchema.setQuickSaveItemApi("/");

                    pickerSchema.setDraggable(true);

                    Picker.HeaderToolbar headerToolbar = new Picker.HeaderToolbar();
                    headerToolbar.setMode("inline");
                    headerToolbar.setTarget("thelist");
                    headerToolbar.setWrapWithPanel(false);
                    headerToolbar.setClassName("text-right");
                    headerToolbar.setType("form");


                    Controls controls = new Controls();
                    controls.setType("input-text");
                    controls.setName("keywords");

                    AddOn addOn = new AddOn();
                    addOn.setType("submit");
                    addOn.setLabel("搜索");
                    addOn.setLevel("primary");
                    addOn.setIcon("fa fa-search pull-left");

                    controls.setAddOn(addOn);
                    headerToolbar.setBody(Arrays.asList(controls));
                    pickerSchema.setHeaderToolbar(headerToolbar);


                    Map<String,Object> foo = new HashMap<>();
                    foo.put("type","pagination");
                    foo.put("showPageInput",true);
                    foo.put("layout","perPage,pager,go");
                    pickerSchema.setFooterToolbar(Arrays.asList("statistics", foo));

                    List<Object> columns = new ArrayList<>();
                    for(EntityField entityField1: relModel.getFields()){
                        Curd1.Columns columns1 = new Curd1.Columns();
                        columns1.setType("text");
                        columns1.setLabel(entityField1.getDisplayName());
                        columns1.setName(entityField1.getName());
                        columns.add(columns1);
                    }

                    pickerSchema.setColumns(columns);
                    columnsList.add(picker);
                }



            } else {
                Columns column = new Columns();
                column.setName(entityField.getName());
                column.setType(curd.getType());

                if(curd == CREATE || curd == UPDATE){
                    switch (entityField.getDataType().getName()){
                        case Constants.BIG_DECIMAL:
                        case Constants.FLOAT:
                        case Constants.DOUBLE:
                        case Constants.INT:
                        case Constants.LONG:
                        case Constants.SHORT:
                            column.setType("input-number");
                            break;
                        case Constants.DATE:
                            column.setType("input-date");
                            break;
                        case Constants.STRING:
                            column.setType("input-text");
                            break;
                        case Constants.BOOLEAN:
                            column.setType("switch");
                            break;
                        case Constants.DATETIME:
                            column.setType("input-datetime-range");
                            break;
                        case Constants.TEXT:
                            column.setType("textarea");
                            break;
                        case Constants.TIMESTAMP:
                            column.setType("input-date");
                        default:{
                        }
                    }
                }

                if(entityField.isPk()){
                    column.setType("hidden");
                }
                column.setLabel(entityField.getDisplayName());

                for(Validate validate: entityField.getValidates().values()){
                    column.setRequired(validate.isEmpty());
                }

                columnsList.add(column);
            }

        }


        columnsList.add(tabs);

        return columnsList;
    }


    /**
     * 构建内容
     * @param entityClass
     * @return
     */
    public static Curd1 buildCurd(EntityClass entityClass, String module){
        Curd1 body = new Curd1();

        body.setFilter(buildfilter(entityClass));

        body.setColumns(buildModel1Columns(entityClass));

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
    public static List<Button> buildButton(EntityClass entityClass){
        List<Button> buttons = new ArrayList<>();
        Button buttonUpdate = new Button();
        buttonUpdate.setActionType("dialog");
        buttonUpdate.setLabel("编辑");
        buttonUpdate.setType("button");
        //TODO
        buttonUpdate.setIcon("fa fa-pencil");
        buttonUpdate.setDialog(buildDialog(entityClass, UPDATE));
        buttons.add(buttonUpdate);

        Button buttonView = new Button();
        buttonView.setActionType("dialog");
        buttonView.setLabel("查看");
        buttonView.setType("button");

        // TODO
        buttonView.setIcon("fa fa-eye");
        buttonView.setDialog(buildDialog(entityClass, READ));
        buttons.add(buttonView);

        Button buttonDelete = new Button();
        //TODO
        buttonDelete.setIcon("fa fa-times text-danger");
        buttonDelete.setActionType("ajax");
        String module = entityClass.getApplication().getName();
        buttonDelete.setApi("delete:/api/rpc/delete?module="+module+"&model="+entityClass.getName()+"&id=$id");
        buttonDelete.setTooltip("删除");
        buttonDelete.setConfirmText("您确认要删除?");
        buttons.add(buttonDelete);

        return buttons;
    }

    public static Dialog buildDialog(EntityClass entityClass, Action curd){
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
    public static Toolbar buildToolbar(EntityClass entityClass){
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
    public static Page buildPage(EntityClass entityClass){
        String module = entityClass.getApplication().getName();

        Page page = new Page();
        page.setBody(Arrays.asList(buildCurd(entityClass, module)));

        page.setName(entityClass.getName());
        page.setTitle(entityClass.getDisplayName());

        page.setToolbar(Arrays.asList(buildToolbar(entityClass)));
        return page;
    }
}
