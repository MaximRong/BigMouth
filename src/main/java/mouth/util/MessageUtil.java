package mouth.util;

import mouth.bean.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class MessageUtil {

    static String html(String html, String content) {
        String TEMPLATE = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <script>\n" +
                "        var delay = 800;\n" +
                "        function appendHtml(id, s) {\n" +
                "            id = id || \"text\";\n" +
                "            var text = document.getElementById(id);\n" +
                "            text.innerText = text.innerText + s;\n" +
                "        }\n" +
                "\n" +
                "        function writeOneByOne(id, str) {\n" +
                "            var id = id || \"text\";\n" +
                "            for (var i = 0; i < str.length; i++) {\n" +
                "                setTimeout(\"appendHtml('\" + id + \"', '\" + str.charAt(i) + \"')\", delay + 50 * i);\n" +
                "            }\n" +
                "            delay = delay + 50 * (str.length - 1);\n" +
                "        }\n" +
                "\n" +
                "        document.addEventListener('DOMContentLoaded', function (event) {\n" +
                "            var str = \"thisIsHtml\";\n" +
                "            var body = document.getElementById(\"body\");\n" +
                "            body.insertAdjacentHTML( 'beforeend', str );\n" +
                "            thisIsWrite\n" +
                "        })\n" +
                "\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body id=\"body\">\n" +
                "</body>\n" +
                "</html>";

        String retHtml = StringUtils.replace(TEMPLATE, "thisIsHtml", html);
        retHtml = StringUtils.replace(retHtml, "thisIsWrite", content);
        System.out.println(retHtml);
        return retHtml;
    }

    public static StringBuilder append(String id, String info) {
        StringBuilder out = new StringBuilder();
        out.append("writeOneByOne('").append(id).append("', '").append(info).append("');\n");
        return out;
    }

    public static Message appendMessage(String id, String info) {
        StringBuilder out = new StringBuilder();
        out.append("writeOneByOne('").append(id).append("', '").append(info).append("');\n");
        Message message = new Message();
        message.setOrig(info);
        message.setContent(out.toString());
        return message;
    }

    public static String getPageUrl(String openTab) {
        return " href='http://localhost:9999/saas/main?opentab=" + openTab + "'";
    }

    public static String getPageUrl(String openTab, String params) {
        return " href='http://localhost:9999/saas/main?opentab=" + openTab + params + "'";
    }
}
