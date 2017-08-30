package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.Danmu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface DanmuRepository extends MongoRepository<Danmu, String> {

    public Danmu findById(String id);

    public List<Danmu> findByDanmuPoolId(String danmuPoolId);

    public List<Danmu> findByIsBlockedAndViewFlgAndDanmuSrcAndDanmuPoolIdInOrderByTimeDesc(boolean isBlock,boolean viewFlg,int danmuSc,List<String>poolIds);

    public Page<Danmu> findByIsBlocked(boolean isBlocked, Pageable pageable);

    /**
     * 通过弹幕池编号 与弹幕来源获取弹幕列表
     * @param danmuPoolId
     * @param danmuSrc
     * @param pageable
     * @return
     */
    public Page<Danmu> findByDanmuPoolIdAndDanmuSrcAndIsBlocked(String danmuPoolId, int danmuSrc, boolean isBlocked, Pageable pageable);

    /**
     * 通过弹幕池编号 与弹幕来源获取弹幕列表
     * @param danmuPoolId
     * @param danmuSrc
     * @param pageable
     * @return
     */
    public Page<Danmu> findByDanmuPoolIdAndDanmuSrc(String danmuPoolId, int danmuSrc, Pageable pageable);


    public Page<Danmu> findByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(String danmuPoolId, int danmuSrc, boolean isBlocked, boolean viewFlg, Pageable pageable);

    public Page<Danmu> findByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(List<String> danmuPoolIdList, int danmuSrc, boolean isBlocked, boolean viewFlg, Pageable pageable);

    public long countByDanmuPoolIdAndDanmuSrcAndIsBlockedAndViewFlg(String danmuPoolId,int danmuSrc,boolean isBlocked,boolean viewFlg);

    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlg(List<String> danmuPoolIdList,int danmuSrc,boolean isBlocked,boolean viewFlg);


    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTimeGreaterThan(List<String> danmuPoolIdList,int danmuSrc,boolean isBlocked,boolean viewFlg,long time);

    public List<Danmu> findByIdIn(List<String> danmuIdList);

}
