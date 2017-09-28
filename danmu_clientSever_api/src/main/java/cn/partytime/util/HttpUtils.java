package cn.partytime.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by administrator on 2017/9/21.
 */
@Slf4j
public class HttpUtils {

    /**
     * http请求
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpRequestStr(String requestUrl, String requestMethod, String outputStr) {
        System.out.println("url:"+requestUrl);
        StringBuffer buffer = new StringBuffer();
        try {
            //requestUrl = requestUrl.replaceAll(requestUrl, URLEncoder.encode(requestUrl, "utf-8"));
            //requestUrl = URLEncoder.encode(requestUrl, "utf-8");
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.setRequestProperty("Content-type", "text/html");
            httpUrlConn.setRequestProperty("Accept-Charset", "utf-8");
            httpUrlConn.setRequestProperty("contentType", "utf-8");
            httpUrlConn.setReadTimeout(6000);
            httpUrlConn.setConnectTimeout(6000);
            if ("GET".equalsIgnoreCase(requestMethod))

                httpUrlConn.connect();


            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        } catch (ConnectException ce) {
            log.error("http server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        log.info("getInfo  :" + buffer.toString());
        return buffer.toString();
    }
}
