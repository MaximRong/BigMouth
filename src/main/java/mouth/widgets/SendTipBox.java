package mouth.widgets;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.NameValuePairHelper;

import java.io.IOException;
import java.nio.charset.Charset;

public class SendTipBox {

    private static final String HTTP_URL = "http://localhost:9999/saas/erp/other/consumerFeedback/add?content=";

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
/*

            HttpPost post = new HttpPost(HTTP_URL);
//                    StringBody stringBody1 = new StringBody("Message 1", ContentType.MULTIPART_FORM_DATA);
//                    StringBody stringBody2 = new StringBody("Message 2", ContentType.MULTIPART_FORM_DATA);
//
//            JSONObject param = new JSONObject();
//            param.put("content", content);
//            StringEntity params = new StringEntity(param.toString(), Charsets.UTF_8);//HTTP.UTF_8, support Chinese and happy face. Otherwise it is messy code
//            post.addHeader("content-type", "application/json");
//            post.addHeader("accept", "application/json");
//            post.setEntity(params);

            //                    StringBody stringBody1 = new StringBody("Message 1", ContentType.MULTIPART_FORM_DATA);
//                    StringBody stringBody2 = new StringBody("Message 2", ContentType.MULTIPART_FORM_DATA);
//
            EntityBuilder builder = EntityBuilder.create();
//            StringBody contentStr = new StringBody(content, ContentType.MULTIPART_FORM_DATA);
            NameValuePair[] pairs = {
                    new NameValuePair() {
                        @Override
                        public String getName() {
                            return "version";
                        }

                        @Override
                        public String getValue() {
                            return content;
                        }
                    }};
            builder.setParameters(pairs);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
*/


            HttpGet httpGet = new HttpGet(HTTP_URL + content);

            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpResponse response = httpclient.execute(httpGet);
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(result);

                new MessageBox().display(stage, "TEXT", "老大，您的问题我已经反馈成功了，会尽快安排！放心！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // 吐槽完了之后完毕对话框
            window.close();
        });
        closeBtn.setOnAction(e -> window.close());

//        Label label = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(textArea, sendBtn, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

}
