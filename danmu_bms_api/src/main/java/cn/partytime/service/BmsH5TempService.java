package cn.partytime.service;

import cn.partytime.model.manager.H5Template;
import cn.partytime.util.H5TempUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by administrator on 2017/5/22.
 */

@Slf4j
@Service
public class BmsH5TempService {

    @Autowired
    private H5TemplateService h5TemplateService;

    @Autowired
    private H5TempUtil h5TempUtil;


    public H5Template save(String tempTitle,String h5Url,String html,Integer isIndex,Integer type,
                           Integer isBase,Integer payMoney,String payTitle){
        H5Template h5Template = h5TemplateService.save(tempTitle,h5Url,html,isIndex,type,isBase,payMoney,payTitle);
        writeFile(type,h5Url,html);
        return h5Template;
    }

    public void update(String id,String tempTitle,String h5Url,String html,Integer isIndex,Integer type,
                       Integer isBase,Integer payMoney,String payTitle){
        h5TemplateService.update(id,tempTitle,h5Url,html,isIndex,type,isBase,payMoney,payTitle);
        writeFile(type,h5Url,html);
    }

    private void writeFile(Integer type,String h5Url,String html){
        String savePath = "";
        String fileExt = "";
        if( type == 0){
            savePath = h5TempUtil.getFtlWritePath();
            fileExt = ".ftl";
        }else if( type == 1){
            savePath = h5TempUtil.getHtmlWritePath();
            fileExt = ".html";
        }
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File file = new File(savePath + File.separator + h5Url+fileExt);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(html.getBytes());
        }catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }
}
