package com.se.focusclock.service;

import com.se.focusclock.entity.User;


public interface UserService {

    public User save(User user);

    public User load(int userid, String username, String phone);

    User setInfo(int id,String nickname,boolean sex);

    User setEmail(int id,String email);
}
