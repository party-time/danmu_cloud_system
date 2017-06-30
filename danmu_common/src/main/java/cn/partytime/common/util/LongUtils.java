package cn.partytime.common.util;

/**
 * Created by lENOVO on 2016/10/27.
 */
public class LongUtils {
    public static long objectConvertToLong(Object object) {
        String str = String.valueOf(object);
        boolean isNum = str.matches("[0-9]+");
        if(isNum){
            return Long.parseLong(str);
        }else{
            return  0;
        }
    }
}
