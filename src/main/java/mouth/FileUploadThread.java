package mouth;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mouth.widgets.ImportResultBox;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUploadThread extends Task<List<String>> {
    private final ImageView imageView;
    private final File file;

    private static final String HTTP_URL = "http://localhost:9999/saas/erp/doc/goods/importExcelData4Mouth?doctype=consumer";
    private final Stage stage;


    FileUploadThread(ImageView imageView, Stage stage, File file) {
        this.imageView = imageView;
        this.stage = stage;
        this.file = file;
    }

    @Override
    protected List<String> call() throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost post = new HttpPost(HTTP_URL);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
//                    StringBody stringBody1 = new StringBody("Message 1", ContentType.MULTIPART_FORM_DATA);
//                    StringBody stringBody2 = new StringBody("Message 2", ContentType.MULTIPART_FORM_DATA);
//
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);
//                    builder.addPart("text1", stringBody1);
//                    builder.addPart("text2", stringBody2);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            HttpResponse response = httpclient.execute(post);
            String result = EntityUtils.toString(response.getEntity(),"utf-8");
            System.out.println(result);
            imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\hello.gif"));
            new ImportResultBox().display(stage, result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
