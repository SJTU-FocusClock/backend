package com.se.focusclock.service;

import com.se.focusclock.entity.Clock;

import java.sql.Time;
import java.util.List;

public interface ClockService {
    Clock createClock(boolean gameType, int gameId, int Setter, int Owner, Time time, List<Integer> week, int ring, String tag);
    boolean modifyClock(int id, boolean gameType, int gameId, Time time, List<Integer> week, int ring, String tag);
    boolean changeStatus(int id, boolean status);
    boolean deleteClock(int id);
    List<Clock> getClockList(int userid);
    Clock getClockById(int id);
}
