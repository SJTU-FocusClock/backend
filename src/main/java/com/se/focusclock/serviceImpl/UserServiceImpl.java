package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.CheckUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import javax.jws.soap.SOAPBinding;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public User save(User user){
        if (user.getPhone() == null) {
            return  null;
        }

        if (user.getUsername() == null) {
            user.setUsername("abc" + user.getPhone());
        }
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        user = userRepository.save(user);
        return user;
    }

    @Override
    public User load(int userid, String username, String phone) {
        User user = null;
        if (!CheckUtils.isEmpty(userid)) {
            user = userRepository.findById(userid);
        }
        if (user == null && !CheckUtils.isEmpty(username)) {
            user = userRepository.findByUsername(username);
        }
        if (user == null && !CheckUtils.isEmpty(phone)) {
            user = userRepository.findByPhone(phone);
        }
        return user;
    }


    @Override
    public User setInfo(int id, String nickname, boolean sex) {
        User user = userRepository.findById(id);
        if(user == null) return null;
        else{
            user.setNickname(nickname);
            user.setSex(sex);
            return userRepository.save(user);
        }
    }

    @Override
    public User setEmail(int id, String email) {
        User user = userRepository.findById(id);
        if(user == null) return null;
        else{
            user.setEmail(email);
            return userRepository.save(user);
        }
    }
}
