package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.annotation.NotBlank;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;


@Table(name = "base_file")
public class IrAttachment extends Model<IrAttachment> {
    @Id
    private Long id;

    @Column(name = "file_name")
    @NotBlank
    private String fileName;

    @Column(name = "size")
    private String size;

    @Column
    @NotBlank
    private Long age;


    @Service
    public boolean create(IrAttachment baseFile){

       List<IrModule> baseAppList= getContext().get("base.BaseApp").call("search", new Criteria(), 0 , 0, null);
       IrModule baseApp = baseAppList.get(0);

        return true;
    }


}
