package mouth.widgets;

import javafx.stage.Stage;
import mouth.bean.Message;
import mouth.util.MessageUtil;
import mouth.util.WebViewUtil;

public class MessageBox {

    public void display(Stage stage, String bisType, String result) {
        String html = "", content = "";
        int length = 0;
        if("XD".equals(bisType)) {
            html = "<span id='content'></span><a id='page'" + MessageUtil.getPageUrl("saleOrderBill") + "></a>";
            Message message = MessageUtil.appendMessage("content", "您有一个新的销售订单，请尽快审核！");
            length += message.origLength();
            content += message.getContent();

            message = MessageUtil.appendMessage("page", "【点击查看】");
            length += message.origLength();
            content += message.getContent();
        } else if("TEXT".equals(bisType)) {
            html = "<span id='content'></span>";
            Message message = MessageUtil.appendMessage("content", result);
            length += message.origLength();
            content += message.getContent();
        } else if("error".equals(bisType)) {
            html = "<span id='content'></span>";
            Message message = MessageUtil.appendMessage("content", result);
            length += message.origLength();
            content += message.getContent();
        }

        WebViewUtil.show(stage, html, content, length);

    }



}
