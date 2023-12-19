package com.yuyaogc.lowcode.engine.rpc;

import java.util.List;
import java.util.Map;

public interface Invocation {

    Object[] getArguments();


    Map<String, String> getAttachments();


    Invoker<?> getInvoker();


    void addInvokedInvoker(Invoker<?> invoker);


    List<Invoker<?>> getInvokedInvokers();
}