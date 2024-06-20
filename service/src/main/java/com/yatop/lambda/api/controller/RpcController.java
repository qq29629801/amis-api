package com.yatop.lambda.api.controller;

import cn.hutool.core.util.IdUtil;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcError;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcRequest;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcResponse;
import com.yuyaogc.lowcode.engine.jsonrpc.RpcId;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rpc")
public class RpcController {
    private final Logger logger = LoggerFactory.getLogger(RpcController.class);



    @GetMapping(value = "/menus")
    public Object menus(){
        try (Context context = new Context(null, Db.getConfig())) {

            Map<String,Object> result = new HashMap<>();

            Map<String,Object> p = new HashMap<>();
            p.put("page", context.get("base.base_menu").call("loadMenus"));

            //TODO
            result.put("data",p);
            result.put("msg","");
            result.put("status",0);

            return  result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping(value = "/views")
    public Object views(){
        try (Context context = new Context(null, Db.getConfig())) {

            Map<String,Object> result = new HashMap<>();

            Map<String,Object> p = new HashMap<>();
            p.put("page", context.get("base.base_menu").call("loadWeb"));


            //TODO
            result.put("data",p);
            result.put("msg","");
            result.put("status",0);

            return  result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    @RequestMapping("/service")
    public JsonRpcResponse service(@RequestBody JsonRpcRequest request, @RequestHeader HttpHeaders headers) {
        try (Context context = new Context(null, Db.getConfig())) {
            Map<String, Object> params = request.getParams().getMap();
            context.setParams(params);
            context.call();
            return new JsonRpcResponse(request.getId(), context.getResult());
        } catch (Exception e) {
            logger.error("==========={}", e);
            return new JsonRpcResponse(request.getId(), JsonRpcError.createInternalError(e));
        }
    }

    @PostMapping("/login")
    public JsonRpcResponse login(@RequestBody Map<String,Object> userVo, @RequestHeader HttpHeaders headers, HttpServletResponse httpServletResponse) {
      return null;
    }


    @PostMapping("/getCaptcha")
    public JsonRpcResponse getCaptcha(@RequestHeader HttpHeaders headers, HttpServletResponse httpServletResponse) {
        RpcId rpcId = new RpcId(1L);

        try (Context context = new Context(null, Db.getConfig())) {
            Map<String, Object> params = new HashMap<>(1);
            context.setParams(params);
            context.getResult().put("data", context.get("base.Captcha").call("getCaptcha"));
            return new JsonRpcResponse(rpcId, context.getResult());
        } catch (Exception e) {
            return new JsonRpcResponse(rpcId, JsonRpcError.createInternalError(e));
        }

    }

}
