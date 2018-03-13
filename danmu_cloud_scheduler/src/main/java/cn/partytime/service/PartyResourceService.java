package cn.partytime.service;

import cn.partytime.dataRpc.RpcResourceFileService;
import org.springframework.beans.factory.annotation.Autowired;

public class PartyResourceService {

    @Autowired
    private RpcResourceFileService rpcResourceFileService;

    public void delEndPartyResourceFile(){
        rpcResourceFileService.delEndPartyResourceFile();
    }

}
