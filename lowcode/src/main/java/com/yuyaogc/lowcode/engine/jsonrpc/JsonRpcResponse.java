package com.yuyaogc.lowcode.engine.jsonrpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * json rpc 响应
 *
 *
 */
public class JsonRpcResponse {
    RpcId id;
    String msg;

    int status;

    @JsonInclude(Include.NON_NULL)
    Object data;

    public JsonRpcResponse() {
    }



    public JsonRpcResponse(Object data) {
        this.data = data;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public RpcId getId() {
        return id;
    }

    public void setId(RpcId id) {
        this.id = id;
    }
}
