package com.yatop.lambda.base.model.views;

import com.yatop.lambda.base.model.views.components.Curd;
import com.yatop.lambda.base.model.views.components.Toolbar;

import java.util.List;

public class Curd2 {
    private String title;
    private String remark;
    private List<Toolbar> toolbar;
    private Curd body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Toolbar> getToolbar() {
        return toolbar;
    }

    public void setToolbar(List<Toolbar> toolbar) {
        this.toolbar = toolbar;
    }

    public Curd getBody() {
        return body;
    }

    public void setBody(Curd body) {
        this.body = body;
    }
}