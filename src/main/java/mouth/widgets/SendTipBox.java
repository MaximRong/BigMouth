package mouth.widgets;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mouth.util.WebViewUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SendTipBox {

    private static final String MESSAGE_URL = "http://localhost:9999/saas/erp/other/consumerFeedback/add?content=";
    private static final String QUESTION_URL = "http://localhost:9999/saas/erp/other/consumerFeedback/question?index=";

    private static final String[] QUESTIONS = {"销售订单和销售单有什么区别？",
            "我单子一张一张打印太麻烦，能不能一起全部打印？",
            "红冲的订单能不能再恢复？",
            "开销售单、退货单、调拨单等单据时，关联仓库正在盘点中，无法提交单据。如何解决？"};

    public void display(Stage stage) {

        Stage window = new Stage();
        window.initStyle(StageStyle.UNDECORATED);
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(300);
        window.setHeight(150);

        TextArea textArea = new TextArea();

        Button sendBtn = new Button("发送吐槽");
        Button closeBtn = new Button("不想吐了");
        sendBtn.setOnAction(e -> {
            // 发送到saas服务器
            String content = textArea.getText();
            int index = 0; boolean found = false;
            for(; index < QUESTIONS.length; index++) {
                String question = QUESTIONS[index];
                if(question.contains(content)) {
                    found = true;
                    break;
                }
            }

            // 如果找到了对应问题，发送到云管家获取html
            if(found) {
                sendQuestion2Saas(stage, index);
            } else {
                sendMessage2Saas(stage, content);
            }

            // 吐槽完了之后完毕对话框
            window.close();
        });
        closeBtn.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(textArea, sendBtn, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    private void sendQuestion2Saas(Stage stage, int index) {
        HttpGet httpGet = new HttpGet(QUESTION_URL + index);
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(httpGet);
            String resultHtml = EntityUtils.toString(response.getEntity(), "utf-8");
            WebViewUtil.show(stage, resultHtml);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage2Saas(Stage stage, String content) {
        HttpGet httpGet = new HttpGet(MESSAGE_URL + content);
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
            new MessageBox().display(stage, "TEXT", "老大，您的问题我已经反馈成功了，会尽快安排！放心！");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
