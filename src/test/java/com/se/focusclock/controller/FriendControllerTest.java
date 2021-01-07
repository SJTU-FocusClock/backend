package com.se.focusclock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.se.focusclock.entity.FriendRequest;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.FriendRequestService;
import com.se.focusclock.service.FriendService;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.LoginUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebAppConfiguration
@WithMockUser(username = "abc18983671259", password = "zm000818")
class FriendControllerTest {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;


    @MockBean
    FriendService friendService;

    @MockBean
    UserService userService;

    @MockBean
    FriendRequestService friendRequestService;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getFriendList() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);
        List<User> users = new ArrayList<>();
        when(friendService.getFriendList(1)).thenReturn(users);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/friends/list").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(content);
        assertEquals(users,array);
    }

    @Test
    void searchUserByPhone() throws Exception {
        User user = new User();
        user.setId(1);
        when(friendService.searchByPhone("18983671259")).thenReturn(user);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/friends/search").param("phone","18983671259").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject  object = JSON.parseObject(content);
        assertEquals(user.getId(),object.get("id"));
    }

    @Test
    void deleteFriend() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/friends/remove/-1").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("false",content);

        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);
        when(friendService.removeFriend(1,2)).thenReturn(true);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/friends/remove/2").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        assertEquals("true",content);
    }

    @Test
    void sendRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/friends/sendRequest/-1").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("false",content);

        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);
        when(friendRequestService.sendFriendRequest(user.getId(),2)).thenReturn(true);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/friends/sendRequest/2").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        assertEquals("true",content);
    }

    @Test
    void getRequestList() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);

        List<FriendRequest> friendRequests = new ArrayList<>();
        when(friendRequestService.getFriendRequestList(user.getId())).thenReturn(friendRequests);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/friends/getRequestList").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(content);
        assertEquals(friendRequests,array);


    }

    @Test
    void handleRequest() throws Exception {
        when(friendRequestService.handleFriendRequst(1,1)).thenReturn(true);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/friends//handle").param("requestId","1").param("type","1").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("true",content);

    }
}