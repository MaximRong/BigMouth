package mouth.util;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mouth.listener.HyperlinkRedirectListener;

import java.io.File;
import java.util.Objects;

// 强化变成链接可以点到外部
public class WebViewUtil {

    private static String classPath = Objects.requireNonNull(MessageUtil.class.getClassLoader().getResource("")).getPath();

    private static WebView webView(String html) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.loadContent(html);

        // 保证链接在新的浏览器打开
        engine.getLoadWorker().stateProperty().addListener(new HyperlinkRedirectListener(webView));
        return webView;
    }

    public static WebView webView(String html, String content) {
        return webView(MessageUtil.html(html, content));
    }

    public static void show(Stage stage, String html, String content, int sayLength) {
        Platform.runLater(() -> {

            Stage window = new Stage();
            window.initStyle(StageStyle.UNDECORATED);
            //modality要使用Modality.APPLICATION_MODEL
            window.initModality(Modality.APPLICATION_MODAL);
            window.setAlwaysOnTop(true);
            window.setWidth(300);
            window.setHeight(300);
            window.setTitle("怪兽消息");
            String url = new File(classPath + "pokeBal.png").toURI().toString();
            window.getIcons().add(new Image(url));

            window.setX(stage.getX() + stage.getWidth() / 2 - window.getWidth() / 2);
            window.setY(stage.getY() - window.getHeight() - 20);




            Button closeBtn = new Button("关闭");
            closeBtn.setOnAction(e -> window.close());

            WebView webView = webView(html, content);

            MediaPlayer say = SoundUtil.say();
            int millSecond = 1200 + 50 * (sayLength - 1);
            System.out.println(millSecond);
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(millSecond);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            sleeper.setOnSucceeded(event1 -> say.stop());
            new Thread(sleeper).start();



            VBox layout = new VBox(10);
            layout.getChildren().addAll(webView, closeBtn);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
            window.showAndWait();

        });
    }

    public static void show(Stage stage, String html) {
        Platform.runLater(() -> {

            Stage window = new Stage();
            window.initStyle(StageStyle.UNDECORATED);
            //modality要使用Modality.APPLICATION_MODEL
            window.initModality(Modality.APPLICATION_MODAL);
            window.setAlwaysOnTop(true);
            window.setWidth(800);
            window.setHeight(800);
            window.setTitle("怪兽消息");
            String url = new File(classPath + "pokeBal.png").toURI().toString();
            window.getIcons().add(new Image(url));

//            window.setX(window.getWidth() / 2 - 400);
//            window.setY(window.getHeight() / 2 - 400);




            Button closeBtn = new Button("我知道啦！");
            closeBtn.setMinHeight(30);
            closeBtn.setMinHeight(50);
            closeBtn.setStyle("-fx-font-size: 20px;");
            closeBtn.setCursor(Cursor.HAND);
//            closeBtn.setFont(new Font());
            closeBtn.setOnAction(e -> window.close());

            WebView webView = webView(html);

            VBox layout = new VBox(10);
            layout.getChildren().addAll(webView, closeBtn);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
            window.showAndWait();

        });
    }
}
