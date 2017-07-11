package cn.partytime.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jack on 15/6/29.
 */
public class CommonUtils {

  public static String getIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();
  }


  /**
   * 校验表情
   * @param s
   * @return
   */
  public static String filterEmoji(String s) {
    if (StringUtils.isEmpty(s)) {
      return s;
    }
    return s.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "").replaceAll("\\s+", " ").trim();
  }

}
