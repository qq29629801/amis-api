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
public class Dialog {

    private String title;
    private String name;
    private Body body;
    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }


    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public class Body {

        private String type;
        private String name;
        private String api;
        private List<Columns> body;

        public List<Columns> getBody() {
            return body;
        }

        public void setBody(List<Columns> body) {
            this.body = body;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }


}