package com.se.focusclock.service;

import com.se.focusclock.entity.ClockRecord;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

public interface ClockRecordService {
    boolean createClockRecord(int userId,Time start, Time end, Date date);
    List<ClockRecord> getByDate(int userId, Date date);
}
