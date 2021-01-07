package com.se.focusclock.service;

import com.se.focusclock.Repository.FocusRecordRepository;
import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.FocusRecord;
import com.se.focusclock.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FocusRecordServiceImplTest {

    @MockBean
    FocusRecordRepository focusRecordRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    FocusRecordService focusRecordService;

    @Test
    void addFocusRecord() {

        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse("09:44:01");
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time startTime = new java.sql.Time(d.getTime());
        try {
            d = format.parse("09:50:01");
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time endTime = new java.sql.Time(d.getTime());

        User user = new User(1,"user1","user1","user1","xxxx","123456789",1,1,false);
        when(userRepository.findById(1)).thenReturn(user);
        assertEquals(true,focusRecordService.addFocusRecord(1,startTime,startTime,20,date));
        assertEquals(true,focusRecordService.addFocusRecord(1,startTime,startTime,40,date));
        assertEquals(true,focusRecordService.addFocusRecord(1,startTime,startTime,70,date));
    }

    @Test
    void getRecent7Days() {
        List<Integer> durations = new ArrayList<>();
        List<java.sql.Date> dates = new ArrayList<>();

        long time = System.currentTimeMillis();
        java.sql.Date now = new java.sql.Date(time);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        for (int i = 0; i < 7; i++) {
            calendar.add(calendar.DATE, -1);
            java.util.Date utilDate = (java.util.Date) calendar.getTime();
            java.sql.Date before = new java.sql.Date(utilDate.getTime());
            dates.add(before);
            durations.add(0);
        }
        when(focusRecordRepository.findByDate(1,dates.get(0))).thenReturn(0);
        when(focusRecordRepository.findByDate(1,dates.get(1))).thenReturn(0);
        when(focusRecordRepository.findByDate(1,dates.get(2))).thenReturn(0);
        when(focusRecordRepository.findByDate(1,dates.get(3))).thenReturn(0);
        when(focusRecordRepository.findByDate(1,dates.get(4))).thenReturn(0);
        when(focusRecordRepository.findByDate(1,dates.get(5))).thenReturn(0);
        when(focusRecordRepository.findByDate(1,dates.get(6))).thenReturn(0);


        assertEquals(durations,focusRecordService.getRecent7Days(1));
    }

    @Test
    void getRecent6Months() {

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
            when(focusRecordRepository.findBetweenDate1AndDate2(1,new java.sql.Date(date1.getTime()), new java.sql.Date(date2.getTime()))).thenReturn(0);
            durations.add(0);
        }

        assertEquals(durations,focusRecordService.getRecent6Months(1));
    }

    @Test
    void getOneDay() {
        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);
        List<FocusRecord> focusRecords = new ArrayList<>();
        when(focusRecordRepository.findFocusRecordsByUseridAndDate(1,date)).thenReturn(focusRecords);
        assertEquals(focusRecords,focusRecordService.getOneDay(1,date));
    }
}