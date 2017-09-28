package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPartyServiceHystrix;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */
@FeignClient(value = "${dataRpcServer}",fallback = RpcPartyServiceHystrix.class)
public interface RpcPartyService {


    /**
     * 获取在线的电影
     * @param command
     * @return
     */
    @RequestMapping(value = "/rpcParty/findByMovieAliasOnLine" ,method = RequestMethod.GET)
    public PartyModel findByMovieAliasOnLine(@RequestParam(name = "command") String command);
    /**
     * 通过活动获取活动相关的场地
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/rpcParty/findAddressIdListByPartyId" ,method = RequestMethod.GET)
    public List<String> findAddressIdListByPartyId(@RequestParam(name = "partyId") String partyId);

    /**
     * 通过地址编号获取当前正在进行的活动或电影
     * @param addressId
     * @return
     */
    @RequestMapping(value = "/rpcParty/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByAddressId(@RequestParam(name = "addressId") String addressId);

    /**
     * 通过地址编号获取当前正在进行的电影
     * @param addressId
     * @return
     */
    @RequestMapping(value = "/rpcParty/findFilmByAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findFilmByAddressId(@RequestParam(name = "addressId") String addressId);

    /**
     * 通过地址编号获取当前正在进行的临时活动
     * @param addressId
     * @return
     */
    @RequestMapping(value = "/rpcParty/findTempPartyByAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findTempPartyByAddressId(@RequestParam(name = "addressId") String addressId);

    /**
     * 通过活动编号获取活动
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/rpcParty/findPartyByPartyId" ,method = RequestMethod.GET)
    public PartyModel getPartyByPartyId(@RequestParam(name = "partyId") String partyId);

    /**删除活动 @param partyId*/
    @RequestMapping(value = "/rpcParty/deleteParty" ,method = RequestMethod.GET)
    public void deleteParty(@RequestParam(name = "partyId")  String partyId);

    /**
     * 判断活动是否结束
     * @param partyModel
     * @return
     */
    @RequestMapping(value = "/rpcParty/checkPartyIsOver" ,method = RequestMethod.POST)
    public boolean checkPartyIsOver(PartyModel partyModel);

    /**
     * g通过经纬度获取活动信息
     * @param longitude
     * @param latitude
     * @return
     */
    @RequestMapping(value = "/rpcParty/findPartyByLonLat" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByLonLat(@RequestParam(name = "longitude") Double longitude, @RequestParam(name = "latitude") Double latitude);


    /**
     * 通过地址获取临时活动
     * @param addressId
     * @return
     */
    @RequestMapping(value = "/rpcParty/findTemporaryParty" ,method = RequestMethod.GET)
    public PartyLogicModel findTemporaryParty(@RequestParam(name = "addressId") String addressId);

    /**
     * 获取活动的弹幕密度
     * @param addressId
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/rpcParty/getPartyDmDensity" ,method = RequestMethod.GET)
    public int getPartyDmDensity(@RequestParam(name = "addressId") String addressId,@RequestParam(name = "partyId") String partyId);

    /**
     * 通过地址和活动状态获取活动列表
     * @param addressId
     * @param status
     * @return
     */
    @RequestMapping(value = "/rpcParty/findByAddressIdAndStatus" ,method = RequestMethod.GET)
    public List<PartyModel> findByAddressIdAndStatus(@RequestParam(name = "addressId") String addressId, @RequestParam(name = "status") Integer status);

    @RequestMapping(value = "/rpcParty/findByAddressIdAndType" ,method = RequestMethod.GET)
    public List<PartyModel> findByAddressIdAndType(@RequestParam(name = "addressId") String addressId, @RequestParam(name = "status") Integer status);

    /**
     * 通过活动类型和活动状态获取活动列表
     * @param type
     * @param status
     * @return
     */
    @RequestMapping(value = "/rpcParty/findByTypeAndStatus" ,method = RequestMethod.GET)
    public List<PartyModel> findByTypeAndStatus(@RequestParam(name = "type") Integer type, @RequestParam(name = "status") Integer status);

    /**
     * 保存活动信息
     * @param party
     * @return
     */
    @RequestMapping(value = "/rpcParty/saveParty" ,method = RequestMethod.POST)
    public PartyModel saveParty(PartyModel party);


    @RequestMapping(value = "/rpcParty/findCurrentParyIsInProgress" ,method = RequestMethod.POST)
    public boolean findCurrentParyIsInProgress(PartyModel party);

}
