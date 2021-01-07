package com.se.focusclock.Repository;

import com.se.focusclock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);

    User findByPhoneAndPassword(String phone, String password);

    User findByUsername(String username);

    User findByPhone(String phone);

    User findById(int id);
}

