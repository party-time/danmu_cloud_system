package cn.partytime.service;

import cn.partytime.model.manager.AdminUserCompany;
import cn.partytime.repository.manager.AdminUserCompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class AdminUserCompanyService {

    @Autowired
    private AdminUserCompanyRepository adminUserCompanyRepository;

    /**
     * 添加一个公司
     * @param adminuserCompany
     * @return
     */
    public AdminUserCompany save(AdminUserCompany adminuserCompany){
        return adminUserCompanyRepository.insert(adminuserCompany);
    }

    /**
     * 添加一组公司
     * @param adminUserCompanyList
     * @return
     */
    public List<AdminUserCompany> save(List<AdminUserCompany> adminUserCompanyList){
        return adminUserCompanyRepository.insert(adminUserCompanyList);
    }

    /**
     * 查找一个公司
     * @param id
     * @return
     */
    public AdminUserCompany findById(String id){
        return adminUserCompanyRepository.findById(id);
    }

    /**
     * 删除一个公司信息
     * @param id
     */
    public void deleteById(String id){
        adminUserCompanyRepository.delete(id);
    }

    /**
     * 更新一个公司信息
     * @param adminUserCompany
     */
    public void updateAdminUserCompany(AdminUserCompany adminUserCompany){
        if( null == adminUserCompany || StringUtils.isEmpty(adminUserCompany.getId())){
            throw new IllegalArgumentException("对象或者主键不能为空");
        }
        adminUserCompanyRepository.save(adminUserCompany);
    }

    /**
     * 查询一个用户的公司信息
     * @param adminUserId
     * @return
     */
    public AdminUserCompany findByAdminUserId(String adminUserId){
        if(StringUtils.isEmpty(adminUserId)){
            throw new IllegalArgumentException("adminUserId不能为空");
        }
        return adminUserCompanyRepository.findByAdminUserId(adminUserId);
    }


}
