package com.se.focusclock.service;

import com.se.focusclock.Repository.ClockRepository;
import com.se.focusclock.entity.Clock;
import com.se.focusclock.service.ClockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.Option;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClockServiceImplTest {
    @Autowired
    ClockService clockService;

    @MockBean
    ClockRepository clockRepository;

    @Test
    void createClock() {
        boolean gameType = true;
        int gameId = 1;
        int setter = 1;
        int owner = 2;
        String time = "09:00:00";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time _time = new Time(d.getTime());
        List<Integer> week = new ArrayList<>();
        week.add(1);
        int ring = 1;
        String tag = "test";

        Clock clock = new Clock();
        clock.setGameid(gameId);
        clock.setGametype(gameType);
        clock.setTime(_time);
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
        when(clockRepository.findClockByOwnerAndTime(6,_time)).thenReturn(clock);
        assertNull(clockService.createClock(gameType, gameId, setter, 6, _time, week, ring, tag));

        when(clockRepository.save(clock)).thenReturn(clock);
        assertEquals("test", clockService.createClock(gameType, gameId, setter, owner, _time, week, ring, tag).getTag());

    }

    @Test
    void modifyClock() {
        when(clockRepository.findById(100)).thenReturn(null);
        assertFalse(clockService.modifyClock(100, true, 1, null, null, 1, null));

        String time = "09:00:00";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time _time = new Time(d.getTime());


        Clock clock = new Clock();
       Optional<Clock> clockOptional = Optional.of(clock);
        when(clockRepository.findById(100)).thenReturn(clockOptional);


        boolean gameType = true;
        int gameId = 1;



        int ring = 1;
        String tag = "test";
        clock.setGameid(gameId);
        clock.setGametype(gameType);
        clock.setTime(_time);
        clock.setRing(ring);
        clock.setTag(tag);
        clock.setWeek((byte)1);

        when(clockRepository.save(clock)).thenReturn(clock);
        List<Integer> week = new ArrayList<>();
        week.add(1);
        assertEquals(true,clockService.modifyClock(100,gameType,gameId,_time,week,ring,tag));

    }

    @Test
    void changeStatus() {
        int id = 2;
        boolean status = true;

        Clock clock = new Clock();
        clock.setId(1);

        Optional<Clock> clockOptional = null;

        when(clockRepository.findById(5)).thenReturn(clockOptional);
        assertFalse(clockService.changeStatus(5, status));

        Optional<Clock> clockOptional2 = Optional.of(clock);
        when(clockRepository.findById(id)).thenReturn(clockOptional2);
        assertTrue(clockService.changeStatus(id, status));
    }

    @Test
    void deleteClock() {
        int id = 2;

        Clock clock = new Clock();
        clock.setId(1);

        Optional<Clock> clockOptional = null;

        when(clockRepository.findById(5)).thenReturn(clockOptional);
        assertFalse(clockService.deleteClock(5));

        Optional<Clock> clockOptional2 = Optional.of(clock);
        when(clockRepository.findById(id)).thenReturn(clockOptional2);
        assertTrue(clockService.deleteClock(id));
    }

    @Test
    void getClockList() {
        int userid = 2;

        List<Clock> clocks = new ArrayList<>();

        when(clockRepository.findClocksByOwner(userid)).thenReturn(clocks);
        assertEquals(clocks, clockService.getClockList(userid));
    }

    @Test
    void getClockById() {
        int id = 1;

        Clock clock = new Clock();
        clock.setId(1);
        Optional<Clock> clockOptional = Optional.of(clock);

        when(clockRepository.findById(id)).thenReturn(clockOptional);
        assertEquals(clock, clockService.getClockById(id));

    }
}