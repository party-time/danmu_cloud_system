package cn.partytime.service.client;

import cn.partytime.model.manager.RegistCode;
import cn.partytime.repository.manager.RegistCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by liuwei on 2016/9/7.
 */
@Service
@Slf4j
public class RegistCodeService {

    @Autowired
    private RegistCodeRepository registCodeRepository;

    private String[] letter = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private String[] number = {"0","1","2","3","4","5","6","7","8","9"};

    private String getCode(String codeId){
        Random r = new Random(codeId.hashCode());
        int maxLetter = r.nextInt(20);
        List<String> temp = new ArrayList<String>();
        for( int i=maxLetter;i<maxLetter+6;i++){
            temp.add(letter[i]);
        }
        int maxNum = r.nextInt(5);
        for( int i=maxNum;i<maxNum+6;i++){
            temp.add(number[i]);
        }
        Collections.shuffle(temp);
        String c = String.valueOf(Math.abs(codeId.hashCode()));
        int subC = r.nextInt(4);
        c = c.substring(subC,subC+6);
        String code = "";
        for( int i=0;i<c.length();i++){
            Integer d = Integer.valueOf(c.substring(i,i+1));
            code += temp.get(d);
        }
        return code;
    }

    public RegistCode save(Date overdue) {
        RegistCode registCode = new RegistCode();
        if (null == overdue) {
            registCode.setType(1);
        } else {
            registCode.setType(0);
        }
        registCode.setOverdue(overdue);
        registCode = registCodeRepository.insert(registCode);
        String rc = "";
        RegistCode r = null;
        while( null == r){
            rc = this.getCode(registCode.getId());
            r = registCodeRepository.findByRegistCode(rc);
            if( r == null){
                r = registCode;
            }
        }
        registCode.setRegistCode(rc);
        return registCodeRepository.save(registCode);
    }

    /**
     * 按照registCode查找
     * @param registCode
     * @return
     */
    public RegistCode findByRegistCode(String registCode){
        if (StringUtils.isEmpty(registCode)) {
            return null;
        }
        return registCodeRepository.findByRegistCode(registCode);
    }

    public void deleteById(String id){
        registCodeRepository.delete(id);
    }



}
