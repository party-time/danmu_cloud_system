package cn.partytime.service.danmu;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.PartyResult;
import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.model.manager.Party;
import cn.partytime.service.DanmuLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/6.
 */

@Slf4j
@Service
public class BmsPreDanmuLibraryService {


    @Autowired
    private DanmuLibraryService danmuLibraryService;

    public PageResultModel findAllByPage(Integer pageNo, Integer pageSize , String name){
        Page<DanmuLibrary> danmuLibraryPage = null;
        danmuLibraryPage = danmuLibraryService.findByName(name,pageNo,pageSize);
        PageResultModel pageResultModel = new PageResultModel();

        if( null != danmuLibraryPage && null != danmuLibraryPage.getContent()){
            List<DanmuLibrary> danmuLibraryList = danmuLibraryPage.getContent();
            pageResultModel.setRows(danmuLibraryList);
            pageResultModel.setTotal(danmuLibraryPage.getTotalElements());
        }
        return pageResultModel;
    }

}
