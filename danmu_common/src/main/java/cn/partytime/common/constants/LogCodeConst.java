package cn.partytime.common.constants;

/**
 * Created by dm on 2017/6/29.
 */
public interface LogCodeConst {

    public class DanmuLogCode{
        public static final String CLIENT_DANMU_ISNULL="0100";
        public static final String CLIENT_DANMU_ISMORE="0101";
        public static final String DANMU_IS_OK="0102";

        public static final String PREDANMU_ISNULL = "0103";
        public static final String CLIENT_HISTORYDANMU_ISNULL = "0104";
        public static final String CLIENT_TIMERDANMU_ISNULL = "0105";

        public static final String CLIENT_TIMERDANMU_NOT_PLAY="0106";
    }

    public class PartyLogCode{

        //电影时间过长
        public static final String MOVIE_TIME_TOO_LONG="1000";
        //电影时间过短
        public static final String MOVIE_TIME_TOO_SHORT="1001";
        //电影开始异常
        public static final String MOVIE_START_ERROR="1002";

    }

    public class DeviceLogCode{
        public static final String PROJECTOR_NOT_OPEN="2000";
        public static final String PROJECTOR_NOT_CLOSE="2001";

        /**Java客户端执行脚本异常*/
        public static final String JAVACLIENT_EXECUTESHELL_EXCEPTION="2002";
    }

    public class CLientLogCode{
        public static final String FLASH_NETWORK_EXCEPTION="3000";

        public static final String BULB_LIFE_TIME="3001";
    }

    public class ResourceLogCode{

    }

    public class AdminLogCode{
        public static final String ADMIN_ONLINE_COUNT_ZERO="4000";
    }

    public class MoneyLogCode{

    }
}
