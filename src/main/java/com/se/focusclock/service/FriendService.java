package com.se.focusclock.service;

import com.se.focusclock.entity.User;

import java.util.List;

public interface FriendService {
    List<User> getFriendList(int userid);
    User searchByPhone(String phone);
    boolean removeFriend(int me, int other);
}
