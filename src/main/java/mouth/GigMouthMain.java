package mouth;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mouth.widgets.ImportResultBox;
import mouth.widgets.MessageBox;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GigMouthMain extends Application {

    private static final int VIEW_HEIGHT = 100;
    private static final int VIEW_WIDTH = 100;
    private TrayIcon trayIcon;
    private static final String HTTP_URL = "http://localhost:9999/saas/erp/doc/goods/importExcelData4Mouth?doctype=consumer";

    private double xOffSet = 0;
    private double yOffSet = 0;

    private String classPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();

    @Override
    public void start(Stage stage) {
        try {
            // 设计变成无边框的模式
            stage.initStyle(StageStyle.UNDECORATED);
            // 始终保持在前端/不会被最小化
            stage.setAlwaysOnTop(true);

            enableTray(stage);


            // 启动初始图片
            Image bigMouthImage = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\hello.gif");
            ImageView imageView = new ImageView();
            imageView.setImage(bigMouthImage);
            imageView.setFitWidth(VIEW_WIDTH);
            imageView.setFitHeight(VIEW_HEIGHT);
            // 将面板整个设置成手的形状
            imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> imageView.setCursor(Cursor.HAND));

           /*
            获取图片的长于宽，并且展示出来
            double imageHeight = bigMouthImage.getHeight();
            double imageWidth = bigMouthImage.getWidth();
            System.out.println(imageWidth + " : " + imageHeight);
            */

            // 设置菜单
            final ContextMenu clickMenu = new ContextMenu();
            clickMenu.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                // 当用户右键点击菜单时
                if (event.getButton() == MouseButton.SECONDARY) {
                    event.consume();
                }
            });
            // 处理左键点击主要事件
            clickMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String menuText = ((MenuItem) event.getTarget()).getText();
                    if ("吐槽一下".equals(menuText)) {
                        new MessageBox().display();
                    } else if ("叮咚".equals(menuText)) {
                        // 设置声音文件，用于播放提醒
                        String soundStr = classPath + "dingDong.mp3";
                        Media sound = new Media(new File(soundStr).toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                    } else {
                        imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\sleep.gif"));

                        Task<Void> sleeper = new Task<Void>() {
                            @Override
                            protected Void call() {
                                try {
                                    Thread.sleep(1200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(event1 ->  System.exit(0));
                        new Thread(sleeper).start();
                    }
                    System.out.println("right gets consumed so this must be left on " +
                            menuText);
                }
            });

            MenuItem letterItem = new MenuItem("吐槽一下");
            MenuItem voiceItem = new MenuItem("叮咚");
            MenuItem quitItem = new MenuItem("退出");
            clickMenu.getItems().addAll(letterItem, voiceItem, quitItem);


            // 设置根pane
            StackPane pane = new StackPane();
            // 将其他组件加入
            pane.getChildren().addAll(imageView);
            pane.setOnDragOver(event -> {
                if (event.getGestureSource() != pane
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });


            pane.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    List<File> files = db.getFiles();
                    File file = files.get(0);
                    // 处理的过场动画
                    imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\move.gif"));

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
                        new ImportResultBox().display(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);
                event.consume();
            });

            pane.setOnMousePressed(event -> {
                xOffSet = event.getSceneX();
                yOffSet = event.getSceneY();
            });

            pane.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffSet);
                stage.setY(event.getScreenY() - yOffSet);
            });

            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    // 右键点击事件
                    if (e.getButton() == MouseButton.SECONDARY) {
                        clickMenu.show(pane, e.getScreenX(), e.getScreenY());
                    } else {
                        if (2 == e.getClickCount()) {
                            System.out.println("double click");
                            imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\jumpLine.gif"));
                        }

                    }
                }
            });

            startBackStageThread();

            // 设置组件大小
            Scene scene = new Scene(pane, VIEW_WIDTH, VIEW_HEIGHT);
            stage.setScene(scene);
            stage.show();

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void startBackStageThread() {
        BackStageThread backStageThread = new BackStageThread();
        backStageThread.runningProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean wasRunning, Boolean isRunning) {
//                if (!isRunning) {
//
//                }
            }
        });

        final Thread thread = new Thread(backStageThread, "backStageThread");
        thread.setDaemon(true);
        thread.start();
    }

    private void enableTray(final Stage stage) {

        PopupMenu popupMenu = new PopupMenu();
        java.awt.MenuItem openItem = new java.awt.MenuItem("显示");
        java.awt.MenuItem hideItem = new java.awt.MenuItem("最小化");
        java.awt.MenuItem quitItem = new java.awt.MenuItem("退出");

        ActionListener actionListener = e -> {
            java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();
            Platform.setImplicitExit(false); //多次使用显示和隐藏设置false

            if (item.getLabel().equals("退出")) {
                SystemTray.getSystemTray().remove(trayIcon);
                Platform.exit();
                return;
            }
            if (item.getLabel().equals("显示")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.show();
                    }
                });
            }
            if (item.getLabel().equals("最小化")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.hide();
                    }
                });
            }

        };

        //双击事件方法
        MouseListener mouseListener = new MouseListener() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                //多次使用显示和隐藏设置false
                Platform.setImplicitExit(false);
                if (e.getClickCount() == 2) {
                    if (stage.isShowing()) {
                        Platform.runLater(stage::hide);
                    }else{
                        Platform.runLater(stage::show);
                    }
                }
            }
        };



        openItem.addActionListener(actionListener);
        quitItem.addActionListener(actionListener);
        hideItem.addActionListener(actionListener);

        popupMenu.add(openItem);
        popupMenu.add(hideItem);
        popupMenu.add(quitItem);

        try {
            SystemTray tray = SystemTray.getSystemTray();
            File file = new File(classPath + "pokeBal.png");
            BufferedImage image = ImageIO.read(file);
            trayIcon = new TrayIcon(image, "自动备份工具", popupMenu);
            trayIcon.setToolTip("自动备份工具");
            tray.add(trayIcon);
            trayIcon.addMouseListener(mouseListener);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
