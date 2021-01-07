package com.se.focusclock.service;

import com.se.focusclock.entity.ClockRequest;

import java.sql.Time;
import java.util.List;

public interface ClockRequestService {
    List<ClockRequest> getClockRequestList(int userid);
    ClockRequest getClockRequestById(int id);
    boolean handleClockRequest(int id, int type);
    boolean createClockRequest(int sender, int receiver, boolean gameType, int gameId, Time time, List<Integer> week, int ring, String tag);
}
