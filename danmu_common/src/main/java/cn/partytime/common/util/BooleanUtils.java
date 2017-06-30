package cn.partytime.common.util;

/**
 * Created by lENOVO on 2016/10/8.
 */
public class BooleanUtils {

    public static boolean objectConvertToBoolean(Object str){
        return Boolean.parseBoolean(String.valueOf(str));
    }
}
