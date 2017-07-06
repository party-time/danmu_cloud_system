package cn.partytime.service;

import cn.partytime.model.manager.H5Template;
import cn.partytime.repository.manager.H5TemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by administrator on 2017/4/28.
 */
@Slf4j
@Service
public class H5TemplateService {

    @Autowired
    private H5TemplateRepository h5TemplateRepository;



    public H5Template save(String tempTitle, String h5Url, String html, Integer isIndex, Integer type,
                           Integer isBase, Integer payMoney, String payTitle){
        if(!StringUtils.isEmpty(h5Url)){
            H5Template h5Template = findByH5Url(h5Url);
            if( null != h5Template){
                return null;
            }
        }

        Integer count = countByIsBase(isBase);
        if( isBase == 0 && count > 0){
            return null;
        }

        H5Template h5Template = new H5Template();
        h5Template.setTempTitle(tempTitle);
        h5Template.setH5Url(h5Url);
        h5Template.setHtml(html);
        h5Template.setIsIndex(isIndex);
        h5Template.setType(type);
        h5Template.setIsBase(isBase);
        h5Template.setPayMoney(payMoney);
        h5Template.setPayTitle(payTitle);
        return h5TemplateRepository.save(h5Template);
    }

    public void update(String id,String tempTitle,String h5Url,String html,Integer isIndex,Integer type,
                       Integer isBase,Integer payMoney,String payTitle){
        H5Template h5Template = this.findById(id);
        if( null != h5Template){
            h5Template.setTempTitle(tempTitle);
            h5Template.setH5Url(h5Url);
            h5Template.setHtml(html);
            h5Template.setIsIndex(isIndex);
            h5Template.setType(type);
            h5Template.setIsBase(isBase);
            h5Template.setPayMoney(payMoney);
            h5Template.setPayTitle(payTitle);
            h5TemplateRepository.save(h5Template);
        }
    }

    public H5Template findById(String id){
        return h5TemplateRepository.findOne(id);
    }

    public H5Template findByH5Url(String h5Url){
        return h5TemplateRepository.findByH5Url(h5Url);
    }

    public void delById(String id){
        h5TemplateRepository.delete(id);
    }


    public Page<H5Template> findAll(Integer page, Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return h5TemplateRepository.findAll(pageRequest);
    }

    public Integer countByIsBase(Integer isBase){
        return h5TemplateRepository.countByIsBase(isBase);
    }

    public Integer countByH5Url(String h5Url){
        return h5TemplateRepository.countByH5Url(h5Url);
    }

    public Integer countByIsBaseAndIdNot(Integer isBase,String id){
        return h5TemplateRepository.countByIsBaseAndIdNot(isBase,id);
    }

    public Integer countByH5UrlAndIdNot(String h5Url,String id){
        return h5TemplateRepository.countByH5UrlAndIdNot(h5Url,id);
    }


    public Page<H5Template> findByIsIndex(Integer isIndex, Integer page, Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return h5TemplateRepository.findByIsIndex(isIndex,pageRequest);
    }

    public H5Template findByIsBase(Integer isBase){
        return h5TemplateRepository.findByIsBase(isBase);
    }


}
