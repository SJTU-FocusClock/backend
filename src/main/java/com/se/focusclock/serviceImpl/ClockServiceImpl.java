package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.ClockRepository;
import com.se.focusclock.entity.Clock;
import com.se.focusclock.service.ClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class ClockServiceImpl implements ClockService {
    @Autowired
    ClockRepository clockRepository;


    @Override
    public Clock createClock(boolean gameType, int gameId, int setter,int owner, Time time, List<Integer> week, int ring, String tag) {

        if (clockRepository.findClockByOwnerAndTime(owner,time) != null){
            return null;
        }
        Clock clock = new Clock();
        clock.setGameid(gameId);
        clock.setGametype(gameType);
        clock.setTime(time);
        clock.setOwner(owner);
        clock.setSetter(setter);
        clock.setRing(ring);
        clock.setTag(tag);
        clock.setStatus(true);
        byte weekbit = 0;
        for(int i = 0;i<week.size();i++){
            byte mask = 1;
            mask <<= week.get(i)-1;
            weekbit |= mask;
        }
        clock.setWeek(weekbit);
        return clockRepository.save(clock);
    }

    @Override
    public boolean modifyClock(int id,boolean gameType, int gameId,Time time, List<Integer> week, int ring, String tag) {
        Optional<Clock> clockOptional = clockRepository.findById(id);
        if(clockOptional == null){
            return false;
        }
        Clock clock = clockOptional.get();
        clock.setGameid(gameId);
        clock.setGametype(gameType);
        clock.setTime(time);
        clock.setRing(ring);
        clock.setTag(tag);
        byte weekbit = 0;
        for(int i = 0;i<week.size();i++){
            byte mask = 1;
            mask <<= week.get(i)-1;
            weekbit |= mask;
        }
        clock.setWeek(weekbit);
        clockRepository.save(clock);
        return true;
    }

    @Override
    public boolean changeStatus(int id, boolean status) {
        Optional<Clock> clockOptional = clockRepository.findById(id);
        if(clockOptional == null){
            return false;
        }
        Clock clock = clockOptional.get();
        clock.setStatus(status);
        clockRepository.save(clock);
        return true;
    }

    @Override
    public boolean deleteClock(int id) {
        Optional<Clock> clockOptional = clockRepository.findById(id);
        if(clockOptional == null){
            return false;
        }
        clockRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Clock> getClockList(int userid) {
        return clockRepository.findClocksByOwner(userid);
    }

    @Override
    public Clock getClockById(int id) {
        return clockRepository.findById(id).get();
    }
}
