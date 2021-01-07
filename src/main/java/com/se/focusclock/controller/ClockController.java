package com.se.focusclock.controller;

import com.alibaba.fastjson.JSONObject;
import com.se.focusclock.entity.*;
import com.se.focusclock.service.ClockRecordService;
import com.se.focusclock.service.ClockRequestService;
import com.se.focusclock.service.ClockService;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.LoginUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clocks")
@CrossOrigin(allowCredentials = "true")
public class ClockController {
    @Resource
    ClockService clockService;
    @Resource
    ClockRequestService clockRequestService;
    @Resource
    UserService userService;
    @Resource
    ClockRecordService clockRecordService;

    LoginUtils loginUtils = new LoginUtils();


    @PostMapping("/create")
    Clock CreateClock(@RequestBody JSONObject clock){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        Boolean gameType = clock.getBoolean("gameType");
        int gameId = clock.getInteger("gameId");
        String time =clock.getString("time");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = clock.getInteger("ring");
        String tag = clock.getString("tag");
        List<Integer> week = (ArrayList)clock.get("week");

        return clockService.createClock(gameType,gameId,user.getId(),user.getId(),_time,week,ring,tag);
    }

    @PutMapping("/modify/{clockId}")
    Boolean modifyClock(@PathVariable int clockId,@RequestBody JSONObject newClock){
        Boolean gameType = newClock.getBoolean("gameType");
        int gameId = newClock.getInteger("gameId");
        String time =newClock.getString("time");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        System.out.println(time);
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = newClock.getInteger("ring");
        String tag = newClock.getString("tag");
        List<Integer> week = (ArrayList)newClock.get("week");
        System.out.println(_time);
        return clockService.modifyClock(clockId,gameType,gameId,_time,week,ring,tag);
    }

    @DeleteMapping("/delete/{clockId}")
    Boolean deleteClock(@PathVariable int clockId){
        return clockService.deleteClock(clockId);
    }

    @PutMapping("/setStatus")
    Boolean setStatus(@RequestParam int clockId,@RequestParam boolean status){
        return clockService.changeStatus(clockId,status);
    }

    @GetMapping("/clockList")
    List<Clock> getClockList(){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        return clockService.getClockList(user.getId());
    }

    @GetMapping("/clock/{clockId}")
    Clock getClockById(@PathVariable int clockId){
        return clockService.getClockById(clockId);
    }

    @GetMapping("/requestList")
    List<ClockRequest> getClockRequestList(){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        return clockRequestService.getClockRequestList(user.getId());
    }

    @GetMapping("/getRequest/{requestId}")
    ClockRequest getClockRequestById(@PathVariable int requestId){
        return clockRequestService.getClockRequestById(requestId);
    }

    @PostMapping("/sendRequest")
    boolean SendClockRequest(@RequestBody JSONObject clock){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        Boolean gameType = clock.getBoolean("gameType");
        int gameId = clock.getInteger("gameId");
        String time =clock.getString("time");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = clock.getInteger("ring");
        String tag = clock.getString("tag");
        List<Integer> week = (ArrayList)clock.get("week");
        int receiver = clock.getInteger("receiver");

        return clockRequestService.createClockRequest(user.getId(),receiver,gameType,gameId,_time,week,ring,tag);
    }

    @PostMapping("/handle")
    boolean handleRequest(@RequestParam int requestId,@RequestParam int type){
            return clockRequestService.handleClockRequest(requestId, type);
    }


    @PostMapping("/createRecord")
    boolean createClockRecord(@RequestParam String start,@RequestParam String end){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);

        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(start);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time startTime = new java.sql.Time(d.getTime());
        try {
            d = format.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time endTime = new java.sql.Time(d.getTime());
        return clockRecordService.createClockRecord(user.getId(),startTime,endTime,date);
    }


    @GetMapping("/getRecord")
    List<ClockRecord> getClockRecord(@RequestParam  String date){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date _date = new java.sql.Date(d.getTime());

        return clockRecordService.getByDate(user.getId(),_date);
    }


}

