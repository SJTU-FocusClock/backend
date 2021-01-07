package com.se.focusclock.controller;


import com.se.focusclock.entity.FriendRequest;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.FriendRequestService;
import com.se.focusclock.service.FriendService;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.LoginUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/friends")
@CrossOrigin(allowCredentials = "true")
public class FriendController {
    @Resource
    FriendService friendService;

    @Resource
    UserService userService;

    @Resource
    FriendRequestService friendRequestService;

    LoginUtils loginUtils = new LoginUtils();

    @GetMapping("/list")
    List<User> getFriendList(){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        return friendService.getFriendList(user.getId());
    }

    @GetMapping("/search")
    User searchUserByPhone(@RequestParam String phone){
        return friendService.searchByPhone(phone);
    }


    @DeleteMapping("/remove/{friendId}")
    boolean deleteFriend(@PathVariable int friendId){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        if(friendId <= 0) return false;

        return friendService.removeFriend(user.getId(),friendId);
    }

    @PostMapping("/sendRequest/{receiverId}")
    boolean sendRequest(@PathVariable int receiverId){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        if(receiverId <= 0) return false;

        return friendRequestService.sendFriendRequest(user.getId(),receiverId);
    }


    @GetMapping("/getRequestList")
    List<FriendRequest> getRequestList(){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        return friendRequestService.getFriendRequestList(user.getId());
    }

    @PostMapping("/handle")
    boolean handleRequest(@RequestParam int requestId,@RequestParam int type){
        return friendRequestService
                .handleFriendRequst(requestId,type);
    }
}
