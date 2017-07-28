package cn.partytime.common.cachekey;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Client缓存key标识
 * Created by 杨腾飞 on 2016/6/21.
 */
public class ClientCacheKey {


    /**
     * 客户端id编号 client:id:sortset:address
     */
    public static final String CLIENT_CACHE_ID_SORTSET = "client:id:sortset:";


    public static final String ClIENT_OFFLINE_TIME="client:offline:time";

    public static final String CLIENT_OFFLINE_ALARM_COUNT="client:offlinealarm:count";


    /**
     * 客户端弹幕缓存
     */
    public static final String CLIENT_DANMU_CHACHE="client:danmu:cache:";



}
