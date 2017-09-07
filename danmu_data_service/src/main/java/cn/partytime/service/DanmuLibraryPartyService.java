package cn.partytime.service;


import cn.partytime.common.util.DateUtils;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.repository.danmu.DanmuLibraryPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 2016/10/21.
 */
@Service
public class DanmuLibraryPartyService {

    @Autowired
    private DanmuLibraryPartyRepository danmuLibraryPartyRepository;



    public DanmuLibraryParty save(String danmuLibraryId,String partyId,int densitry){
        Date date = DateUtils.getCurrentDate();
        DanmuLibraryParty danmuLibraryParty = danmuLibraryPartyRepository.findByPartyIdAndDanmuLibraryId(partyId,danmuLibraryId);
        if( null == danmuLibraryParty) {
            danmuLibraryParty = new DanmuLibraryParty();
            danmuLibraryParty.setCreateTime(date);
        }
        danmuLibraryParty.setUpdateTime(date);
        danmuLibraryParty.setPartyId(partyId);
        danmuLibraryParty.setDanmuLibraryId(danmuLibraryId);
        danmuLibraryParty.setDensitry(densitry);
        danmuLibraryPartyRepository.save(danmuLibraryParty);
        return danmuLibraryParty;
    }


    public void delDanmuLibraryParty(String danmuLibraryId){
        List<DanmuLibraryParty> danmuLibraryPartyList =danmuLibraryPartyRepository.findByDanmuLibraryId(danmuLibraryId);
        if( null != danmuLibraryPartyList){
            for(DanmuLibraryParty danmuLibraryParty : danmuLibraryPartyList){
                danmuLibraryPartyRepository.delete(danmuLibraryParty);
            }
        }
    }

    public void deleteById(String id){
        danmuLibraryPartyRepository.delete(id);
    }

    public DanmuLibraryParty findByPartyIdAndLibraryId(String partyId,String libraryId){
        return danmuLibraryPartyRepository.findByPartyIdAndDanmuLibraryId(partyId,libraryId);
    }

    public List<DanmuLibraryParty> findByPartyId(String partyId){
        return danmuLibraryPartyRepository.findByPartyId(partyId);
    }








}
