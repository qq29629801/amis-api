package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;


@Table(name = "base_mode_data")
public class IrModelData extends Model<IrModelData>{
    @Id
    private Long id;
    @Column
    private String name;

    @Column
    private String module;

    @Column
    private String refId;

    @Service
    public IrModelData findRef(String xmlid) {
        String[] parts = xmlid.split("\\.", 2);
       return this.selectOne(Criteria.equal("refId", parts[1]));
    }
}
