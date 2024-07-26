/**
  * Copyright 2024 bejson.com 
  */
package com.yatop.lambda.base.model.views;
import java.util.List;

/**
 * Auto-generated: 2024-06-26 22:45:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Page {
    private String type = "page";
    private String title;
    private String remark;
    private String name;

    private List<Toolbar> toolbar;
    private List<Object> body;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setToolbar(List<Toolbar> toolbar) {
         this.toolbar = toolbar;
     }
     public List<Toolbar> getToolbar() {
         return toolbar;
     }


    public List<Object> getBody() {
        return body;
    }

    public void setBody(List<Object> body) {
        this.body = body;
    }
}