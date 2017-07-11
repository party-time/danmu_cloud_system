package cn.partytime.controller.addanmu;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsPartyAddressAdRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/1/18.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/partyAddressAdRelation")
public class PartyAddressAdRelationController extends BaseAdminController {

    @Autowired
    private BmsPartyAddressAdRelationService bmsPartyAddressAdRelationService;

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public RestResultModel add(String partyId, String addressId, String poolId) {
        return bmsPartyAddressAdRelationService.PartyAddressAdAdd(partyId,addressId,poolId,getAdminUser().getId());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public RestResultModel delete(String partyId, String addressId) {
        RestResultModel restResultModel = new RestResultModel();
        bmsPartyAddressAdRelationService.deletePartyAddressAd(partyId,addressId);
        restResultModel.setResult(200);
        return restResultModel;
    }
}
