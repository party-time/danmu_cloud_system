package cn.partytime.service.projector;

import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.model.projector.ProjectorAction;
import cn.partytime.repository.manager.Projector.ProjectorActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dm on 2017/6/14.
 */

@Service
public class ProjectorActionService {


    @Autowired
    private ProjectorActionRepository projectorActionRepository;


    public Page<ProjectorAction> findProjectorActionPage(String registorCode, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return projectorActionRepository.findByRegisterCode(registorCode, pageRequest);
    }


    public List<ProjectorAction> findByRegisterCodeAndStartTimeAfter(String registorCode, Date startDate) {
        return projectorActionRepository.findByRegisterCodeAndStartTimeAfterOrderByUpdateTimeDesc(registorCode,startDate);
    }


    public ProjectorAction save(ProjectorAction projectorAction){
        return projectorActionRepository.save(projectorAction);
    }

}
