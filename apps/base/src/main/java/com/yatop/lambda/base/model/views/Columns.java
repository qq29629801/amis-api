/**
  * Copyright 2024 bejson.com 
  */
package com.yatop.lambda.base.model.views;

/**
 * Auto-generated: 2024-06-26 22:45:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Columns {

    private String name;
    private String label;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean sortable;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setLabel(String label) {
         this.label = label;
     }
     public String getLabel() {
         return label;
     }

    public void setSortable(boolean sortable) {
         this.sortable = sortable;
     }
     public boolean getSortable() {
         return sortable;
     }

}