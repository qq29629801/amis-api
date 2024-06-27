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
public class Body {

    private String type;
    private String name;
    private String api;


    private Filter filter;
    private List<BulkActions> bulkActions;
    private List<Columns> columns;
    private boolean affixHeader;
    private String columnsTogglable;
    private String placeholder;
    private String tableClassName;
    private String headerClassName;
    private String footerClassName;
    private String toolbarClassName;
    private int combineNum;
    private String bodyClassName;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setApi(String api) {
         this.api = api;
     }
     public String getApi() {
         return api;
     }

    public void setFilter(Filter filter) {
         this.filter = filter;
     }
     public Filter getFilter() {
         return filter;
     }

    public void setBulkActions(List<BulkActions> bulkActions) {
         this.bulkActions = bulkActions;
     }
     public List<BulkActions> getBulkActions() {
         return bulkActions;
     }

    public void setColumns(List<Columns> columns) {
         this.columns = columns;
     }
     public List<Columns> getColumns() {
         return columns;
     }

    public void setAffixHeader(boolean affixHeader) {
         this.affixHeader = affixHeader;
     }
     public boolean getAffixHeader() {
         return affixHeader;
     }

    public void setColumnsTogglable(String columnsTogglable) {
         this.columnsTogglable = columnsTogglable;
     }
     public String getColumnsTogglable() {
         return columnsTogglable;
     }

    public void setPlaceholder(String placeholder) {
         this.placeholder = placeholder;
     }
     public String getPlaceholder() {
         return placeholder;
     }

    public void setTableClassName(String tableClassName) {
         this.tableClassName = tableClassName;
     }
     public String getTableClassName() {
         return tableClassName;
     }

    public void setHeaderClassName(String headerClassName) {
         this.headerClassName = headerClassName;
     }
     public String getHeaderClassName() {
         return headerClassName;
     }

    public void setFooterClassName(String footerClassName) {
         this.footerClassName = footerClassName;
     }
     public String getFooterClassName() {
         return footerClassName;
     }

    public void setToolbarClassName(String toolbarClassName) {
         this.toolbarClassName = toolbarClassName;
     }
     public String getToolbarClassName() {
         return toolbarClassName;
     }

    public void setCombineNum(int combineNum) {
         this.combineNum = combineNum;
     }
     public int getCombineNum() {
         return combineNum;
     }

    public void setBodyClassName(String bodyClassName) {
         this.bodyClassName = bodyClassName;
     }
     public String getBodyClassName() {
         return bodyClassName;
     }


}