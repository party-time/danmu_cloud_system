package cn.partytime.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by administrator on 2017/5/22.
 */
@ConfigurationProperties(prefix = "h5temp")
public class H5TempUtil {

    private String ftlWritePath;

    private String ftlReadDir;

    private String htmlWritePath;

    private String htmlReadDir;

    public String getFtlWritePath() {
        return ftlWritePath;
    }

    public void setFtlWritePath(String ftlWritePath) {
        this.ftlWritePath = ftlWritePath;
    }

    public String getFtlReadDir() {
        return ftlReadDir;
    }

    public void setFtlReadDir(String ftlReadDir) {
        this.ftlReadDir = ftlReadDir;
    }

    public String getHtmlWritePath() {
        return htmlWritePath;
    }

    public void setHtmlWritePath(String htmlWritePath) {
        this.htmlWritePath = htmlWritePath;
    }

    public String getHtmlReadDir() {
        return htmlReadDir;
    }

    public void setHtmlReadDir(String htmlReadDir) {
        this.htmlReadDir = htmlReadDir;
    }
}

