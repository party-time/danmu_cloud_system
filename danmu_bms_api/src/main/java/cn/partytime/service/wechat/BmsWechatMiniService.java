package cn.partytime.service.wechat;


import com.baidu.aip.speech.AipSpeech;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class BmsWechatMiniService {

    public static final String APP_ID = "xxxxxxx";
    public static final String API_KEY = "xxxxxxxxxxx";
    public static final String SECRET_KEY = "xxxxxxxxxxxxxxxx";

    public String convertVedioToWord() throws JSONException {

        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.asr("D:\\16.wav", "wav", 16000, null);
        //System.out.println(res.toString(2));
        return res.toString();
    }
}
