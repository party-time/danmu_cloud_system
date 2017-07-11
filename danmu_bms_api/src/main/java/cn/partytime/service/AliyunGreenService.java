package cn.partytime.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20160621.TextKeywordFilterRequest;
import com.aliyuncs.green.model.v20160621.TextKeywordFilterResponse;
import com.aliyuncs.green.model.v20160621.TextKeywordFilterResponse.KeywordResult;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuwei on 16/7/15.
 */
@Service
@Slf4j
public class AliyunGreenService {

    private static String ACCESS_KEY_ID = "BPzuMLVmr0L5wq0y";

    private static String ACCESS_KEY_SECRET = "PFHChXc46VfpbIN7APxnlfjOvL9uw5";

    public boolean blockTextKeyword(String text){
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        TextKeywordFilterRequest textKeywordFilterRequest = new TextKeywordFilterRequest();
        textKeywordFilterRequest.setText(text);

        try {
            TextKeywordFilterResponse textKeywordFilterResponse = client.getAcsResponse(textKeywordFilterRequest);
            List<KeywordResult> keywordResultList = textKeywordFilterResponse.getKeywordResults();
            if( null != keywordResultList && keywordResultList.size() > 0){
                for( KeywordResult keywordResult: keywordResultList){
                    String keywordCtx = keywordResult.getKeywordCtx();
                    String keywordType = keywordResult.getKeywordType();
                    //log.debug("keywordCtx:"+keywordCtx+",keywordType:"+keywordType);
                }
                return true;
            }else{
                return false;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

}
