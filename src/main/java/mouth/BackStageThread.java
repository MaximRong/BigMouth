package mouth;

import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mouth.util.SoundUtil;
import mouth.widgets.MessageBox;
import mouth.widgets.SendTipBox;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author Maxim
 */
public class BackStageThread extends Task<List<String>> {

    private Stage stage;
    private ImageView imageView;

    public BackStageThread(Stage stage, ImageView imageView) {
        this.stage = stage;
        this.imageView = imageView;
    }

    @Override
    protected List<String> call() throws Exception {
        for (; ; ) {
            Thread.sleep(5000);
            Jedis jedis = new Jedis("localhost", 6379);  //指定Redis服务Host和port
            jedis.select(11); // 11号数据库
            String message = jedis.get("message"); //访问Redis服务


            jedis.del("message");
            jedis.close(); //使用完关闭连接

            if (StringUtils.isEmpty(message)) {
                continue;
            }

            // 设置声音文件，用于播放提醒
            SoundUtil.dingDong();
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
            sleeper.setOnSucceeded(event1 -> {

                String[] messageSplit = message.split("\\|");
                String type = messageSplit[0];
                String bisType = messageSplit[1];
                String result = messageSplit[2];
                if("openBill".equals(type)) {
                    new MessageBox().display(stage, imageView, bisType, result);
                } else if("error".equals(type)) {
                    new MessageBox().display(stage, imageView, bisType, "老大，您的问题已经收到，会尽快安排！放心！");
                } else if("message".equals(type)) {
                    new MessageBox().display(stage, imageView, bisType, result);
                }
            });
            new Thread(sleeper).start();



        }

    }
}
