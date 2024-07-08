package com.yuyaogc.lowcode.engine.util.views;

import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yuyaogc.lowcode.engine.util.views.CURD.*;
import static com.yuyaogc.lowcode.engine.util.views.CURD.CREATE;

public class ViewUtil {




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
    public static List<Body.Columns> buildBodyColumns(EntityClass entityClass){
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

    public static List<Object> buildDialogColumns(EntityClass entityClass, CURD curd){
        List<Object> columnsList = new ArrayList<>();
        for(EntityField entityField: entityClass.getFields()){
            //man2many
            if(Constants.MANY2ONE.equals(entityField.getDataType().getName())){

                if(curd == CREATE || curd == UPDATE){
                    EntityClass relModel =  Container.me().getEntityClass(entityField.getRelModel2());

                    Select select = new Select();
                    select.setLabel(relModel.getDisplayName());
                    select.setName("select");
                    select.setType("select");


                    List<Options> optionsList = new ArrayList<>();
                    Options options = new Options();
                    options.setLabel("a");
                    options.setValue("a");
                    optionsList.add(options);
                    select.setOptions(optionsList);
                    columnsList.add(select);
                }

            } else if(Constants.MANY2MANY.equals(entityField.getDataType().getName())){
                EntityClass relModel =  Container.me().getEntityClass(entityField.getRelModel2());


                Picker picker = new Picker();
                picker.setType("picker");
                picker.setName("type4");
                picker.setJoinValues(true);
                picker.setValueField("id");
                picker.setLabelField("engine");
                picker.setLabel("Picker");
                picker.setEmbed(false);

                //    /api/rpc/search?module=" + module + "&model=" + entityClass.getName()
                String module = relModel.getApplication().getName();
                picker.setSource(" /api/rpc/search?module="+module+"&model=" + relModel.getName());
                picker.setSize("lg");
                picker.setValue("4,5");
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


                Controls controls = new Controls();
                controls.setType("input-text");
                controls.setName("keywords");

                AddOn addOn = new AddOn();
                addOn.setType("submit");
                addOn.setLabel("搜索");

                controls.setAddOn(addOn);
                headerToolbar.setBody(Arrays.asList(controls));




                List<Object> columns = new ArrayList<>();
                for(EntityField entityField1: relModel.getFields()){
                    Body.Columns columns1 = new Body.Columns();
                    columns1.setType("text");
                    columns1.setLabel(entityField1.getDisplayName());
                    columns1.setName(entityField1.getName());
                    columns.add(columns1);
                }

                picker.setColumns(columns);
                columnsList.add(picker);

            } else {
                Dialog.Columns column = new Dialog.Columns();
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
        return columnsList;
    }


    /**
     * 构建内容
     * @param entityClass
     * @return
     */
    public static Body buildBody(EntityClass entityClass,String module){
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
    public static List<Button> buildButton(EntityClass entityClass){
        List<Button> buttons = new ArrayList<>();
        Button buttonUpdate = new Button();
        buttonUpdate.setActionType("dialog");
        buttonUpdate.setLabel("编辑");
        buttonUpdate.setType("button");
        buttonUpdate.setIcon("fa fa-pencil");
        buttonUpdate.setDialog(buildDialog(entityClass, UPDATE));
        buttons.add(buttonUpdate);

        Button buttonView = new Button();
        buttonView.setActionType("dialog");
        buttonView.setLabel("查看");
        buttonView.setType("button");
        buttonView.setIcon("fa fa-eye");
        buttonView.setDialog(buildDialog(entityClass, READ));
        buttons.add(buttonView);

        Button buttonDelete = new Button();
        buttonDelete.setIcon("fa fa-times text-danger");
        buttonDelete.setActionType("ajax");
        String module = entityClass.getApplication().getName();
        buttonDelete.setApi("delete:/api/rpc/delete?module="+module+"&model="+entityClass.getName()+"&id=$id");
        buttonDelete.setTooltip("删除");
        buttonDelete.setConfirmText("您确认要删除?");
        buttons.add(buttonDelete);

        return buttons;
    }

    public static Dialog buildDialog(EntityClass entityClass, CURD curd){
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
    public static ViewBuilder buildDefaultView(EntityClass entityClass){
        ViewBuilder view = new ViewBuilder();
        String module = entityClass.getApplication().getName();
        view.setBody(Arrays.asList(buildBody(entityClass, module)));
        view.setType("page");
        view.setName(entityClass.getName());
        view.setTitle(entityClass.getDisplayName());
        view.setToolbar(Arrays.asList(buildToolbar(entityClass)));
        return view;
    }
}
