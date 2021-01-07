package com.se.focusclock.service;

import com.se.focusclock.Repository.UserRepository;
import com.se.focusclock.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    void save() {
        User user = new User();
        user.setId(2);
        assertNull(userService.save(user));

        user.setPhone("18996460510");
        user.setPassword("jyx2000124");
        user.setUsername("abc18996460510");
        when(userRepository.save(user)).thenReturn(user);
        user.setUsername(null);
        assertEquals("abc18996460510", userService.save(user).getUsername());
    }

    @Test
    void load() {
        User user = new User();
        String username = "abc18996460510";
        String phone = "18996460510";
        int userid = -1;

        user.setId(2);
        user.setUsername(username);
        user.setPhone(phone);

        when(userRepository.findById(userid)).thenReturn(null);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(userRepository.findByPhone(phone)).thenReturn(user);

        assertEquals("18996460510", userService.load(userid, username, null).getPhone());
        assertEquals("18996460510", userService.load(userid, null, phone).getPhone());
    }

    @Test
    void setInfo() {
        int id = -1;
        String nickname = "hahaha";
        boolean sex = true;

        User user = new User();
        user.setId(2);
        when(userRepository.findById(2)).thenReturn(user);

        user.setUsername(nickname);
        user.setSex(sex);
        when(userRepository.save(user)).thenReturn(user);

        assertNull(userService.setInfo(-1, null, false));
        assertEquals("hahaha", userService.setInfo(2, nickname, true).getNickname());
    }

    @Test
    void setEmail() {
        int id = -1;

        User user = new User();
        user.setId(2);
        when(userRepository.findById(2)).thenReturn(user);

        user.setEmail("testemail");
        when(userRepository.save(user)).thenReturn(user);
        assertNull(userService.setEmail(-1, "testemail"));
        assertEquals("testemail", userService.setEmail(2, "testemail").getEmail());
    }


}