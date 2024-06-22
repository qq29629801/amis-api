package com.yatop.lambda.api.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcError;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcRequest;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcResponse;
import com.yuyaogc.lowcode.engine.jsonrpc.RpcId;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.KvMap;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/api/rpc")
public class RpcController {
    private final Logger logger = LoggerFactory.getLogger(RpcController.class);



    @GetMapping(value = "/menus")
    public Map<String, Object> menus(){
        try (Context context = new Context(null, Db.getConfig())) {
            return context.get("base.base_menu").call("loadMenus");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping(value = "/views")
    public Object views(String key){
        try (Context context = new Context(null, Db.getConfig())) {
            return  context.get("base.base_ui").call("loadView", key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/search")
    public List<Map<String,Object>> search(int page,int perPage,String keywords,String module, String service){
        try (Context context = new Context(null, Db.getConfig())) {
            int offset = page * perPage;
            return  context.get(String.format("%s.%s", module, service)).search(Criteria.like("name", keywords),offset,perPage,null );
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
