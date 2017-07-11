package cn.partytime.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by administrator on 2017/5/22.
 */
@Component
public class H5TempUtil {

    @Value("${h5temp.ftlWritePath}")
    private String ftlWritePath;

    @Value("${h5temp.ftlReadDir}")
    private String ftlReadDir;

    @Value("${h5temp.htmlWritePath}")
    private String htmlWritePath;

    @Value("${h5temp.htmlReadDir}")
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

