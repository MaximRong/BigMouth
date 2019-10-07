package mouth;

import javafx.concurrent.Task;
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

    public BackStageThread(Stage stage) {
        this.stage = stage;
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
            String[] messageSplit = message.split("\\|");
            String type = messageSplit[0];
            String bisType = messageSplit[1];
            String result = messageSplit[2];
            if("openBill".equals(type)) {
                new MessageBox().display(stage, bisType, result);
            }
        }

    }
}
