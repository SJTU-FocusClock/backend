package com.se.focusclock.service;

import com.se.focusclock.Repository.FriendRepository;
import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.Friend;
import com.se.focusclock.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@SpringBootTest
class FriendServiceImplTest {

    @Autowired
    FriendService friendService;

    @MockBean
    FriendRepository friendRepository;
    @MockBean
    UserRepository userRepository;


    @Test
    void getFriendList() {
        Friend friend1 = new Friend(1,3,1);
        Friend friend2 = new Friend(2,3,2);
        User user1 = new User(1,"user1","user1","user1","xxxx","xxxx",1,1,false);
        User user2 = new User(2,"user2","user2","user2","xxxx","xxxx",1,1,false);
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friend1);
        friendList.add(friend2);
        when(friendRepository.findByMeOrderByOtherAsc(3)).thenReturn(friendList);
        when(userRepository.findById(1)).thenReturn(user1);
        when(userRepository.findById(2)).thenReturn(user2);
        List<User> result = new ArrayList<>();
        result.add(user1);
        result.add(user2);

        assertEquals(result,friendService.getFriendList(3));
    }

    @Test
    void searchByPhone() {
        User user = new User(1,"user1","user1","user1","xxxx","123456789",1,1,false);
        when(userRepository.findByPhone("123456789")).thenReturn(user);
        assertEquals(user,friendService.searchByPhone("123456789"));
    }

    @Test
    void removeFriend() {
        Friend friend1 = new Friend(1,1,2);
        Friend friend2 = new Friend(2,2,1);
        when(friendRepository.findByMeAndOther(1,2)).thenReturn(friend1);
        when(friendRepository.findByMeAndOther(2,1)).thenReturn(friend2);
        assertEquals(true,friendService.removeFriend(1,2));


        friend1 = null;
        friend2 = null;
        when(friendRepository.findByMeAndOther(1,2)).thenReturn(friend1);
        when(friendRepository.findByMeAndOther(2,1)).thenReturn(friend2);
        assertEquals(false,friendService.removeFriend(1,2));

    }
}