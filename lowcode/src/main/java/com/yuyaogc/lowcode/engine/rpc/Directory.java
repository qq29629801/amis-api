package com.yuyaogc.lowcode.engine.rpc;

import java.util.List;

public interface Directory<T> {
    List<Invoker<T>> list(Invocation invocation) throws Throwable;
}