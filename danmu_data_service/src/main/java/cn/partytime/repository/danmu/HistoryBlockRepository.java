package cn.partytime.repository.danmu;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by lENOVO on 2016/10/31.
 */

@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public class HistoryBlockRepository {
}
