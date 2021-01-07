package com.se.focusclock.service;

import com.se.focusclock.Repository.ClockRecordRepository;
import com.se.focusclock.entity.ClockRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClockRecordServiceImplTest {
    @Autowired
    ClockRecordService clockRecordService;

    @MockBean
    ClockRecordRepository clockRecordRepository;

    @Test
    void createClockRecord() {
        String start = "09:00:00";
        String end = "10:00:00";
        long time = System.currentTimeMillis();
        Date date = new Date(time);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(start);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time startTime = new Time(d.getTime());
        try {
            d = format.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time endTime = new Time(d.getTime());
        int userId = 2;

        ClockRecord clockRecord = new ClockRecord();
        clockRecord.setId(1);
        clockRecord.setDate(date);
        clockRecord.setUserid(userId);
        clockRecord.setStart(startTime);
        clockRecord.setEnd(endTime);

        when(clockRecordRepository.save(clockRecord)).thenReturn(clockRecord);
        assertTrue(clockRecordService.createClockRecord(userId, startTime, endTime, date));
    }

    @Test
    void getByDate() {
        int userId = 2;
        long time = System.currentTimeMillis();
        Date date = new Date(time);

        List<ClockRecord> clockRecords = new ArrayList<>();

        when(clockRecordRepository.findByUseridAndDate(userId,date)).thenReturn(clockRecords);
        assertEquals(clockRecords, clockRecordService.getByDate(userId, date));
    }
}