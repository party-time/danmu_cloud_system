package cn.partytime.service;

import cn.partytime.annotation.CommandAnnotation;
import cn.partytime.dataRpc.RpcResourceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartyResourceService {

    @Autowired
    private RpcResourceFileService rpcResourceFileService;


    public void delEndPartyResourceFile(){
        rpcResourceFileService.delEndPartyResourceFile();
    }

}
