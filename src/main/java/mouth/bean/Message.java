package mouth.bean;

public class Message {

    // 封装后的内容
    private String content;

    // 原始内容
    private String orig;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrig() {
        return orig;
    }

    public void setOrig(String orig) {
        this.orig = orig;
    }

    public int origLength() {
        return this.orig.length();
    }
}
