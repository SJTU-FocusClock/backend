package com.se.focusclock.service;

import com.se.focusclock.entity.FriendRequest;

import java.util.List;

public interface FriendRequestService {
    boolean sendFriendRequest(int sender, int receiver);
    List<FriendRequest> getFriendRequestList(int userid);
    boolean handleFriendRequst(int id, int type);
}
