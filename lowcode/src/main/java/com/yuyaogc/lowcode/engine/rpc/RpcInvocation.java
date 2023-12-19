package com.yuyaogc.lowcode.engine.rpc;

import java.util.List;
import java.util.Map;

public class RpcInvocation implements Invocation {

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Map<String, String> getAttachments() {
        return null;
    }

    @Override
    public Invoker<?> getInvoker() {
        return null;
    }

    @Override
    public void addInvokedInvoker(Invoker<?> invoker) {

    }

    @Override
    public List<Invoker<?>> getInvokedInvokers() {
        return null;
    }
}