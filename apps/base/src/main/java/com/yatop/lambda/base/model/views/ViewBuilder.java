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
public class ViewBuilder {

    private String type;
    private String title;
    private String remark;
    private String name;
    private List<Toolbar> toolbar;
    private List<Body> body;
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

    public void setBody(List<Body> body) {
         this.body = body;
     }
     public List<Body> getBody() {
         return body;
     }

}