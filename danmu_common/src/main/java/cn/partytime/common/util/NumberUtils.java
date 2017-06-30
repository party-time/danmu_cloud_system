package cn.partytime.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dm on 2017/4/25.
 */
public class NumberUtils {


    public static boolean checkNumberAfterPoint(String str,int point){
        String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){"+point+"})$";
        return str.matches(regex);
    }

    public static boolean checkNumberIsInteger(String str){
        String regex = "^(([1-9]{1}\\d*)|([0]{1}))$";
        return str.matches(regex);
    }

    public static boolean checkNumber(double value){
        String str = String.valueOf(value);
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return str.matches(regex);
    }

    public static boolean checkNumber(int value){
        String str = String.valueOf(value);
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return str.matches(regex);
    }

    public static boolean checkNumber(String value){
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }
}
