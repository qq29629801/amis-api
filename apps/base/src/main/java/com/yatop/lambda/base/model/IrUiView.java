package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.*;

@Table(name = "base_ui")
public class IrUiView extends Model<IrUiView> {
    @Id
    private Long id;
    private String name;
    private String model;
    private String key;
    private String mode;
    private boolean active;
//    @ManyToOne
//    @JoinColumn(name = "inherit_id")
//    private IrUiView inherit;

    @Column(type = DataTypeEnum.TEXT)
    private String arch;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;


    @Service
    public String loadWeb(String key) {
        IrUiView uiView = new IrUiView();
        List<IrUiView> views = uiView.search(Criteria.equal("key", key), 0, 0, "");
        IrUiView primary = null;
        List<IrUiView> extension = new ArrayList<>();
        for (IrUiView view : views) {
            if (primary == null && "primary".equals(view.get("mode"))) {
                primary = view;
            }
            if ("extension".equals(view.get("mode"))) {
                extension.add(view);
            }
        }
        if (primary == null) {
            throw new EngineException("找不到视图");
        }
        Document doc = Jsoup.parse((String) primary.get("arch"), Parser.xmlParser());
        Elements base = doc.children();
        for (IrUiView ext : extension) {
            Document arch = Jsoup.parse((String) ext.get("arch"), Parser.xmlParser());
            Elements data = getData(arch);
            combined(base, data);
        }
        doc.select("[debug=true]").remove();
        // dom4j无法解析<!DOCTYPE>
        return "<!DOCTYPE html>\r\n" + doc.toString();
    }

    public static void combined(Elements base, Elements data) {
        for (Element el : data) {
            String position = el.attr("position");
            if ("xpath".equals(el.tagName())) {
                String has = el.attr("has");
                if (StringUtils.isNotEmpty(has)) {
                    Elements found = base.select(has);
                    if (found.size() == 0) {
                        continue;
                    }
                }
                String hasNot = el.attr("has_not");
                if (StringUtils.isNotEmpty(hasNot)) {
                    Elements found = base.select(hasNot);
                    if (found.size() > 0) {
                        continue;
                    }
                }
                String expr = el.attr("expr");
                Elements selects = base.select(expr);
                for (Element select : selects) {
                    combined(position, select, el.childNodes().toArray(new Node[el.childNodeSize()]));
                }
            } else {
                for (Element select : base) {
                    combined(position, select, el);
                }
            }
        }
    }

    static void combined(String position, Element target, Node... nodes) {
        if ("before".equals(position)) {
            for (Node node : nodes) {
                target.before(node);
            }
        } else if ("after".equals(position)) {
            for (Node node : nodes) {
                target.after(node);
            }
        } else if ("replace".equals(position)) {
            boolean first = true;
            for (Node node : nodes) {
                if (first) {
                    first = false;
                    target.replaceWith(node);
                } else {
                    target.after(node);
                }
            }
        } else if ("inside".equals(position)) {
            target.appendChildren(Arrays.asList(nodes));
        } else if ("attribute".equals(position)) {
            for (Node node : nodes) {
                target.attributes().addAll(node.attributes());
            }
        }
    }

    Elements getData(Document doc) {
        for (Element el : doc.children()) {
            if ("data".equals(el.tagName())) {
                return el.children();
            }
        }
        return doc.children();
    }
}
