package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.wechat.MaterlListPageResultJson;
import cn.partytime.model.wechat.WeixinMessage;
import cn.partytime.service.BmsWechatUserService;
import cn.partytime.service.WeixinMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by liuwei on 2016/9/23.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/wxmessage")
@Slf4j
public class WeixinMessageController {

    @Autowired
    private WeixinMessageService weixinMessageService;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel weixinMessageList(String words , int pageNumber, int pageSize){
        pageNumber = pageNumber-1;
        Page<WeixinMessage> weixinMessagePage = null;
        if(StringUtils.isEmpty(words)){
            weixinMessagePage = weixinMessageService.findAll(pageNumber,pageSize);
        }else{
            weixinMessagePage = weixinMessageService.findByWordsLike(words,pageNumber,pageSize);
        }

        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setRows(weixinMessagePage.getContent());
        pageResultModel.setTotal(weixinMessagePage.getTotalElements());
        return pageResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        weixinMessageService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String words, String message, String mediaId, String mediaName){
        RestResultModel restResultModel = new RestResultModel();
        weixinMessageService.save(words,message,mediaId,mediaName);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id, String words, String message){
        RestResultModel restResultModel = new RestResultModel();
        weixinMessageService.update(id,words,message);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public RestResultModel findById(String id){
        RestResultModel restResultModel = new RestResultModel();
        WeixinMessage weixinMessage = weixinMessageService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(weixinMessage);
        return restResultModel;
    }

    @RequestMapping(value = "/findByWords", method = RequestMethod.GET)
    public RestResultModel findByWords(String words){
        RestResultModel restResultModel = new RestResultModel();
        List<WeixinMessage> weixinMessageList = weixinMessageService.findByWords(words);
        restResultModel.setResult(200);
        restResultModel.setData(weixinMessageList.size());
        return restResultModel;
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST , consumes = "application/json")
    public RestResultModel saveOrUpdate(@RequestBody(required = false) List<WeixinMessage> weixinMessageList ){
        RestResultModel restResultModel = new RestResultModel();
        if( null != weixinMessageList && weixinMessageList.size() > 0){
            for(WeixinMessage weixinMessage : weixinMessageList){
                try {
                    weixinMessage.setWords(URLDecoder.decode(weixinMessage.getWords(),"utf-8"));
                    weixinMessage.setMessage(URLDecoder.decode(weixinMessage.getMessage(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            weixinMessageService.saveOrupdate(weixinMessageList);
        }
        restResultModel.setResult(200);
        return restResultModel;
    }

    /**
     * 查找上传的音频文件
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/findVoice", method = RequestMethod.GET)
    public PageResultModel findVoice(int page,int size){
        PageResultModel pageResultModel = new PageResultModel();
        MaterlListPageResultJson materlListPageResultJson = bmsWechatUserService.getVoice(page,size);
        pageResultModel.setRows(materlListPageResultJson.getItem());
        pageResultModel.setTotal(materlListPageResultJson.getTotal_count());
        return pageResultModel;
    }

    /**
     * 关联音频文件
     * @param id
     * @param mediaId
     * @param mediaName
     * @return
     */
    @RequestMapping(value = "/selectVoice", method = RequestMethod.GET)
    public RestResultModel selectVoice(String id, String mediaId, String mediaName){
        RestResultModel restResultModel = new RestResultModel();
        weixinMessageService.updateWeixinMessageVoice(id,mediaId,mediaName);
        restResultModel.setResult(200);
        return restResultModel;
    }

    /**
     * 取消关联音频文件
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancelVoice", method = RequestMethod.GET)
    public RestResultModel cancelVoice(String id){
        RestResultModel restResultModel = new RestResultModel();
        weixinMessageService.cancelWeixinMessageVoice(id);
        restResultModel.setResult(200);
        return restResultModel;
    }


}
