package com.yatop.lambda.base.model;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

/**
 * 验证码
 */
@Table(name = "base_captcha")
public class Captcha extends Model<Captcha> {
    @Id
    private Long id;


    @Service(displayName = "获取验证码")
    public String getCaptcha(){
        String base64String = "";
        try {
            ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
            base64String = "data:image/png;base64," + shearCaptcha.getImageBase64();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64String;
    }
}
