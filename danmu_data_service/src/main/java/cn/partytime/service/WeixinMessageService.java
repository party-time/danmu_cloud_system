package cn.partytime.service;

import cn.partytime.model.wechat.WeixinMessage;
import cn.partytime.repository.manager.WeixinMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by liuwei on 2016/9/23.
 */
@Service
public class WeixinMessageService {

    @Autowired
    private WeixinMessageRepository weixinMessageRepository;

    public WeixinMessage save(String words , String message,String mediaId,String mediaName){
        WeixinMessage weixinMessage = new WeixinMessage();
        weixinMessage.setMessage(message);
        weixinMessage.setWords(words);
        weixinMessage.setMediaId(mediaId);
        weixinMessage.setMediaName(mediaName);
        return weixinMessageRepository.save(weixinMessage);
    }

    public void save(List<WeixinMessage> weixinMessageList){
        weixinMessageRepository.save(weixinMessageList);
    }

    public void del(String id){
        weixinMessageRepository.delete(id);
    }

    public void update(String id,String words,String message){
        WeixinMessage weixinMessage = weixinMessageRepository.findOne(id);
        if( null == weixinMessage){
            throw new IllegalArgumentException("更新的自动回复不存在");
        }
        weixinMessage.setMessage(words);
        weixinMessage.setMessage(message);
        weixinMessageRepository.save(weixinMessage);
    }

    public void saveOrupdate(List<WeixinMessage> weixinMessageList){
        for(WeixinMessage weixinMessage : weixinMessageList){
            if(StringUtils.isEmpty(weixinMessage.getId())){
                weixinMessage.setId(null);
                weixinMessageRepository.insert(weixinMessage);
            }else{
                weixinMessageRepository.save(weixinMessage);
            }
        }
    }

    public List<WeixinMessage> findAll(){
        return weixinMessageRepository.findAll();
    }

    public Page<WeixinMessage> findAll(int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return weixinMessageRepository.findAll(pageRequest);
    }

    public Page<WeixinMessage> findByWordsLike(String words, int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return weixinMessageRepository.findByWordsLike(words,pageRequest);

    }

    public WeixinMessage findById(String id){

        return weixinMessageRepository.findOne(id);
    }

    public List<WeixinMessage> findByWords(String words){
        return weixinMessageRepository.findByWords(words);
    }


    public void updateWeixinMessageVoice(String id,String mediaId,String mediaName){
        WeixinMessage weixinMessage = this.findById(id);
        if( null != weixinMessage){
            weixinMessage.setMediaId(mediaId);
            weixinMessage.setMediaName(mediaName);
        }
        weixinMessageRepository.save(weixinMessage);
    }

    public void cancelWeixinMessageVoice(String id){
        WeixinMessage weixinMessage = this.findById(id);
        if( null != weixinMessage){
            weixinMessage.setMediaId(null);
            weixinMessage.setMediaName(null);
        }
        weixinMessageRepository.save(weixinMessage);
    }


}
