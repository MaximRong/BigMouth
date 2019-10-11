package mouth.widgets;

import javafx.stage.Stage;
import mouth.bean.Message;
import mouth.util.MessageUtil;
import mouth.util.WebViewUtil;
import org.apache.commons.lang3.StringUtils;

public class MessageBox {

    String TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body id=\"body\">\n" +
            "replaceHtml\n" +
            "</body>\n" +
            "</html>";

    public void display(Stage stage, String bisType, String result) {
        String html = "", content = "";
        int length = 0;
        if ("XD".equals(bisType) || "XS".equals(bisType)) {
            html = "<span id='content' style='font-size: 20px;'></span><a id='page'" + MessageUtil.getPageUrl("XD".equals(bisType) ? "saleOrderBill" : "saleBill") + " style='font-size: 20px;'></a>";
            Message message = MessageUtil.appendMessage("content", "您有一个新的销售" + ("XD".equals(bisType) ? "订" : "") + "单，请尽快审核！");
            length += message.origLength();
            content += message.getContent();

            message = MessageUtil.appendMessage("page", "【点击查看】");
            length += message.origLength();
            content += message.getContent();
        } else if ("TEXT".equals(bisType)) {
            html = "<span id='content' style='font-size: 20px;'></span>";
            Message message = MessageUtil.appendMessage("content", result);
            length += message.origLength();
            content += message.getContent();
        } else if ("error".equals(bisType)) {
            html = "<span id='content' style='font-size: 20px;'></span>";
            Message message = MessageUtil.appendMessage("content", result);
            length += message.origLength();
            content += message.getContent();
        }

        WebViewUtil.show(stage, html, content, length);

    }


}
