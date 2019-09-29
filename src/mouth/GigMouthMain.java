package mouth;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class GigMouthMain extends Application {

    private static final int VIEW_HEIGHT = 100;
    private static final int VIEW_WIDTH = 100;

    private double xOffSet = 0;
    private double yOffSet = 0;

    @Override
    public void start(Stage stage) {
        try {
            // 设计变成无边框的模式
            stage.initStyle(StageStyle.UNDECORATED);

            // 上传文本标签
            Label talkText = new Label("");

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
                    if("吐槽一下".equals(menuText)) {

                    } else {
                        imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\sleep.gif"));

                        Task<Void> sleeper = new Task<Void>() {
                            @Override
                            protected Void call() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                System.exit(0);
                            }
                        });
                        new Thread(sleeper).start();
                    }
                    System.out.println("right gets consumed so this must be left on " +
                            menuText);
                }
            });

            MenuItem letterItem = new MenuItem("吐槽一下");
            MenuItem quitItem = new MenuItem("退出");
            clickMenu.getItems().addAll(letterItem, quitItem);

            // 设置根pane
            StackPane pane = new StackPane();
            // 将其他组件加入
            pane.getChildren().addAll(imageView, talkText);
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
                    talkText.setText(db.getFiles().toString());
                    imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\move.gif"));
                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);
                event.consume();
            });

//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent event) {
//                    System.out.println("我退出啦！");
//                }
//            });


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
                        if(2 == e.getClickCount()){
                            System.out.println("double click");
                            imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\jumpLine.gif"));
                        }


//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
//                        imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\BigMouth\\hello.gif"));
                    }
                }
            });

            // 设置组件大小
            Scene scene = new Scene(pane, VIEW_WIDTH, VIEW_HEIGHT);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
