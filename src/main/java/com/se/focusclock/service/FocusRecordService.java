package com.se.focusclock.service;

import com.se.focusclock.entity.FocusRecord;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface FocusRecordService {
    boolean addFocusRecord(int userid, Time start, Time end, int duration, Date date);
    List<Integer> getRecent7Days(int userid);
    List<Integer> getRecent6Months(int userid);
    List<FocusRecord> getOneDay(int userid,Date date);
}
