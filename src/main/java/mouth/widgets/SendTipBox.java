package mouth.widgets;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SendTipBox {

    private static final String MESSAGE_URL = "http://localhost:9999/saas/erp/other/consumerFeedback/add?content=";
    private static final String QUESTION_URL = "http://localhost:9999/saas/erp/other/consumerFeedback/question?index=";

    private static final String[] QUESTIONS = {"销售订单和销售单有什么区别？",
            "我单子一张一张打印太麻烦，能不能一起全部打印？"};

    public void display(Stage stage, ImageView imageView) {
        try {
            Stage window = new Stage();
            window.initStyle(StageStyle.UNDECORATED);
            //modality要使用Modality.APPLICATION_MODEL
            window.initModality(Modality.APPLICATION_MODAL);
            window.setWidth(600);
            window.setHeight(400);

            window.setX(stage.getX() + stage.getWidth() / 2 - window.getWidth() / 2 - 100);
            window.setY(stage.getY() - window.getHeight() - 20);

            window.setTitle("怪兽消息");
            URL resource = getClass().getResource("/pokeBal.png");
            window.getIcons().add(new Image(resource.toString()));

            Pane pane = FXMLLoader.load(getClass().getResource("/sendBox.fxml"));

            TextArea textArea = (TextArea) pane.getChildren().get(0);
            Button sendBtn = (Button) pane.getChildren().get(1);
            Button closeBtn = (Button) pane.getChildren().get(2);

            sendBtn.setOnAction(e -> {
                // 发送到saas服务器
                String content = textArea.getText();
                int index = 0;
                boolean found = false;
                for (; index < QUESTIONS.length; index++) {
                    String question = QUESTIONS[index];
                    if (question.contains(content)) {
                        found = true;
                        break;
                    }
                }

                // 如果找到了对应问题，发送到云管家获取html
                if (found) {
                    sendQuestion2Saas(stage, index);
                } else {
                    sendMessage2Saas(stage, imageView, content);
                }

                // 吐槽完了之后完毕对话框
                window.close();
            });
            closeBtn.setOnAction(e -> window.close());
            Scene scene = new Scene(pane);
            window.setScene(scene);
            //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void sendMessage2Saas(Stage stage, ImageView imageView, String content) {
        HttpGet httpGet = new HttpGet(MESSAGE_URL + content);
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
            new MessageBox().display(stage, imageView, "TEXT", "老大，您的问题我已经反馈成功了，会尽快安排！放心！");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
