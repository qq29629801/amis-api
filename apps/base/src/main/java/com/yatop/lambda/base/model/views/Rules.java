package com.yatop.lambda.base.model.views;

import java.io.Serializable;

public class Rules implements Serializable {
    private boolean required;
    private String message;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
