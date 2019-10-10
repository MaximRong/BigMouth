package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mouth.widgets.SendTipBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class TransparentStage extends Application {

    private static final int VIEW_HEIGHT = 100;
    private static final int VIEW_WIDTH = 100;

    private TrayIcon trayIcon;

    private double xOffSet = 0;
    private double yOffSet = 0;

    private String classPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        // 始终保持在前端/不会被最小化
        stage.setAlwaysOnTop(true);

        enableTray(stage);
//        Text text = new Text("Transparent!");
//        text.setFont(new Font(40));
//        VBox box = new VBox();
//        box.getChildren().add(text);
        // 启动初始图片
        Image bigMouthImage = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\pokemon-ball.png");
        ImageView imageView = new ImageView();


        imageView.setImage(bigMouthImage);
        imageView.setFitWidth(VIEW_WIDTH);
        imageView.setFitHeight(VIEW_HEIGHT);
        imageView.setStyle("-fx-background:transparent;");
        // 将面板整个设置成手的形状
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> imageView.setCursor(Cursor.HAND));


        imageView.setImage(bigMouthImage);
        imageView.setFitWidth(VIEW_WIDTH);
        imageView.setFitHeight(VIEW_HEIGHT);

        // 设置菜单
        final ContextMenu clickMenu = new ContextMenu();
        clickMenu.setStyle("-fx-background:transparent;");
        clickMenu.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            // 当用户右键点击菜单时
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
            }
        });
        // 处理左键点击主要事件
        clickMenu.setOnAction(event -> {
            String menuText = ((javafx.scene.control.MenuItem) event.getTarget()).getText();
            if ("吐槽一下".equals(menuText)) {
                new SendTipBox().display(stage);
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
                sleeper.setOnSucceeded(event1 -> System.exit(0));
                new Thread(sleeper).start();
            }
            System.out.println("right gets consumed so this must be left on " +
                    menuText);
        });
        javafx.scene.control.MenuItem letterItem = new javafx.scene.control.MenuItem("吐槽一下");
        javafx.scene.control.MenuItem voiceItem = new javafx.scene.control.MenuItem("叮咚");
        javafx.scene.control.MenuItem quitItem = new javafx.scene.control.MenuItem("退出");
        clickMenu.getItems().addAll(letterItem, voiceItem, quitItem);


        // 设置根pane
        StackPane pane = new StackPane();
        // 将其他组件加入
        pane.getChildren().addAll(imageView);
        pane.setStyle("-fx-background-color: transparent;");
        final Scene scene = new Scene(pane,300, 250);
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();
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
                    } else {
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
