package cn.partytime.service.wechat;


import com.baidu.aip.speech.AipSpeech;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Service
public class BmsWechatMiniService {

    @Value("${video_appId}")
    public static String appId;

    @Value("${video_appKey}")
    public static String appKey;

    @Value("${video_appsecret}")
    public static String secret;

    public String convertVedioToWord(String path) throws JSONException {

        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(appId, appKey, secret);

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
        JSONObject res = client.asr(path, "wav", 16000, null);
        //System.out.println(res.toString(2));
        log.info("res.toString()-----------------"+res.toString(2));
        return res.toString();
    }

    private String execShell(String shellString) {
        log.info(shellString);
        Process process = null;
        StringBuffer sb = new StringBuffer();
        try {
            String[] commands = { "/bin/sh", "-c", shellString };
            log.info("exec cmd:"+shellString);
            process = Runtime.getRuntime().exec(commands);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            input.close();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call sh failed. error code is :" + exitValue);
            } else {
                log.info("sh exec success");
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return sb.toString();
    }

}
