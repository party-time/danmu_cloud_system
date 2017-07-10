package cn.partytime.service.cms;

import cn.partytime.model.cms.Column;
import cn.partytime.repository.manager.cms.ColumnRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/6/29.
 */
@Service
@Slf4j
public class ColumnService {

    @Autowired
    private ColumnRepository columnRepository;

    public Column save(Column column){
        return columnRepository.insert(column);
    }

    public Column update(Column column){
        return columnRepository.save(column);
    }

    public Column findById(String id){
        return columnRepository.findOne(id);
    }

    public List<Column> findByIds(List<String> idList){
        return columnRepository.findByIdIn(idList);
    }

    public void delById(String id){
        columnRepository.delete(id);
    }

    public List<Column> findByAddressId(String addressId){
        return columnRepository.findByAddressId(addressId);
    }

}
