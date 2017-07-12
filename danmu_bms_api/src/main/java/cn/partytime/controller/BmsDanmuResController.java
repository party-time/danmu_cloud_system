package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.ResourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/11/18.
 */

@RestController
@RequestMapping(value = "/v1/api")
public class BmsDanmuResController {
    @Autowired
    private ResourceFileService resourceFileService;

    @RequestMapping(value = "/admin/initResource", method = RequestMethod.GET)
    public RestResultModel initResource(String partyId) {
        RestResultModel restResultModel = new RestResultModel();

        //通过活动获取资源
        Map<String, Object> map = resourceFileService.findResourceMapByPartyId(partyId);
        if (map != null && !map.isEmpty()) {
            List<ResourceFile> resourceFileModelList = (List<ResourceFile>) map.get("specialVideos");
            if (null != resourceFileModelList && resourceFileModelList.size() > 0) {
                List<Map<String, String>> list = new ArrayList<>();
                for (ResourceFile resourceFileModel : resourceFileModelList) {
                    Map<String, String> smap = new HashMap<>();
                    smap.put("id", resourceFileModel.getId());
                    smap.put("status", "0");
                    smap.put("resourceName", resourceFileModel.getResourceName());
                    list.add(smap);
                }
                map.put("specialVideos", list);
            }
        }
        restResultModel.setResult(200);
        restResultModel.setData(map);
        return restResultModel;
    }
}


