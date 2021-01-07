package com.se.focusclock.Repository;

import com.se.focusclock.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    @Query(value = "select * from focusclock.friend_request where ((send_user_id = ?1 and receive_user_id = ?2) or (send_user_id = ?2 and receive_user_id = ?1)) and friend_request.type = ?3",nativeQuery = true)
    FriendRequest findBySenderOrReceiverAndType(int sender, int receiver,int type);
    List<FriendRequest> findFriendRequestsByReceiverAndTypeOrderByRequesttimeDesc(int receiver, int type);

}
