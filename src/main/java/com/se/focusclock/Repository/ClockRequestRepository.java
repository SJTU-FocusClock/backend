package com.se.focusclock.Repository;

import com.se.focusclock.entity.ClockRequest;
import com.se.focusclock.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClockRequestRepository extends JpaRepository<ClockRequest, Integer> {
    List<ClockRequest> findClockRequestsByReceiverAndTypeOrderByRequesttimeDesc(int receiver, int type);
    ClockRequest findBySenderAndReceiverAndType(int sender, int receiver, int type);
}
