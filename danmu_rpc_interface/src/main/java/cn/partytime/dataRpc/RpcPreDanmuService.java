package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPreDanmuServiceHystrix;
import cn.partytime.model.PreDanmuModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/10.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcPreDanmuServiceHystrix.class)
public interface RpcPreDanmuService {




    @RequestMapping(value = "/rpcRepDanmu/updateDensityByPartyIdAndLiBraryIdAndDensity" ,method = RequestMethod.GET)
    public void updateDensityByPartyIdAndLiBraryIdAndDensity(@RequestParam(name = "partyId")  String partyId,@RequestParam(name = "libraryId")  String libraryId,@RequestParam(name = "density")  Integer density);

    @RequestMapping(value = "/rpcRepDanmu/getPartyDanmuDensity" ,method = RequestMethod.GET)
    public int getPartyDanmuDensity(@RequestParam(name = "partyId") String partyId);

    @RequestMapping(value = "/rpcRepDanmu/initPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void initPreDanmuIntoCache(@RequestParam(name = "partyId") String partyId,@RequestParam(name = "addressId") String addressId);

    @RequestMapping(value = "/rpcRepDanmu/checkIsReInitPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void checkIsReInitPreDanmuIntoCache(@RequestParam(name = "partyId") String partyId,@RequestParam(name = "addressId") String addressId);


    @RequestMapping(value = "/rpcRepDanmu/reInitPreDanmuIntoCache" ,method = RequestMethod.GET)
    public void reInitPreDanmuIntoCache(@RequestParam(name = "partyId") String partyId,@RequestParam(name = "addressId") String addressId);


    @RequestMapping(value = "/rpcRepDanmu/getPreDanmuFromCache" ,method = RequestMethod.GET)
    public Map<String,Object> getPreDanmuFromCache(@RequestParam(name = "partyId") String partyId,@RequestParam(name = "addressId") String addressId, @RequestParam(name = "danmuCount") int danmuCount);

    @RequestMapping(value = "/rpcRepDanmu/setPreDanmuLibrarySortRule" ,method = RequestMethod.GET)
    public void setPreDanmuLibrarySortRule(@RequestParam(name = "partyId") String partyId) ;

    @RequestMapping(value = "/rpcRepDanmu/removePreDanmuCache" ,method = RequestMethod.GET)
    public void removePreDanmuCache(@RequestParam(name = "partyId") String partyId,@RequestParam(name = "addressId") String addressId) ;



    @RequestMapping(value = "/rpcRepDanmu/findByPartyId" ,method = RequestMethod.GET)
    public List<PreDanmuModel> findByPartyId(@RequestParam(name = "partyId") String partyId);


    @RequestMapping(value = "/rpcRepDanmu/findDanmuLibraryIdByParty" ,method = RequestMethod.GET)
    public List<String> findDanmuLibraryIdByParty(@RequestParam(name = "partyId") String partyId);


    @RequestMapping(value = "/rpcRepDanmu/findPreDanmuByLibraryId" ,method = RequestMethod.GET)
    public List<PreDanmuModel> findPreDanmuByLibraryId(@RequestParam(name = "libraryId") String libraryId,@RequestParam(name = "page") int page,@RequestParam(name = "size") int size) ;

    @RequestMapping(value = "/rpcRepDanmu/findPreDanmuCountByLibraryId" ,method = RequestMethod.GET)
    public long findPreDanmuCountByLibraryId(@RequestParam(name = "libraryId") String libraryId);




}
