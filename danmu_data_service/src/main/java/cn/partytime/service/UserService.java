package cn.partytime.service;

import cn.partytime.model.user.User;
import cn.partytime.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.insert(user);
    }

    public void deleteById(String id){
        userRepository.delete(id);
    }

    public User updateById(User user){
        return userRepository.save(user);
    }

    public User findById(String id){
        return userRepository.findById(id);
    }
}
