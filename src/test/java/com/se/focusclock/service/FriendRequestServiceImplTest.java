package com.se.focusclock.service;

import com.se.focusclock.Repository.FriendRepository;
import com.se.focusclock.Repository.FriendRequestRepository;
import com.se.focusclock.entity.Friend;
import com.se.focusclock.entity.FriendRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FriendRequestServiceImplTest {

    @Autowired
    FriendRequestService friendRequestService;

    @MockBean
    FriendRequestRepository friendRequestRepository;

    @MockBean
    FriendRepository friendRepository;

    @Test
    void sendFriendRequest() {
        when(friendRepository.findByMeAndOther(1, 2)).thenReturn(new Friend());
        when(friendRepository.findByMeAndOther(2, 1)).thenReturn(new Friend());
        assertEquals(false, friendRequestService.sendFriendRequest(1, 2));

        when(friendRequestRepository.findBySenderOrReceiverAndType(1, 2, 0)).thenReturn(new FriendRequest());
        assertEquals(false, friendRequestService.sendFriendRequest(1, 2));


        when(friendRepository.findByMeAndOther(1, 2)).thenReturn(null);
        when(friendRepository.findByMeAndOther(2, 1)).thenReturn(null);
        when(friendRequestRepository.findBySenderOrReceiverAndType(1, 2, 0)).thenReturn(null);

        assertEquals(true, friendRequestService.sendFriendRequest(1, 2));

    }

    @Test
    void getFriendRequestList() {
        FriendRequest friendRequest1 = new FriendRequest();
        FriendRequest friendRequest2 = new FriendRequest();
        FriendRequest friendRequest3 = new FriendRequest();
        FriendRequest friendRequest4 = new FriendRequest();
        FriendRequest friendRequest5 = new FriendRequest();
        List<FriendRequest> friendRequestList = new ArrayList<>();
        friendRequestList.add(friendRequest1);
        friendRequestList.add(friendRequest2);
        friendRequestList.add(friendRequest3);
        friendRequestList.add(friendRequest4);
        friendRequestList.add(friendRequest5);

        when(friendRequestRepository.findFriendRequestsByReceiverAndTypeOrderByRequesttimeDesc(1,0)).thenReturn(friendRequestList);

        assertEquals(friendRequestList,friendRequestService.getFriendRequestList(1));
    }

    @Test
    void handleFriendRequst() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(1);
        friendRequest.setReceiver(2);
        friendRequest.setType(0);
        Optional<FriendRequest> friendRequestOptional =Optional.of(friendRequest);

        when(friendRequestRepository.findById(1)).thenReturn(friendRequestOptional);

        assertEquals(true,friendRequestService.handleFriendRequst(1,1));

        when(friendRequestRepository.findById(1)).thenReturn(null);
        assertEquals(false,friendRequestService.handleFriendRequst(1,1));

    }
}