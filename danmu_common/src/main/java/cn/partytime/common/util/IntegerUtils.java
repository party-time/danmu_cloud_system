package cn.partytime.common.util;

/**
 * Created by lENOVO on 2016/10/8.
 */
public class IntegerUtils {

    public static int objectConvertToInt(Object object) {
        String str = String.valueOf(object);
        boolean isNum = str.matches("[0-9]+");
        if(isNum){
            return Integer.parseInt(str);
        }else{
            return  0;
        }
    }
}
