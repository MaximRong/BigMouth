package mouth.widgets;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mouth.listener.HyperlinkRedirectListener;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ImportResultBox {

    private String html = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "    <script>\n" +
            "        var delay = 0;\n" +
            "        function appendHtml(id, s) {\n" +
            "            id = id || \"text\";\n" +
            "            var text = document.getElementById(id);\n" +
            "            text.innerText = text.innerText + s;\n" +
            "        }\n" +
            "\n" +
            "        function writeOneByOne(id, str) {\n" +
            "            var id = id || \"text\";\n" +
            "            for (var i = 0; i < str.length; i++) {\n" +
            "                setTimeout(\"appendHtml('\" + id + \"', '\" + str.charAt(i) + \"')\", delay + 50 * i);\n" +
            "            }\n" +
            "            delay = delay + 50 * (str.length - 1);\n" +
            "        }\n" +
            "\n" +
            "        document.addEventListener('DOMContentLoaded', function (event) {\n" +
            "            var str = \"thisIsHtml\";\n" +
            "            var body = document.getElementById(\"body\");\n" +
            "            body.insertAdjacentHTML( 'beforeend', str );\n" +
            "            thisIsWrite\n" +
            "        })\n" +
            "\n" +
            "    </script>\n" +
            "</head>\n" +
            "<body id=\"body\">\n" +
            "</body>\n" +
            "</html>";

    public void display(Stage stage, String result) {
        Platform.runLater(() -> {

            Stage window = new Stage();
            window.initStyle(StageStyle.UNDECORATED);
            //modality要使用Modality.APPLICATION_MODEL
            window.initModality(Modality.APPLICATION_MODAL);
            window.setWidth(300);
            window.setHeight(300);

            window.setX(stage.getX() + stage.getWidth() / 2 - window.getWidth() / 2);
            window.setY(stage.getY() - window.getHeight() - 20);


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

            StringBuilder htmlSb = new StringBuilder();
            StringBuilder writeSb = new StringBuilder();
            appendResult(consumerInfo, consumerResult, htmlSb, writeSb, "客户", "consumer");
            appendResult(goodsInfo, goodsResult, htmlSb, writeSb, "商品", "goods");
            appendResult(supplierInfo, supplierResult, htmlSb, writeSb, "供应商", "supplier");
            appendResult(goodsPricePlanInfo, goodsPricePlanResult, htmlSb, writeSb, "价格方案", "goodsPricePlan");

            html = StringUtils.replace(html, "thisIsHtml", htmlSb.toString());
            html = StringUtils.replace(html, "thisIsWrite", writeSb.toString());
            System.out.println(html);

      /*  HTMLEditor htmlEditor = new HTMLEditor();

//        textArea.setWrappingWidth(200);
//        textArea.setStyle("-fx-opacity: 1;");
        TextArea textArea = new TextArea();
        textArea.setText(out.toString());
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setStyle("-fx-opacity: 1;");*/

            Button closeBtn = new Button("关闭");
            closeBtn.setOnAction(e -> window.close());

            WebView webView = new WebView();
            WebEngine engine = webView.getEngine();
            engine.loadContent(html);

            // 保证链接在新的浏览器打开
            engine.getLoadWorker().stateProperty().addListener(new HyperlinkRedirectListener(webView));


//        Label label = new Label(message);

            VBox layout = new VBox(10);
            layout.getChildren().addAll(webView, closeBtn);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
            window.showAndWait();

        });

    }

    private void appendResult(String consumerInfo, Boolean consumerResult,
                              StringBuilder html, StringBuilder out, final String type, String typeId) {
        String zeroMsg = "价格方案".equals(type) ? "更新0个商品明细" : "导入" + type + "档案0个";

        if (consumerResult) {
            if (!zeroMsg.equals(consumerInfo)) {
                html.append("<div><span id='").append(typeId).append("'></span>").append("<a href='http://localhost:9999/saas/erp/doc/consumer/list' id='" + typeId + "_a' target='_blank'></a>").append("</div>");
                out.append("writeOneByOne('").append(typeId).append("', '").append(consumerInfo).append("');\n");
                out.append("writeOneByOne('").append(typeId).append("_a', '").append("点击查看").append(type).append("');\n");
            }
            return;
        }

        html.append("<div><span id='").append(typeId).append("'></span>").append("</div>");
        out.append("writeOneByOne('").append(typeId).append("', '").append("导入").append(type).append("异常: ").append(consumerInfo).append("');\n");
    }

}
