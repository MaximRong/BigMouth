package mouth;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mouth.util.SoundUtil;
import mouth.widgets.MessageBox;
import mouth.widgets.SendTipBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Maxim
 * todo : 系统托盘图标
 * todo : 跳转自动打开对应页面
 * <p>
 * // Bulbasaur
 */
public class GigMouthMain extends Application {

    private static final int VIEW_HEIGHT = 150;
    private static final int VIEW_WIDTH = 150;
    private TrayIcon trayIcon;

    private double xOffSet = 0;
    private double yOffSet = 0;

    private String classPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();

    @Override
    public void start(Stage stage) {
        try {
            // 设计变成无边框的模式
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            // 始终保持在前端/不会被最小化
            stage.setAlwaysOnTop(true);
            stage.setTitle("大口怪兽");
            String url = new File(classPath + "pokeBal.png").toURI().toString();
            stage.getIcons().add(new Image(url));

            enableTray(stage);

            // 启动初始图片
            Image bigMouthImage = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\pokeBall.gif");
            ImageView imageView = new ImageView();


            imageView.setImage(bigMouthImage);
            imageView.setFitWidth(VIEW_WIDTH);
            imageView.setFitHeight(VIEW_HEIGHT);
            imageView.setStyle("-fx-background:transparent;");
            // 将面板整个设置成手的形状
            imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> imageView.setCursor(Cursor.HAND));

            // 设置菜单
            final ContextMenu clickMenu = new ContextMenu();
            clickMenu.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                // 当用户右键点击菜单时
                if (event.getButton() == MouseButton.SECONDARY) {
                    event.consume();
                }
            });
            // 处理左键点击主要事件
            clickMenu.setOnAction(event -> {
                String menuText = ((MenuItem) event.getTarget()).getText();
                if ("吐槽一下".equals(menuText)) {
                    new SendTipBox().display(stage, imageView);
                } else if ("叮咚".equals(menuText)) {
                    // 设置声音文件，用于播放提醒
                    String soundStr = classPath + "wawa.m4r";
                    Media sound = new Media(new File(soundStr).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                } else {
                    imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_bye.gif"));

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

            MenuItem letterItem = new MenuItem("吐槽一下");
//            MenuItem voiceItem = new MenuItem("叮咚");
            MenuItem quitItem = new MenuItem("退出");
            clickMenu.getItems().addAll(letterItem, quitItem);


            // 设置根pane
            StackPane pane = new StackPane();
            // 设置的右键菜单后，一定要设置父面板背景透明，否则透明效果无效
            pane.setStyle("-fx-background-color: transparent;");
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


            pane.setOnDragEntered(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    List<File> files = db.getFiles();
                    File file = files.get(0);
                    // 处理的过场动画
                    imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_zhangzui.png"));
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.consume();
            });

            pane.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    List<File> files = db.getFiles();
                    File file = files.get(0);
                    String name = file.getName();
                    if (!"xls".equals(name.split("\\.")[1])) {
                        file.delete();
                        imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_jujue.gif"));
                        Task<Void> sleeper = new Task<Void>() {
                            @Override
                            protected Void call() {
                                try {
                                    Thread.sleep(2400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(event1 -> {
                            new MessageBox().display(stage, imageView, "GE", "嗝~香！");
                        });
                        new Thread(sleeper).start();


                    } else {
                        // 处理的过场动画
                        imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_jujue.gif"));
                        startFileUploadThread(imageView, stage, file);
                    }

                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);
                event.consume();
            });

            // 重复次数
            AtomicInteger duplication = new AtomicInteger();
            AtomicBoolean bigger = new AtomicBoolean(false);

            pane.setOnMousePressed(event -> {
//                System.out.println("1摇晃的次数是: " + duplication.get());
                xOffSet = event.getSceneX();
                yOffSet = event.getSceneY();
            });

            pane.setOnMouseReleased(event -> {
                duplication.set(0);
            });

            AtomicReference<Double> lastScreenX = new AtomicReference<>((double) 0);
            pane.setOnMouseDragged(event -> {
                double screenX = event.getScreenX();
                boolean lastBigger = bigger.get();
//                System.out.println("当前的点是 " + screenX + ", 之前的点是 " + lastScreenX);
                if (screenX != lastScreenX.get()) {
                    bigger.set(screenX > lastScreenX.get());
                    if (lastBigger != bigger.get()) {
                        duplication.incrementAndGet();
                    }


                }
                lastScreenX.set(screenX);

                if (duplication.get() <= 13) {
                    stage.setX(screenX - xOffSet);
                    stage.setY(event.getScreenY() - yOffSet);
                } else {
                    duplication.set(0);
                    // omnom_shengqi.gif
                    imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_yun.gif"));
                    new MessageBox().display(stage, imageView, "YUN", "老大，求您不要再晃我了，我招还不行嘛...云管家新版本已经配置了在线支付功能了哦！");

                    Task<Void> sleeper = new Task<Void>() {
                        @Override
                        protected Void call() {
                            try {
                                Thread.sleep(4200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(event1 -> {
//                        Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\omnom\\omnom.gif");
                        Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_kaishi.gif");
                        imageView.setImage(image);
                    });
                    new Thread(sleeper).start();
                }

            });

            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                // 右键点击事件
                if (e.getButton() == MouseButton.SECONDARY) {
                    clickMenu.show(pane, e.getScreenX(), e.getScreenY());
                } else {
                    if (2 == e.getClickCount()) {
                        imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_yun.gif"));

                        Task<Void> sleeper = new Task<Void>() {
                            @Override
                            protected Void call() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(event1 -> {
//                            Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\omnom\\omnom.gif");
                            Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_kaishi.gif");
                            imageView.setImage(image);
                        });
                        new Thread(sleeper).start();

                    }

                }
            });

            startBackStageThread(stage, imageView);

            // 设置组件大小
            Scene scene = new Scene(pane, VIEW_WIDTH, VIEW_HEIGHT);
            // 透明
            scene.setFill(null);
            stage.setScene(scene);

            // 初始化显示在屏幕的右下角
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 300);
            stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 300);


           stage.setOnShown(event -> {
                Task<Void> sleeperBang = new Task<Void>() {
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
                sleeperBang.setOnSucceeded(event1 -> {
//                    Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\boom.gif");
                    Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\boom.gif", VIEW_WIDTH, VIEW_HEIGHT, false, false);
                    imageView.setImage(image);
                    imageView.setFitWidth(VIEW_WIDTH);
                    imageView.setFitHeight(VIEW_HEIGHT);
                });
                new Thread(sleeperBang).start();

                Task<Void> sleeper = new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            Thread.sleep(2400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
//                sleeper.setOnSucceeded(event1 ->  imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\hello.gif")));
                sleeper.setOnSucceeded(event1 -> {
//                    Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\omnom\\omnom.gif", VIEW_WIDTH, VIEW_HEIGHT, false, false);
                    Image image = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\nimo\\nimo_kaishi.gif");
                    imageView.setImage(image);
                    imageView.setFitWidth(VIEW_WIDTH);
                    imageView.setFitHeight(VIEW_HEIGHT);
                });
                new Thread(sleeper).start();



            });

            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startBackStageThread(Stage stage, ImageView imageView) {
        BackStageThread backStageThread = new BackStageThread(stage, imageView);
        backStageThread.runningProperty().addListener((ov, wasRunning, isRunning) -> {
//                if (!isRunning) {
//
//                }
        });

        final Thread thread = new Thread(backStageThread, "backStageThread");
        thread.setDaemon(true);
        thread.start();
    }

    private void startFileUploadThread(ImageView imageView, Stage stage, File file) {
        FileUploadThread fileUploadThread = new FileUploadThread(imageView, stage, file);
        fileUploadThread.runningProperty().addListener((ov, wasRunning, isRunning) -> {
//                if (!isRunning) {
//
//                }
        });

        final Thread thread = new Thread(fileUploadThread, "fileUploadThread");
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
