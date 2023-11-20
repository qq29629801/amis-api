package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;

import java.util.List;

@Table(name = "im_group_user")
public class ImGroupUser extends Model<ImGroupUser> {
    @Id
    private Long id;
    @Column(name = "group_nick_name")
    private String groupNickName;

    @ManyToOne(cascade = CascadeType.DELETE)
    @JoinColumn(name = "user_id")
    private ImUser userId;

    @ManyToOne(cascade = CascadeType.DELETE)
    @JoinColumn(name = "group_id")
    private ImGroup groupId;

    @Service
    public List<ImGroupUser> search(Criteria criteria, Integer offset, Integer limit, String order) {
      List<ImUser> users =  this.getContext().get("base.ImUser").call("search", new Criteria(), 0, 0, null);
     ImGroupUser imUser =  findById(1);
        System.out.println(users.get(0).getLogin());
        return null;
    }
}
