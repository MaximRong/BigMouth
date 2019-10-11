package mouth.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Objects;

public class SoundUtil {

    private static String classPath = Objects.requireNonNull(SoundUtil.class.getClassLoader().getResource("")).getPath();

    public static void dingDong() {
        String soundStr = classPath + "wawa.m4r";
        Media sound = new Media(new File(soundStr).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public static void ge() {
        String soundStr = classPath + "belching.mp3";
        Media sound = new Media(new File(soundStr).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public static MediaPlayer say() {
        String soundStr = classPath + "say.mp3";
        Media sound = new Media(new File(soundStr).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        return mediaPlayer;
    }
}
