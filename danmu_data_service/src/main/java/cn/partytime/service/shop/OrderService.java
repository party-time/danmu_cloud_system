package cn.partytime.service.shop;

import cn.partytime.model.shop.Order;
import cn.partytime.repository.manager.shop.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * Created by administrator on 2017/7/7.
 */
@Slf4j
@Service
public class OrderService {

    public static final String numberChar = "0123456789";

    @Autowired
    private OrderRepository orderRepository;


    private String getCode(){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i = 0; i < 6; i++) {
            sb.append( numberChar.charAt( random.nextInt( numberChar.length() ) ) );
        }
        return sb.toString();
    }

    /**
     * 比较差劲的写法，以后在优化
     * @param order
     * @return
     */
    public Order save(Order order){
        String code = getCode();
        Order temp = this.findByCodeAndGetItemTime(code,null);
        while( null != temp){
            code = getCode();
            temp = this.findByCodeAndGetItemTime(code,null);
        }
        order.setCode(code);
        return orderRepository.insert(order);
    }

    public Order update(Order order){
        return orderRepository.save(order);
    }

    public Order findById(String id){
        return orderRepository.findOne(id);
    }

    public Page<Order> findAll(Integer pageNum,Integer pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return orderRepository.findAll(pageRequest);
    }

    public Order getItem(String id,String adminId){
        Order order = orderRepository.findOne(id);
        if( null != order){
            order.setGetItemTime(new Date());
            order.setAdminId(adminId);
            orderRepository.save(order);
        }
        return order;
    }

    public Order findByCode(String code){
        return orderRepository.findByCode(code);
    }

    public Order findByCodeAndGetItemTime(String code,Date getItemTime){
        return orderRepository.findByCodeAndGetItemTime(code,getItemTime);
    }

    /**
     * 管理员给用户发货
     * @param code
     * @param adminId
     */
    public void updateGetItem(String code,String adminId){
        Order order = this.findByCodeAndGetItemTime(code,null);
        if( null != order){
            log.info("orderId:"+order.getId());
            order.setGetItemTime(new Date());
            order.setAdminId(adminId);
            orderRepository.save(order);
        }
    }

    public void updateStatus(String id,Integer status){
        Order order = this.findById(id);
        if( null != order){
            order.setStatus(status);
            orderRepository.save(order);
        }
    }




}
