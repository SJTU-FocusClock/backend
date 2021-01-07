package com.se.focusclock.Repository;

import com.se.focusclock.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findByMeOrderByOtherAsc(int me);
    Friend findByMeAndOther(int me, int other);
}
