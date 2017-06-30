package cn.partytime.service.adDanmu;

import cn.partytime.common.util.DateUtils;
import cn.partytime.model.manager.AdTimerDanmuFile;
import cn.partytime.repository.manager.AdTimerDanmuFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */

@Service
public class AdTimerDanmuFileService {

    @Autowired
    private AdTimerDanmuFileRepository adTimerDanmuFileRepository;


    /**
     * 通过活动获取弹幕文件信息
     * @param poolIdList
     * @return
     */
    public List<AdTimerDanmuFile> findAdTimerDanmuFileByPoolIdList(List<String> poolIdList){
        return adTimerDanmuFileRepository.findByPoolIdIn(poolIdList);
    }

    public void insertAdTimerDanmuFile(String poolId,String path,String adminId){
        AdTimerDanmuFile adTimerDanmuFile = adTimerDanmuFileRepository.findByPoolId(poolId);
        Date date = DateUtils.getCurrentDate();
        if(adTimerDanmuFile!=null){
            adTimerDanmuFile.setPath(path);
            adTimerDanmuFile.setUpdateUserId(adminId);
            adTimerDanmuFile.setUpdateTime(date);
            adTimerDanmuFileRepository.save(adTimerDanmuFile);
        }else{
            adTimerDanmuFile = new AdTimerDanmuFile();
            adTimerDanmuFile.setPoolId(poolId);
            adTimerDanmuFile.setPath(path);
            adTimerDanmuFile.setUpdateUserId(adminId);
            adTimerDanmuFile.setUpdateTime(date);
            adTimerDanmuFile.setCreateUserId(adminId);
            adTimerDanmuFile.setCreateTime(date);
            adTimerDanmuFileRepository.insert(adTimerDanmuFile);
        }
    }

    public AdTimerDanmuFile findAdTimerDanmuFile(String poolId){
        return  adTimerDanmuFileRepository.findByPoolId(poolId);
    }

}
