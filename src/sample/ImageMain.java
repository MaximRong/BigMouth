package sample;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 图片展示
 *
 * @author Maxim
 */
public class ImageMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.initStyle(StageStyle.UNDECORATED);

//            primaryStage.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> primaryStage.setCursor(Cursor.HAND));

            Label dropped = new Label("");

            Image hello = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\换热机组\\hello.gif");
//            Image image = new Image("http://docs.oracle.com/javafx/"
//                    + "javafx/images/javafx-documentation.png");
            ImageView imageView = new ImageView();
            imageView.setImage(hello);

            double imageHeight = hello.getHeight();
            double imageWidth = hello.getWidth();
            // Display image on screen
            StackPane pane = new StackPane();
            pane.getChildren().addAll(imageView, dropped);
            Scene scene = new Scene(pane, imageWidth, imageHeight);

            primaryStage.setTitle("Image Read Test");
            primaryStage.setScene(scene);
            primaryStage.show();

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
                    dropped.setText(db.getFiles().toString());
                    imageView.setImage(new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\换热机组\\move.gif"));

                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            });
//            Thread.sleep(3000);
//            Image move = new Image("file:C:\\Users\\86186\\Desktop\\牛栏山\\换热机组\\move.gif");
//            imageView.setImage(move);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
