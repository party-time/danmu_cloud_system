package cn.partytime.service.shop;

import cn.partytime.repository.manager.shop.ItemAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2017/6/26.
 */

@Service
@Slf4j
public class ItemAddressService {

    @Autowired
    private ItemAddressRepository itemAddressRepository;



}
