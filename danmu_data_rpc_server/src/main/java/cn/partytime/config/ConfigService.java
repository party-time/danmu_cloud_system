package cn.partytime.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Value("${uploadClient.uploadPath}")
    private String uploadPath;

    @Value("${uploadClient.downloadPath}")
    private String downloadPath;

    @Value("${uploadClient.backupPath}")
    private String backupPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

}
