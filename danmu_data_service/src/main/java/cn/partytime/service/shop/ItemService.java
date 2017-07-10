package cn.partytime.service.shop;

import cn.partytime.model.shop.Item;
import cn.partytime.repository.manager.shop.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/6/26.
 */
@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item save(Item item){
        return itemRepository.insert(item);
    }

    public void update(Item item){
        itemRepository.save(item);
    }

    public Page<Item> findAll(Integer pageNum, Integer pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return itemRepository.findAll(pageRequest);
    }

    public Item findById(String id){
        return itemRepository.findOne(id);
    }

    public void delById(String id){
        itemRepository.delete(id);
    }

    public List<Item> findByIds(List<String> idList){

        return itemRepository.findByIdIn(idList);
    }





}
