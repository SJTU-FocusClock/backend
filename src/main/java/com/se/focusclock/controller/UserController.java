package com.se.focusclock.controller;

import com.alibaba.fastjson.JSONObject;
import com.se.focusclock.entity.User;
import com.se.focusclock.exception.FailureResponse;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.LoginUtils;
import com.se.focusclock.utils.ValidateUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Password;

import javax.annotation.Resource;
import java.security.Principal;

@RestController
@RequestMapping("/users")
@CrossOrigin(allowCredentials = "true")
public class UserController {
    @Resource
    UserService userService;

    LoginUtils loginUtils = new LoginUtils();

    @GetMapping("/user")
    public Object currentUser(Principal principal){
        if (principal == null) {
            return new FailureResponse("Not logged in",0);
        }
        String username = principal.getName();
        return userService.load(-1, username, null);
    }

    @GetMapping("/Ulogin")
    public String Ulogin() {
        return "Please Login in";
    }

    @GetMapping("/Plogin")
    public String Plogin() {
        return "Please Login in";
    }

    @PostMapping("/register")
    public Object register(@RequestParam("phone")String phone, @RequestParam("password")String password){
        if (!ValidateUtils.checkMoblie(phone)) {
            return new FailureResponse("手机号格式错误",0);
        }
        if (userService.load(-1, null, phone) != null) {
            return new FailureResponse("手机号已存在",0);
        }
        if (!ValidateUtils.checkPass(password)) {
            return new FailureResponse("密码格式错误",0);
        }
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        userService.save(user);
        return user;
    }

    @PutMapping("/alterInfo")
    User alterInfo(@RequestBody JSONObject newInfo){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

       String nickname = newInfo.getString("nickname");
       boolean sex = newInfo.getBoolean("sex");

       return userService.setInfo(user.getId(),nickname,sex);
    }

    @GetMapping("/getUserInfo/{userId}")
    User getUserInfo(@PathVariable int userId){
        return userService.load(userId,null,null);
    }

    @GetMapping("/getUserCredit")
    int getUserCredit() {
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        return userService.load(user.getId(),null,null).getCredit();
    }

    @PutMapping("/setPassword")
    User setPassword(@RequestParam String password){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        user.setPassword(password);
        return userService.save(user);
    }

    @PutMapping("/setEmail")
    User setEmail(@RequestParam String email){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        return userService.setEmail(user.getId(),email);
    }
}
