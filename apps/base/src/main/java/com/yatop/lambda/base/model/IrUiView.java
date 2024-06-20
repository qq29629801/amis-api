package com.yatop.lambda.base.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CustomJsonNodeFactory;
import com.yuyaogc.lowcode.engine.util.CustomParserFactory;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
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
          //  if (primary == null && "primary".equals(view.get("mode"))) {
                primary = view;
//            }
//            if ("extension".equals(view.get("mode"))) {
//                extension.add(view);
//            }
        }
        if (primary == null) {
            throw new EngineException("找不到视图");
        }

        try {

            String xmlString = primary.getStr("arch");
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
//
//            CustomParserFactory customParserFactory = new CustomParserFactory();
//            ObjectMapper mapper = new ObjectMapper(customParserFactory);
//            CustomJsonNodeFactory factory = new CustomJsonNodeFactory(mapper.getDeserializationConfig().getNodeFactory(),
//                    customParserFactory);
//            mapper.setConfig(mapper.getDeserializationConfig().with(factory));
//            Configuration config = Configuration.builder().mappingProvider(new JacksonMappingProvider(mapper))
//                    .jsonProvider(new JacksonJsonNodeJsonProvider(mapper)).options(Option.ALWAYS_RETURN_LIST)
//                    .options(Option.SUPPRESS_EXCEPTIONS).build();
//            JsonContext context = (JsonContext) JsonPath.parse(inputStream, config);

            return primary.getStr("arch");
        } catch (Exception e) {
            e.printStackTrace();
        }



//        Document doc = Jsoup.parse((String) primary.get("arch"), Parser.xmlParser());
//        Elements base = doc.children();
//        for (IrUiView ext : extension) {
//            Document arch = Jsoup.parse((String) ext.get("arch"), Parser.xmlParser());
//            Elements data = getData(arch);
//            combined(base, data);
//        }
//        doc.select("[debug=true]").remove();
        // dom4j无法解析<!DOCTYPE>
     //   return "<!DOCTYPE html>\r\n" + doc.toString();


        return null;
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
