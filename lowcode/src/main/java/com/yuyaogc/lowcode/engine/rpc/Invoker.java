package com.yuyaogc.lowcode.engine.rpc;

public interface Invoker<T> {
    Result invoke(Invocation invocation) throws RpcException;
}