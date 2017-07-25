package cn.partytime.service;

import cn.partytime.model.manager.AdminRole;
import cn.partytime.repository.manager.AdminRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/2/7.
 */

@Service
@Slf4j
public class AdminRoleService {

    @Autowired
    private AdminRoleRepository adminRoleRepository;


    public AdminRole save(AdminRole adminRole){
        return adminRoleRepository.save(adminRole);
    }

    public List<AdminRole> findAll(){
        return adminRoleRepository.findAll();
    }



}
