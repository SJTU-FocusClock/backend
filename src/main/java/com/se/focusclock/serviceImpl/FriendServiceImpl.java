package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.FriendRepository;
import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.Friend;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getFriendList(int userid) {
        List<Friend> friendList  = friendRepository.findByMeOrderByOtherAsc(userid);
        List<User> result = new ArrayList<>();
        for (int i = 0; i <friendList.size() ; i++) {
            result.add(userRepository.findById(friendList.get(i).getOther()));
        }
        return result;
    }

    @Override
    public User searchByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public boolean removeFriend(int me, int other) {
        Friend friend1 = friendRepository.findByMeAndOther(me,other);
        Friend friend2 = friendRepository.findByMeAndOther(other,me);
        if(friend1 == null || friend2 == null){
            return false;
        }
        friendRepository.deleteById(friend1.getId());
        friendRepository.deleteById(friend2.getId());
        return true;
    }
}
