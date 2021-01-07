package com.se.focusclock.controller;


import com.se.focusclock.entity.FocusRecord;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.FocusRecordService;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.LoginUtils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/focus")
@CrossOrigin(allowCredentials = "true")
public class FocusController {
    @Resource
    FocusRecordService focusRecordService;

    @Resource
    UserService userService;

    LoginUtils loginUtils = new LoginUtils();


    @PostMapping("/addRecord")
    boolean addFocusRecord(@RequestParam String start,@RequestParam String end,@RequestParam int duration) throws ParseException {
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        d = format.parse(start);
        java.sql.Time startTime = new java.sql.Time(d.getTime());
        d = format.parse(end);
        java.sql.Time endTime = new java.sql.Time(d.getTime());
        return focusRecordService.addFocusRecord(user.getId(),startTime,endTime,duration,date);
    }


    @GetMapping("/recent7Days")
    List<Integer> getRecent7Days(){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        return focusRecordService.getRecent7Days(user.getId());

    }

    @GetMapping("/recent6Months")
    List<Integer> getRecent6Months(){
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        return focusRecordService.getRecent6Months(user.getId());
    }

    @GetMapping("/oneDay")
    List<FocusRecord> geOneDay(@RequestParam String date) throws ParseException {
        User user = userService.load(-1,loginUtils.getLoginUser(),null);
        if(user == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        d = format.parse(date);
        java.sql.Date _date = new java.sql.Date(d.getTime());
        return focusRecordService.getOneDay(user.getId(),_date);
    }
}
