package com.se.focusclock.Repository;

import com.se.focusclock.entity.ClockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ClockRecordRepository extends JpaRepository<ClockRecord, Integer> {
    List<ClockRecord> findByUseridAndDate(int userId, Date date);
}
