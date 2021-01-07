package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.ClockRecordRepository;
import com.se.focusclock.Repository.FocusRecordRepository;
import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.ClockRecord;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.ClockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Date;
import java.util.List;


@Service
public class ClockRecordServiceImpl implements ClockRecordService {
    @Autowired
    ClockRecordRepository clockRecordRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean createClockRecord(int userId, Time start, Time end, Date date) {
        ClockRecord clockRecord  =new ClockRecord();
        clockRecord.setDate(date);
        clockRecord.setUserid(userId);
        clockRecord.setStart(start);
        clockRecord.setEnd(end);

        clockRecordRepository.save(clockRecord);


        User user = userRepository.findById(userId);
        user.setCredit(user.getCredit() + 1);
        userRepository.save(user);


        return true;
    }

    @Override
    public List<ClockRecord> getByDate(int userId, Date date) {

        return clockRecordRepository.findByUseridAndDate(userId,date);
    }
}
