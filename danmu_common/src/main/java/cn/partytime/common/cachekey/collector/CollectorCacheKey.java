package cn.partytime.common.cachekey.collector;

public class CollectorCacheKey {


    public static final String BASE_KEY="collector";

    public static final String BASE_ALARM_KEY="collector:alarm";

    public static final String COLLECTOR_ADDRESS_IP_CACHEC_LIST="collector:iplist:";

    /**
     * 客户端id编号 client:id:sortset:address
     */
    public static final String COLLECTOR_CLIENT_CACHE_SORTSET = "collector:client:sortSet:";

    /**flash:0, moblie:1; javaclient:2*/
    public static final String COLLECTOR_CLIENT_COUNT="collector:client:count:";

    public static final String COLLECTOR_FLASH_CLIENT_OFFLINE_TIME ="client:flash:offline:time:";

    public static final String COLLECTOR_CLIENT_CACHE_KEY="check:adminUser";

}
