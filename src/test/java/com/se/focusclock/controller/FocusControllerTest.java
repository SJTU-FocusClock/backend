package com.se.focusclock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.se.focusclock.entity.FocusRecord;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.FocusRecordService;
import com.se.focusclock.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@WithMockUser(username = "abc18983671259", password = "zm000818")
class FocusControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    FocusRecordService focusRecordService;

    @MockBean
    UserService userService;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void addFocusRecord() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);


      assertThrows(
                Exception.class,
                () ->  mockMvc.perform(
                        post("/focus/addRecord").param("start","33:3").param("end","123").param("duration","60")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn(),
                "Expected doThing() to throw, but it didn't"
        );


        assertThrows(
                Exception.class,
                () ->  mockMvc.perform(
                        post("/focus/addRecord").param("start","09:00:00").param("end","123").param("duration","60")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn(),
                "Expected doThing() to throw, but it didn't"
        );



        String start = "09:00:00";
        String end = "10:00:00";




        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(start);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time startTime = new java.sql.Time(d.getTime());
        try {
            d = format.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time endTime = new java.sql.Time(d.getTime());

       when(focusRecordService.addFocusRecord(user.getId(),startTime,endTime,60,date)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post("/focus/addRecord").param("start",start).param("end",end).param("duration","60").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertNotEquals(0,content.length());
    }

    @Test
    void getRecent7Days() throws Exception {

        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);

        List<Integer> result = new ArrayList<>();
        when(focusRecordService.getRecent7Days(user.getId())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/focus/recent7Days").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(content);
        assertEquals(result,array);
    }

    @Test
    void getRecent6Months() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);

        List<Integer> result = new ArrayList<>();
        when(focusRecordService.getRecent6Months(user.getId())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/focus/recent6Months").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(content);
        assertEquals(result,array);
    }

    @Test
    void geOneDay() throws Exception {
        when(userService.load(-1,"abc18983671259",null)).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/focus/oneDay").param("date","2020-08-18").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("",content);

        User user = new User();
        user.setId(1);
        when(userService.load(-1,"abc18983671259",null)).thenReturn(user);

        assertThrows(
                Exception.class,
                () ->  mockMvc.perform(MockMvcRequestBuilders.get("/focus/oneDay").param("date","20208")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn(),
                "Expected to throw, but it didn't"
        );




        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse("2020-08-18");
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date _date = new java.sql.Date(d.getTime());

        List<FocusRecord> focusRecords = new ArrayList<>();
        when(focusRecordService.getOneDay(user.getId(),_date)).thenReturn(focusRecords);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/focus/oneDay").param("date","2020-08-18").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(content);
        assertEquals(focusRecords,array);

    }
}