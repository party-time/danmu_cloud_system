package cn.partytime.util;

/**
 * Created by lENOVO on 2016/10/14.
 */
public class DanmuCheckUtil {

    public static boolean checkDanmuIsAllBlack(String str) {
        String replaceStr = str.replace(" ", " ").replace(" ", "");
        if (replaceStr.length() == 0) {
            return true;
        }
        return false;
    }
}
