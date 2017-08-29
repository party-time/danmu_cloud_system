package cn.partytime.common.util;

import java.util.List;
import java.util.Set;

public class SetUtils {

    /**
     * 判断list是不是空
     *
     * @param set
     * @return
     */
    public static boolean checkSetIsNotNull(Set set) {
        if (set != null && !set.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean checkSetIsNull(Set set){
        if (set != null && !set.isEmpty()) {
            return true;
        }
        return false;
    }

}
