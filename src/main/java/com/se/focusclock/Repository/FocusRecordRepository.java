package com.se.focusclock.Repository;

import com.se.focusclock.entity.FocusRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FocusRecordRepository extends JpaRepository<FocusRecord, Integer> {
    @Query(value = "select sum(duration) from focusclock.focus_record where date = ?2 and user_id = ?1",nativeQuery = true)
    Integer findByDate(int userid,Date date);
    @Query(value = "select sum(duration) from focusclock.focus_record where user_id = ?1 and date >= ?2 and date <= ?3",nativeQuery = true)
    Integer findBetweenDate1AndDate2(int userid,Date date1,Date date2);

    List<FocusRecord> findFocusRecordsByUseridAndDate(int userid,Date date);
}
