package com.yatop.lambda.base.model.views;

public enum CURD {
    CREATE("text","创建"),
    DELETE("",""),
    UPDATE("input-text","更新"),
    READ("static", "查看");

    CURD(String type,String title){
        this.type = type;
        this.title = title;
    }

    private String title;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
