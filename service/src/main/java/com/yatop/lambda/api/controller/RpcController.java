package com.yatop.lambda.api.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcError;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcRequest;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcResponse;
import com.yuyaogc.lowcode.engine.jsonrpc.RpcId;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.KvMap;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.BeanUtils;
import com.yuyaogc.lowcode.engine.util.ConfigUtils;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/rpc")
public class RpcController {
    private final Logger logger = LoggerFactory.getLogger(RpcController.class);

    @Value("${engine.low-code.path}")
    private String path;

    @GetMapping(value = "/menus")
    public Map<String, Object> menus(){
        try (Context context = new Context(null, Db.getConfig())) {
            return context.get("base.base_ui_menu").call("loadMenus");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping(value = "/views")
    public Object views(String key,String model,String module){
        try (Context context = new Context(null, Db.getConfig())) {
            return  context.get("base.base_ui_view").call("loadView", key, model, module);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/search")
    public JsonRpcResponse search(int page,int perPage,String keywords,String module, String model){
        try (Context context = new Context(null, Db.getConfig())) {
             int OFFSET = (page - 1) * perPage;

            Map<String,Object> result = new HashMap<>();
            result.put("count", context.get(String.format("%s.%s", module, model)).count(new Criteria()));
            result.put("rows", context.get(String.format("%s.%s", module, model)).search(new Criteria(),OFFSET,perPage,null ));
            return new JsonRpcResponse(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/lookup")
    public JsonRpcResponse lookup(int page,int perPage,String keywords,String module, String model){
        try (Context context = new Context(null, Db.getConfig())) {
            int OFFSET = (page - 1) * perPage;

            Map<String,Object> result = new HashMap<>();

            List<Model> list = context.get(String.format("%s.%s", module, model)).search(new Criteria(),OFFSET,perPage,null );
            EntityClass entityClass = context.get(String.format("%s.%s", module, model)).getEntity();

            List<Map<String,Object>> values = new ArrayList<>();
            for(Model model1: list){
                Map<String,Object> v = new LinkedHashMap<>();
                v.put("label",model1.get("name"));
                v.put("value",model1.get("id"));
                values.add(v);
            }

            result.put("options", values);
            result.put("value",null);
            return new JsonRpcResponse(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/file/upload")
    public List<Map<String, Object>> upload(@RequestParam("file") MultipartFile[] multipartFiles){
        if (multipartFiles == null || multipartFiles.length == 0) {

        }
        List<Map<String, Object>> attachments = new ArrayList<Map<String, Object>>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            long fileSize = multipartFile.getSize();

            String originalFilename = multipartFile.getOriginalFilename();
            String contentType = org.apache.commons.lang3.StringUtils.substringAfterLast(originalFilename, ".");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(contentType)) {
                contentType = "." + contentType;
            }


            String md5 = null;
            try {
                md5 = DigestUtils.md5Hex(multipartFile.getInputStream());
            } catch (IOException e) {

            }

            try {
                File directory = new File(path);
                directory.mkdirs();
                File file = new File(path + originalFilename);
                if(!file.exists()){
                    file.createNewFile();
                }
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("name", originalFilename);
            result.put("size", fileSize);
            result.put("content_type", contentType);
            result.put("md5", md5);
            attachments.add(result);
        }

        return attachments;
    }

    @RequestMapping("/create")
    public void create(HttpServletRequest req, @RequestBody Map<String,Object> v){
        String module = req.getParameter("module");
        String model = req.getParameter("model");
        try (Context context = new Context(null, Db.getConfig())) {
            Map<String,Object> paras = new LinkedHashMap<>();
            paras.put("app",module);
            paras.put("model", model);
            paras.put("service", "create");
            Map<String,Object> args = new LinkedHashMap<>();
            args.put("value", v);
            paras.put("args",args);
            context.setParams(paras);
            context.call();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/update")
    public void update(HttpServletRequest req, @RequestBody Map<String,Object> v){
        String module = req.getParameter("module");
        String model = req.getParameter("model");
        try (Context context = new Context(null, Db.getConfig())) {
            Map<String,Object> paras = new LinkedHashMap<>();
            paras.put("app",module);
            paras.put("model", model);
            paras.put("service", "updateById");
            Map<String,Object> args = new LinkedHashMap<>();
            args.put("value", v);
            paras.put("args",args);
            context.setParams(paras);
            context.call();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @DeleteMapping("/delete")
    public void update(HttpServletRequest req,Long id){
        String module = req.getParameter("module");
        String model = req.getParameter("model");
        try (Context context = new Context(null, Db.getConfig())) {
            context.get(String.format("%s.%s", module, model)).deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @RequestMapping("/service")
    public JsonRpcResponse service(@RequestBody JsonRpcRequest request, @RequestHeader HttpHeaders headers) {
        try (Context context = new Context(null, Db.getConfig())) {
            Map<String, Object> params = request.getParams().getMap();
            context.setParams(params);
            context.call();
            return new JsonRpcResponse(context.getResult());
        } catch (Exception e) {
            logger.error("==========={}", e);
           // return new JsonRpcResponse(request.getId(), JsonRpcError.createInternalError(e));
        }
        return null;
    }

    @PostMapping("/login")
    public JsonRpcResponse login(@RequestBody Map<String,Object> userVo, @RequestHeader HttpHeaders headers, HttpServletResponse httpServletResponse) {
      return null;
    }


    @PostMapping("/getCaptcha")
    public JsonRpcResponse getCaptcha(@RequestHeader HttpHeaders headers, HttpServletResponse httpServletResponse) {
        try (Context context = new Context(null, Db.getConfig())) {
            Map<String, Object> params = new HashMap<>(1);
            context.setParams(params);
            context.getResult().put("data", context.get("base.Captcha").call("getCaptcha"));
            return new JsonRpcResponse(context.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
