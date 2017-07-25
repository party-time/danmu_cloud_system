package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.DanmuModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface DanmuRepository extends MongoRepository<DanmuModel, String> {

    public DanmuModel findById(String id);

    public List<DanmuModel> findByDanmuPoolId(String danmuPoolId);



    public Page<DanmuModel> findByIsBlocked(boolean isBlocked, Pageable pageable);

    /**
     * 通过弹幕池编号 与弹幕来源获取弹幕列表
     * @param danmuPoolId
     * @param danmuSrc
     * @param pageable
     * @return
     */
    public Page<DanmuModel> findByDanmuPoolIdAndDanmuSrcAndIsBlocked(String danmuPoolId,int danmuSrc,boolean isBlocked,Pageable pageable);

    /**
     * 通过弹幕池编号 与弹幕来源获取弹幕列表
     * @param danmuPoolId
     * @param danmuSrc
     * @param pageable
     * @return
     */
    public Page<DanmuModel> findByDanmuPoolIdAndDanmuSrc(String danmuPoolId,int danmuSrc,Pageable pageable);


    public Page<DanmuModel> findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(String danmuPoolId,int danmuSrc,boolean isBlocked,boolean viewFlg,Pageable pageable);

    public Page<DanmuModel> findByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(List<String> danmuPoolIdList,int danmuSrc,boolean isBlocked,boolean viewFlg,Pageable pageable);

    public long countByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(String danmuPoolId,int danmuSrc,boolean isBlocked,boolean viewFlg);

    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(List<String> danmuPoolIdList,int danmuSrc,boolean isBlocked,boolean viewFlg);

}
