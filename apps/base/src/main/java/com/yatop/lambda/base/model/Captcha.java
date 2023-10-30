package com.yatop.lambda.base.model;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 验证码
 */
@Table(name = "base_captcha")
public class Captcha extends Model<Captcha> {
    @Id
    private Long id;


    @Service(displayName = "获取验证码")
    public Map<String,Object> getCaptcha(){
        try {
            ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
            Map<String,Object> result = new LinkedHashMap<>(1);
            result.put("code", shearCaptcha.getCode());
            result.put("captchaEnabled", true);
            result.put("img", shearCaptcha.getImageBase64());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
