package mouth.widgets;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ImportResultBox {

    public void display(String result) {
        Stage window = new Stage();
        window.initStyle(StageStyle.UNDECORATED);
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(500);
        window.setHeight(350);

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

        StringBuilder out = new StringBuilder();
        appendResult(consumerInfo, consumerResult, out, "客户");
        appendResult(goodsInfo, goodsResult, out, "商品");
        appendResult(supplierInfo, supplierResult, out, "供应商");
        appendResult(goodsPricePlanInfo, goodsPricePlanResult, out, "价格方案");

        HTMLEditor htmlEditor = new HTMLEditor();
        WebView webView = new WebView();
//        textArea.setWrappingWidth(200);
//        textArea.setStyle("-fx-opacity: 1;");
        TextArea textArea = new TextArea();
        textArea.setText(out.toString());
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setStyle("-fx-opacity: 1;");

        Button closeBtn = new Button("关闭");
        closeBtn.setOnAction(e -> window.close());

        WebEngine engine = webView.getEngine();
        engine.loadContent(out.toString());

        engine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                Desktop d = Desktop.getDesktop();
                try {
                    System.out.println(oldValue);
                    System.out.println(newValue);
                    URI address = new URI(newValue);
                    if ((address.getQuery() + "").contains("_openmodal=true")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                engine.load(oldValue);
                            }
                        });
                        d.browse(address);
                    }
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        Document document = engine.getDocument();
        NodeList nodeList = document.getElementsByTagName("a");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node= nodeList.item(i);
            EventTarget eventTarget = (EventTarget) node;
//            eventTarget.addEventListener("click", new EventListener()
//            {
//                @Override
//                public void handleEvent(Event evt)
//                {
//                    EventTarget target = evt.getCurrentTarget();
//                    HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
//                    String href = anchorElement.getHref();
//                    //handle opening URL outside JavaFX WebView
//                    System.out.println(href);
//                    evt.preventDefault();
//                }
//            }, false);
        }

//        Label label = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(webView, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    private void appendResult(String consumerInfo, Boolean consumerResult, StringBuilder out, final String type) {
        String zeroMsg = "价格方案".equals(type) ? "更新0个商品明细" : "导入" + type + "档案0个";

        if (consumerResult) {
            if (!zeroMsg.equals(consumerInfo)) {
                out.append(consumerInfo).append("<a href='http://www.baidu.com' target='_blank'>点击查看</a>").append("<br/>");
            }
            return;
        }

        out.append("导入").append(type).append("异常: ").append(consumerInfo).append("<br/>");
    }

}
