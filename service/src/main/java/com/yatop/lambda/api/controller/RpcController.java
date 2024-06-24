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
import com.yuyaogc.lowcode.engine.util.ConfigUtils;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Object views(String key){
        try (Context context = new Context(null, Db.getConfig())) {
            return  context.get("base.base_ui_view").call("loadView", key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/search")
    public List<Map<String,Object>> search(int page,int perPage,String keywords,String app, String model){
        try (Context context = new Context(null, Db.getConfig())) {
             int OFFSET = (page - 1) * perPage;
            keywords = StringUtils.isEmpty(keywords)?"%%":keywords;
            return  context.get(String.format("%s.%s", app, model)).search(Criteria.like("appName", keywords),OFFSET,perPage,null );
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
