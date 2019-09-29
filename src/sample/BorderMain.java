package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Maxim
 * 边框修正示例
 */
public class BorderMain extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Group root = new Group();
        Scene scene = new Scene(root, 100, 100);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
