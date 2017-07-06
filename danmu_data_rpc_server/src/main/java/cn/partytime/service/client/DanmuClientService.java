package cn.partytime.service.client;

import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.RegistCode;
import cn.partytime.repository.manager.DanmuClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@RestController
@RequestMapping("/danmuClient")
public class DanmuClientService {


    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private DanmuClientRepository danmuClientRepository;

    @Autowired
    private RegistCodeService registCodeService;



    /**
     * 保存客户端信息
     * @param addressId
     * @param overdue
     * @param name
     * @return
     */
    public DanmuClient save(String addressId, Date overdue, String name, String paramTemplateId) {

        DanmuClient danmuClient = new DanmuClient();
        danmuClient.setAddressId(addressId);
        danmuClient.setName(name);
        danmuClient.setParamTemplateId(paramTemplateId);
        RegistCode registCode = registCodeService.save(overdue);
        danmuClient.setRegistCode(registCode.getRegistCode());
        return danmuClientRepository.insert(danmuClient);
    }

    /**
     * 更新客户端信息
     * @param danmuClient
     * @return
     */
    public DanmuClient update(DanmuClient danmuClient) {
        return danmuClientRepository.save(danmuClient);
    }


    /**
     * 通过屏目名称与地址编号获取客户端信息
     * @param addressId
     * @param name
     * @return
     */
    public DanmuClient findDanmuClientByAddressIdAndName(String addressId, String name){
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("addressId").is(addressId),
                Criteria.where("name").is(name)
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return managerMongoTemplate.findOne(query, DanmuClient.class);
    }


    public void deleteById(String id) {
        DanmuClient danmuClient = danmuClientRepository.findById(id);
        if( null != danmuClient){
            RegistCode registCode = registCodeService.findByRegistCode(danmuClient.getRegistCode());
            if( null != registCode){
                registCodeService.deleteById(registCode.getId());
            }
            danmuClientRepository.delete(id);
        }

    }

    public DanmuClient findById(String id) {
        return danmuClientRepository.findById(id);
    }

    public DanmuClient updateById(DanmuClient danmuClient) {
        return danmuClientRepository.save(danmuClient);
    }

    @RequestMapping(value = "/findByRegistCode" ,method = RequestMethod.GET)
    public DanmuClient findByRegistCode(@RequestParam String registCode) {
        return danmuClientRepository.findByRegistCode(registCode);
    }


    public DanmuClient findByDanmuClientCode(String danmuClientCode) {
        return danmuClientRepository.findByDanmuClientCode(danmuClientCode);
    }

    public Page<DanmuClient> findClientListByAddressId(String addressId, int page, int size, String sortby) {
        Sort sort = new Sort(Sort.Direction.DESC, sortby);
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuClientRepository.findByAddressId(pageRequest, addressId);
    }

    public Page<DanmuClient> findByAddressId(String addressId, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return danmuClientRepository.findByAddressId(pageRequest, addressId);
    }

    public List<DanmuClient> findByAddressId(String addressId){
        return danmuClientRepository.findByAddressId(addressId);
    }


    public DanmuClient verifyDanmuClient(String danmuClientCode){
        DanmuClient danmuClient = danmuClientRepository.findByDanmuClientCode(danmuClientCode);
        if( null != danmuClient && !StringUtils.isEmpty(danmuClient.getRegistCode())){
            RegistCode registCode = registCodeService.findByRegistCode(danmuClient.getRegistCode());
            if (null != registCode && (registCode.getOverdue() == null || registCode.getOverdue().after(new Date()))) {
                return danmuClient;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public DanmuClient registClient(String danmuClientCode, String registCode){
        if( StringUtils.isEmpty(danmuClientCode) || StringUtils.isEmpty(registCode)){
            throw new IllegalArgumentException("客户端code或者注册码为空");
        }
        RegistCode registCode1 = registCodeService.findByRegistCode(registCode);
        if( null ==registCode1){
            throw new IllegalArgumentException("注册码不存在");
        }
        DanmuClient danmuClient = this.findByRegistCode(registCode);
        if( null == danmuClient ){
            throw new IllegalArgumentException("注册的场地不存在");
        }
        DanmuClient danmuClient1 = danmuClientRepository.findByDanmuClientCode(danmuClientCode);

        //此客户端之前已经绑定过某个场地,解除绑定
        if( null != danmuClient1 && !StringUtils.isEmpty(danmuClient1.getRegistCode())){
            danmuClient1.setDanmuClientCode(null);
            danmuClientRepository.save(danmuClient1);
        }
        if( null != danmuClient){
            danmuClient.setDanmuClientCode(danmuClientCode);
        }

        danmuClientRepository.save(danmuClient);
        return danmuClient;
    }


    public void selectParamTemplate(String id,String paramTemplateId){
        DanmuClient danmuClient = findById(id);
        if( null != danmuClient){
            danmuClient.setParamTemplateId(paramTemplateId);
            danmuClientRepository.save(danmuClient);
        }

    }
}
