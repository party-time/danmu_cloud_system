package cn.partytime.common.cachekey.danmu;

/**
 * Created by lENOVO on 2016/9/14.
 */
public class DanmuCacheKey {


    /**
     * 要发送的的弹木信息
     */
    public static final String SEND_DANMU_INFO="send:danmu:";
    /**
     * 发送弹幕缓存
     */
    public static final String SEND_DANMU_CACHE_LIST="send:party:danmu:cache:list:";

    /**
     * 发送弹幕临时缓存
     */
    public static final String SEND_TEMP_DANMU_CACHE_LIST="send:temp:party:danmu:cache:list:";

    /**
     * 电影弹幕缓存
     */
    public static final String SEND_FILM_DANMU_CACHE_LIST="send:film:danmu:cache:list:";

    /**
     * 电影弹幕临时缓存
     */
    public static final String SEND_TEMP_FILM_DANMU_CACHE_LIST="send:temp:film:danmu:cache:list:";

    /**
     * 分配给管理员的活动弹幕
     */
    public static final String SEND_USER_DANMU_CACHE_LIST="send:party:user:danmu:cache:sortset:";

    /**
     * 分配给管理员的电影弹幕
     */
    public static final String SEND_USER_FILM_DANMU_CACHE_LIST="send:film:user:danmu:cache:sortset:";



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
