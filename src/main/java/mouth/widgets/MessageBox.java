package mouth.widgets;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class MessageBox {

    public void display(String message){
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
            String text = textArea.getText();
            System.out.println("Your name: " + text);

            // 吐槽完了之后完毕对话框
            window.close();
        });
        closeBtn.setOnAction(e -> window.close());

//        Label label = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(textArea , sendBtn, closeBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

}
