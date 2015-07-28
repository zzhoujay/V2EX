package zhou.v2ex.util;

/**
 * Created by zzhoujay on 2015/7/23 0023.
 * ContentUtil
 */
public class ContentUtils {
    public static String formatContent(String content) {
        if (content == null) {
            return null;
        }
        return content.replace("href=\"/member/", "href=\"http://www.v2ex.com/member/")
                .replace("href=\"/i/", "href=\"https://i.v2ex.co/")
                .replace("href=\"/t/", "href=\"http://www.v2ex.com/t/")
                .replace("href=\"/go/", "href=\"http://www.v2ex.com/go/");
    }
}
