package mouth;

import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Maxim
 */
public class BackStageThread extends Task<List<String>> {

    private String classPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();

    @Override
    protected List<String> call() throws Exception {
        for (;;) {
            Thread.sleep(5000);
            Jedis jedis = new Jedis("localhost", 6379);  //指定Redis服务Host和port
//            jedis.auth("xxxx"); //如果Redis服务连接需要密码，制定密码
            jedis.select(11); // 11号数据库
            String message = jedis.get("message"); //访问Redis服务
            jedis.del("message");
            jedis.close(); //使用完关闭连接

            if(StringUtils.isNotEmpty(message)) {
                // 设置声音文件，用于播放提醒
                String soundStr = classPath + "dingDong.mp3";
                Media sound = new Media(new File(soundStr).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
        }

    }
}
