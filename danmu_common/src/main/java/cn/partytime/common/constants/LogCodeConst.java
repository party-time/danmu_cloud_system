package cn.partytime.common.constants;

/**
 * Created by dm on 2017/6/29.
 */
public interface LogCodeConst {

    public class DanmuLogCode{
        public static final String DANMU_ISNULL_CODE="0100";
        public static final String DANMU_ISMORE_EXCEPTION="0101";
        public static final String DANMU_IS_OK="0102";

        public static final String PREDANMU_ISNULL_CODE = "0200";
        public static final String HISTORYDANMU_ISNULL_CODE = "0300";
        public static final String TIMERDANMU_ISNULL_CODE = "0400";
    }

    public class PartyLogCode{
        public static final String MOVIE_TIME_MORE_THAN_150="1000";
        public static final String MOVIE_TIME_LESS_THAN_60="1001";

    }

    public class DeviceLogCode{
        public static final String PROJECTOR_OPEN_FAIL="2000";
        public static final String PROJECTOR_CLOSE_FAIL="2001";
        public static final String PROJECTOR_BULB_LIFE_MORE_THAN_PRECENT80="2001";
    }

    public class CLientLogCode{
        public static final String FLASH_NETWORK_EXCEPTION="3000";
    }

    public class ResourceLogCode{

    }

    public class AdminLogCode{

    }

    public class MoneyLogCode{

    }
}
