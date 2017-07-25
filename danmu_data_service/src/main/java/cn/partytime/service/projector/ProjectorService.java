package cn.partytime.service.projector;

import cn.partytime.model.projector.Projector;
import cn.partytime.model.projector.ProjectorAction;
import cn.partytime.repository.manager.Projector.ProjectorActionRepository;
import cn.partytime.repository.manager.Projector.ProjectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/6/14.
 */

@Service
public class ProjectorService {

    @Autowired
    private ProjectorRepository projectorRepository;


    public Projector findByRegisterCode(String registorCode) {
        return  projectorRepository.findByRegisterCode(registorCode);
    }

    public Projector findById(String id) {
        return  projectorRepository.findOne(id);
    }

    public Projector save(Projector projector){
        return projectorRepository.save(projector);
    }

}
