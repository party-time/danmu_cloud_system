package cn.partytime.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by lENOVO on 2016/9/28.
 */
public class ListUtils {

    /**
     * 两个list合并 去重
     *
     * @param srcList
     * @param mergeList
     * @return
     */
    public static List<String> listMerge(List<String> srcList, List<String> mergeList) {
        List<String> list = new ArrayList<>();
        Set<String> stringSet = new TreeSet<String>(srcList);
        mergeList.stream().forEach(e -> stringSet.add(e));
        for (String str : stringSet) {
            list.add(str);
        }
        return list;
    }

    /**
     * 判断list是不是空
     *
     * @param list
     * @return
     */
    public static boolean checkListIsNotNull(List list) {
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean checkListIsNull(List list){
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

}
