package cn.partytime.common.cachekey;

/**
 * Created by lENOVO on 2016/9/14.
 */
public class DanmuCacheKey {

    /**
     * 发送弹幕缓存
     */
    public static final String SEND_DANMU_CACHE_LIST="send:danmu:cache:list:";

    /**
     * 电影弹幕缓存
     */
    public static final String SEND_FILM_DANMU_CACHE_LIST="send:film:danmu:cache:list";

    /**
     * 弹幕广播队列
     */
    public static final String PUB_DANMU_CACHE_LIST="pub:danmu:cache:list:";


    /**
     * 未发送队列
     */
    public static final String PUB_DANMU_CACHE_NOT_SEND_LIST="pub:danmu:cache:notsend:list:";

    /**
     * 弹幕确认队列
     */
    public static final String PUB_DANMU_PAY_SEND_SORTSET="pub:danmu:pay:send:sortset:";


    /**
     * 弹幕未发送队列
     */
    public static final String PUB_DANMU_PAY_NOT_SEND_SORTSET ="pub:danmu:pay:not:send:sortset:";


    public static final String TIMERDANMU_NOT_PLARY_RESOUR_LIST = "timerdanmu:notplay:resoure:list:";

}
