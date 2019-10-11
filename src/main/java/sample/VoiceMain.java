package sample;

import javafx.scene.media.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

/**
 * @author Maxim
 * 声音示例
 */
public class VoiceMain extends Application {

    String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        // 这个是获取此文件编译后的路径地址

        String soundStr = path + "wawa.m4r";
        Media sound = new Media(new File(soundStr).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();
    }

}
