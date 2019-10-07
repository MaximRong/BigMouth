package mouth.widgets;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.stage.Stage;
import mouth.bean.Message;
import mouth.util.MessageUtil;
import mouth.util.WebViewUtil;

import java.util.Map;

public class ImportResultBox {


    public void display(Stage stage, String result) {
        JSONObject resultJson = JSONUtil.parseObj(result);
        Map map = (Map) resultJson.get("map");
        Map supplier = (Map) map.get("supplier");
        String supplierInfo = MapUtil.getStr(supplier, "info");
        Boolean supplierResult = MapUtil.getBool(supplier, "result");

        Map goods = (Map) map.get("goods");
        String goodsInfo = MapUtil.getStr(goods, "info");
        Boolean goodsResult = MapUtil.getBool(goods, "result");

        Map consumer = (Map) map.get("consumer");
        String consumerInfo = MapUtil.getStr(consumer, "info");
        Boolean consumerResult = MapUtil.getBool(consumer, "result");

        Map goodsPricePlan = (Map) map.get("goodsPricePlan");
        String goodsPricePlanInfo = MapUtil.getStr(goodsPricePlan, "info");
        Boolean goodsPricePlanResult = MapUtil.getBool(goodsPricePlan, "result");

        int messageLength = 0;
        StringBuilder htmlSb = new StringBuilder();
        StringBuilder writeSb = new StringBuilder();
        messageLength += appendResult(consumerInfo, consumerResult, htmlSb, writeSb, "客户", "consumer");
        messageLength += appendResult(goodsInfo, goodsResult, htmlSb, writeSb, "商品", "goods");
        messageLength += appendResult(supplierInfo, supplierResult, htmlSb, writeSb, "供应商", "supplier");
        messageLength += appendResult(goodsPricePlanInfo, goodsPricePlanResult, htmlSb, writeSb, "价格方案", "goodsPricePlan");

        WebViewUtil.show(stage, htmlSb.toString(), writeSb.toString(), messageLength);
    }

    private int appendResult(String consumerInfo, Boolean consumerResult,
                              StringBuilder html, StringBuilder out, final String type, String typeId) {
        int length = 0;
        String zeroMsg = "价格方案".equals(type) ? "更新0个商品明细" : "导入" + type + "档案0个";

        if (consumerResult) {
            if (!zeroMsg.equals(consumerInfo)) {
                html.append("<div><span id='").append(typeId).append("'></span>")
                        .append("<a").append(MessageUtil.getPageUrl(typeId)).append(" id='")
                        .append(typeId).append("_a' target='_blank'></a>").append("</div>");
                Message message = MessageUtil.appendMessage(typeId, consumerInfo);
                length += message.getOrig().length();
                out.append(message.getContent());
                message = MessageUtil.appendMessage(typeId + "_a", "点击查看" + type);
                length += message.getOrig().length();
                out.append(message.getContent());
            }
            return length;
        }

        html.append("<div><span id='").append(typeId).append("'></span>").append("</div>");
        Message message = MessageUtil.appendMessage(typeId, "导入" + type + "异常: " + consumerInfo);
        length += message.getOrig().length();
        out.append(message.getContent());
        return length;
    }


}
