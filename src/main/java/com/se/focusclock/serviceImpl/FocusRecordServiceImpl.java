package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.FocusRecordRepository;
import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.FocusRecord;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.FocusRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class FocusRecordServiceImpl implements FocusRecordService {
    @Autowired
    FocusRecordRepository focusRecordRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean addFocusRecord(int userid, Time start, Time end, int duration, Date date) {
        FocusRecord focusRecord = new FocusRecord();
        focusRecord.setDate(date);
        focusRecord.setStart(start);
        focusRecord.setEnd(end);
        focusRecord.setDuration(duration);
        focusRecord.setUserid(userid);
        focusRecord.setType(false);

        focusRecordRepository.save(focusRecord);

        User user = userRepository.findById(userid);
        int increment = 0;
        if(duration <30){
            increment = 1;
        }
        else if(duration >= 30 && duration < 60){
            increment = 2;
        }
        else if(duration >=60){
            increment = 3;
        }
        user.setCredit(user.getCredit()+increment);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<Integer> getRecent7Days(int userid) {
        List<Integer> durations = new ArrayList<>();

        long time = System.currentTimeMillis();
        java.sql.Date now = new java.sql.Date(time);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        for (int i = 0; i < 7; i++) {
            calendar.add(calendar.DATE, -1);
            java.util.Date utilDate = (java.util.Date) calendar.getTime();
            java.sql.Date before = new java.sql.Date(utilDate.getTime());
            Integer duration = focusRecordRepository.findByDate(userid,before);
            if(duration == null) duration = 0;
            durations.add(duration);
        }
        return durations;
    }

    @Override
    public List<Integer> getRecent6Months(int userid) {
        List<Integer> durations = new ArrayList<>();
        long time = System.currentTimeMillis();
        java.sql.Date now = new java.sql.Date(time);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        for (int i = 0; i < 6; i++) {
            calendar.add(calendar.MONTH, -1);
            final int last = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, last);
            java.util.Date date2 = (java.util.Date) calendar.getTime();
            final int first = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, first);
            java.util.Date date1 = (java.util.Date) calendar.getTime();
            Integer duration = focusRecordRepository.findBetweenDate1AndDate2(userid,new java.sql.Date(date1.getTime()), new java.sql.Date(date2.getTime()));
            if(duration == null) duration = 0;
            durations.add(duration);
        }
        return durations;
    }

    @Override
    public List<FocusRecord> getOneDay(int userid, Date date) {
        return focusRecordRepository.findFocusRecordsByUseridAndDate(userid,date);
    }
}
