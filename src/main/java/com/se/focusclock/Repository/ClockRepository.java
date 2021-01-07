package com.se.focusclock.Repository;

import com.se.focusclock.entity.Clock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface ClockRepository extends JpaRepository<Clock, Integer> {
    Clock findClockByOwnerAndTime(int owner, Time time);
    List<Clock> findClocksByOwner(int owner);
}
