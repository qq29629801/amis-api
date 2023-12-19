package com.yuyaogc.lowcode.engine.rpc;

public interface Invoker<T> {
    T invoke(Invocation invocation) throws Throwable;
}