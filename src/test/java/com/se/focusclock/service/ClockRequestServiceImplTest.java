package com.se.focusclock.service;

import com.se.focusclock.Repository.ClockRepository;
import com.se.focusclock.Repository.ClockRequestRepository;
import com.se.focusclock.entity.Clock;
import com.se.focusclock.entity.ClockRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClockRequestServiceImplTest {
    @Autowired
    ClockRequestService clockRequestService;

    @MockBean
    ClockRequestRepository clockRequestRepository;

    @MockBean
    ClockRepository clockRepository;

    @Test
    void getClockRequestList() {
        ClockRequest clockRequest1 = new ClockRequest();
        ClockRequest clockRequest2 = new ClockRequest();
        ClockRequest clockRequest3 = new ClockRequest();
        ClockRequest clockRequest4 = new ClockRequest();
        ClockRequest clockRequest5 = new ClockRequest();

        List<ClockRequest> clockRequests = new ArrayList<>();
        clockRequests.add(clockRequest1);
        clockRequests.add(clockRequest2);
        clockRequests.add(clockRequest3);
        clockRequests.add(clockRequest4);
        clockRequests.add(clockRequest5);

        when(clockRequestRepository.findClockRequestsByReceiverAndTypeOrderByRequesttimeDesc(2, 0)).thenReturn(clockRequests);
        assertEquals(clockRequests, clockRequestService.getClockRequestList(2));
    }

    @Test
    void getClockRequestById() {
        int id = 2;
        ClockRequest clockRequest = new ClockRequest();
        Optional<ClockRequest> clockRequestOptional = Optional.of(clockRequest);

        when(clockRequestRepository.findById(3)).thenReturn(null);
        assertNull(clockRequestService.getClockRequestById(3));

        when(clockRequestRepository.findById(id)).thenReturn(clockRequestOptional);
        assertEquals(clockRequest, clockRequestService.getClockRequestById(id));
    }

    @Test
    void handleClockRequest() {
        int id = 2;
        int type = 0;

        when(clockRequestRepository.findById(3)).thenReturn(null);
        assertFalse(clockRequestService.handleClockRequest(3, type));

        Clock clock = new Clock();
        ClockRequest clockRequest = new ClockRequest();
        clockRequest.setType(0);
        clockRequest.setId(id);
        Optional<ClockRequest> clockRequestOptional = Optional.of(clockRequest);

        when(clockRequestRepository.findById(id)).thenReturn(clockRequestOptional);
        when(clockRepository.save(clock)).thenReturn(clock);

        assertTrue(clockRequestService.handleClockRequest(id, 1));

    }

    @Test
    void createClockRequest() {
        Boolean gameType = true;
        int gameId = 1;
        String time = "09:00:00";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = 1;
        String tag = "test";
        List<Integer> week = new ArrayList<>();
        week.add(1);
        int receiver = 1;
        int sender = 2;

        Clock clock = new Clock();
        clock.setId(1);
        ClockRequest clockRequest = new ClockRequest();

        when(clockRequestRepository.findBySenderAndReceiverAndType(4, 5, 0)).thenReturn(clockRequest);
        assertFalse(clockRequestService.createClockRequest(4, 5, gameType, gameId, _time, week, ring, tag));

        when(clockRepository.findClockByOwnerAndTime(7, _time)).thenReturn(clock);
        when(clockRequestRepository.findBySenderAndReceiverAndType(4, 7, 0)).thenReturn(null);
        assertFalse(clockRequestService.createClockRequest(4, 7, gameType, gameId, _time, week, ring, tag));

        when(clockRequestRepository.findBySenderAndReceiverAndType(sender, receiver, 0)).thenReturn(null);
        when(clockRepository.findClockByOwnerAndTime(receiver, _time)).thenReturn(null);

        clockRequest.setType(0);
        clockRequest.setSender(sender);
        clockRequest.setReceiver(receiver);
        clockRequest.setGameid(gameId);
        clockRequest.setRing(ring);
        clockRequest.setTag(tag);
        clockRequest.setGametype(gameType);
        clockRequest.setTime(_time);
        clockRequest.setRequesttime(new java.sql.Timestamp(System.currentTimeMillis()));
        byte weekbit = 0;
        for(int i = 0;i<week.size();i++){
            byte mask = 1;
            mask <<= week.get(i)-1;
            weekbit |= mask;
        }
        clockRequest.setWeek(weekbit);

        when(clockRequestRepository.save(clockRequest)).thenReturn(clockRequest);
        assertTrue(clockRequestService.createClockRequest(sender, receiver, gameType, gameId, _time, week, ring, tag));

    }
}