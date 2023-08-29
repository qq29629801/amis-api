package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "im_post")
public class ImPost extends Model<ImPost> {

    @Column(name = "id")
    private Long id;

    @Column(name = "post_name")
    private String postName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "fabulous_num")
    private Integer fabulousNum;

    @Column(name = "role")
    private Integer role;

    @Column(name = "status")
    private Integer status;

    @Column(name = "urls")
    private String urls;

    @Column(name = "post_context")
    private String postContext;
}
