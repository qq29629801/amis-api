package com.yatop.lambda.api.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.yatop.lambda.api.common.LoginUser;
import com.yatop.lambda.api.common.UserVo;
import com.yatop.lambda.api.service.FileService;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.ContextHandler;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcError;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcRequest;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcResponse;
import com.yuyaogc.lowcode.engine.jsonrpc.RpcId;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/rpc")
public class RpcController {
    private final Logger logger = LoggerFactory.getLogger(RpcController.class);
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public JsonRpcResponse handleFileUpload(@RequestParam("file") MultipartFile file) {
        return new JsonRpcResponse(new RpcId(IdUtil.fastUUID()), fileService.saveJar(file));
    }

    @RequestMapping("/service")
    public JsonRpcResponse service(@RequestBody JsonRpcRequest request, @RequestHeader HttpHeaders headers) {
        try (Context context = new Context(null, Db.getConfig())) {
            Map<String, Object> params = request.getParams().getMap();
            context.setArguments(params);
            ContextHandler.setContext(context);
            context.get("base","Router").call("check",params);
        } catch (Exception e) {
            return new JsonRpcResponse(request.getId(), new JsonRpcError(401, "无权限访问"));
        }
        try (Context context = new Context(Objects.toString(StpUtil.getLoginId()), Db.getConfig())) {
            Map<String, Object> params = request.getParams().getMap();
            context.setArguments(params);
            ContextHandler.setContext(context);
            context.call();
            return new JsonRpcResponse(request.getId(), context.getResult());
        } catch (Exception e) {
            logger.error("==========={}",e);
            return new JsonRpcResponse(request.getId(), JsonRpcError.createInternalError(e));
        }
    }

    @PostMapping("/login")
    public JsonRpcResponse login(@RequestBody UserVo userVo, @RequestHeader HttpHeaders headers, HttpServletResponse httpServletResponse) {
        RpcId rpcId = new RpcId(1L);

        try (Context context = new Context(null, Db.getConfig())) {
            Map<String, Object> params = new HashMap<>(1);
            context.setArguments(params);
            ContextHandler.setContext(context);
            context.getResult().put("data", context.get("base","Login").call("login", userVo));
            return new JsonRpcResponse(rpcId, context.getResult());
        } catch (Exception e) {
            return new JsonRpcResponse(rpcId, JsonRpcError.createInternalError(e));
        }

    }

}
