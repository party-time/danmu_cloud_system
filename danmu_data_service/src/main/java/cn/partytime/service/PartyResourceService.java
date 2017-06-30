package cn.partytime.service;

import cn.partytime.model.manager.PartyResource;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.repository.manager.PartyResourceRepository;
import cn.partytime.repository.manager.ResourceFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2016/11/21.
 */
@Service
@Slf4j
public class PartyResourceService {

    @Autowired
    private PartyResourceRepository partyResourceRepository;

    @Autowired
    private ResourceFileService resourceFileService;

    public List<PartyResource> findByPartyId(String partyId){
        return partyResourceRepository.findByPartyId(partyId);
    }

    public List<PartyResource> findByResourceId(String resourceId){
        return partyResourceRepository.findByResourceId(resourceId);
    }

    public List<PartyResource> findByPartyIdAndFileType(String partyId,Integer fileType){
        return partyResourceRepository.findByPartyIdAndFileType(partyId,fileType);
    }

    public PartyResource save(PartyResource partyResource){
        return partyResourceRepository.save(partyResource);
    }

    public PartyResource save(String partyId,String resourceId,Integer fileType){
        PartyResource partyResource = new PartyResource();
        partyResource.setPartyId(partyId);
        partyResource.setResourceId(resourceId);
        partyResource.setFileType(fileType);
        return partyResourceRepository.save(partyResource);
    }



    public void delByResourceId(String resourceId){
        List<PartyResource> partyResourceList = partyResourceRepository.findByResourceId(resourceId);
        if( null != partyResourceList && partyResourceList.size() > 0){
            partyResourceRepository.delete(partyResourceList);
        }
    }

    public void delById(String id){
        partyResourceRepository.delete(id);

    }

    public void delByPartyIdAndResourceId(String partyId,String resourceId){
        PartyResource partyResource = partyResourceRepository.findByPartyIdAndResourceId(partyId,resourceId);
        if( null != partyResource){
            partyResourceRepository.delete(partyResource.getId());
        }

    }


}
