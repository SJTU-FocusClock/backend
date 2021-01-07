package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.FriendRepository;
import com.se.focusclock.Repository.FriendRequestRepository;
import com.se.focusclock.entity.Friend;
import com.se.focusclock.entity.FriendRequest;
import com.se.focusclock.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {
    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendRepository friendRepository;

    @Override
    //当好友列表里已经有这个人，或者是对方请求列表里已有未处理的你的请求，则不能再发好友请求
    public boolean sendFriendRequest(int sender, int receiver) {
        if(friendRequestRepository.findBySenderOrReceiverAndType(sender,receiver, 0)!=null){
            return false;
        };
        if(friendRepository.findByMeAndOther(sender,receiver) != null || friendRepository.findByMeAndOther(receiver,sender) !=null){
            return false;
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setType(0);
        friendRequest.setRequesttime(new java.sql.Timestamp(System.currentTimeMillis()));
        friendRequestRepository.save(friendRequest);

        return true;
    }

    @Override
    public List<FriendRequest> getFriendRequestList(int userid) {
        return friendRequestRepository.findFriendRequestsByReceiverAndTypeOrderByRequesttimeDesc(userid, 0);
    }

    @Override
    public boolean handleFriendRequst(int id, int type) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(id);
        if(friendRequestOptional == null || friendRequestOptional.get().getType()!=0) {
            return false;
        }
        FriendRequest friendRequest = friendRequestOptional.get();
        friendRequest.setType(type);
        friendRequestRepository.save(friendRequest);

        if(friendRequest.getType() == 1 ){
            Friend friend1 = new Friend();
            friend1.setMe(friendRequest.getReceiver());
            friend1.setOther(friendRequest.getSender());
            friendRepository.save(friend1);
            Friend friend2 = new Friend();
            friend2.setOther(friendRequest.getReceiver());
            friend2.setMe(friendRequest.getSender());
            friendRepository.save(friend2);
        }
        return true;
    }
}
