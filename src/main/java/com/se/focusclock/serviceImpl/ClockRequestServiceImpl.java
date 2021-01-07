package com.se.focusclock.serviceImpl;

import com.se.focusclock.Repository.ClockRepository;
import com.se.focusclock.Repository.ClockRequestRepository;
import com.se.focusclock.entity.Clock;
import com.se.focusclock.entity.ClockRequest;
import com.se.focusclock.entity.FriendRequest;
import com.se.focusclock.service.ClockRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class ClockRequestServiceImpl implements ClockRequestService {
    @Autowired
    ClockRequestRepository clockRequestRepository;

    @Autowired
    ClockRepository clockRepository;

    @Override
    public List<ClockRequest> getClockRequestList(int userid) {
        return clockRequestRepository.findClockRequestsByReceiverAndTypeOrderByRequesttimeDesc(userid, 0);
    }

    @Override
    public ClockRequest getClockRequestById(int id) {
        Optional<ClockRequest> clockRequestOptional = clockRequestRepository.findById(id);
        if(clockRequestOptional == null)
            return null;
        else{
            return clockRequestOptional.get();
        }
    }

    @Override
    public boolean handleClockRequest(int id, int type) {
        Optional<ClockRequest> clockRequestOptional = clockRequestRepository.findById(id);
        if(clockRequestOptional == null || clockRequestOptional.get().getType()!= 0) {
            return false;
        }

        ClockRequest clockRequest = clockRequestOptional.get();
        clockRequest.setType(type);
        clockRequestRepository.save(clockRequest);

        if(type == 1){
            Clock clock = new Clock();
            clock.setGameid(clockRequest.getGameid());
            clock.setGametype(clockRequest.isGametype());
            clock.setTime(clockRequest.getTime());
            clock.setOwner(clockRequest.getReceiver());
            clock.setSetter(clockRequest.getSender());
            clock.setRing(clockRequest.getRing());
            clock.setTag(clockRequest.getTag());
            clock.setWeek(clockRequest.getWeek());
            clock.setStatus(true);
            clockRepository.save(clock);
        }
        return true;
    }

    @Override
    public boolean createClockRequest(int sender, int receiver, boolean gameType, int gameId, Time time, List<Integer> week, int ring, String tag) {
        if(clockRequestRepository.findBySenderAndReceiverAndType(sender,receiver, 0)!=null){
            return false;
        };

        if(clockRepository.findClockByOwnerAndTime(receiver,time) != null){
            return false;
        }
        ClockRequest clockRequest = new ClockRequest();
        clockRequest.setType(0);
        clockRequest.setSender(sender);
        clockRequest.setReceiver(receiver);
        clockRequest.setGameid(gameId);
        clockRequest.setRing(ring);
        clockRequest.setTag(tag);
        clockRequest.setGametype(gameType);
        clockRequest.setTime(time);
        clockRequest.setRequesttime(new java.sql.Timestamp(System.currentTimeMillis()));
        byte weekbit = 0;
        for(int i = 0;i<week.size();i++){
            byte mask = 1;
            mask <<= week.get(i)-1;
            weekbit |= mask;
        }
        clockRequest.setWeek(weekbit);
        clockRequestRepository.save(clockRequest);

        return true;

    }
}
