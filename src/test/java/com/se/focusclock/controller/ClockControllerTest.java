package com.se.focusclock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.se.focusclock.entity.Clock;
import com.se.focusclock.entity.ClockRecord;
import com.se.focusclock.entity.ClockRequest;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.ClockRecordService;
import com.se.focusclock.service.ClockRequestService;
import com.se.focusclock.service.ClockService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebAppConfiguration
class ClockControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    ClockService clockService;

    @MockBean
    ClockRequestService clockRequestService;

    @MockBean
    ClockRecordService clockRecordService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void createClock() throws Exception {
        String url = "/clocks/create";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);

        Boolean gameType = true;
        int gameId = 1;
        String time = "09:00:00";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = 1;
        String tag = "test";
        List<Integer> week = new ArrayList<>();
        week.add(1);

        JSONObject clockObject = new JSONObject();
        clockObject.put("gameType", gameType);
        clockObject.put("gameId", gameId);
        clockObject.put("time", time);
        clockObject.put("ring", ring);
        clockObject.put("tag", tag);
        clockObject.put("week", week);
        String requestJson = JSONObject.toJSONString(clockObject);

        Clock clock = new Clock();
        clock.setGametype(false);
        when(clockService.createClock(gameType, gameId, user.getId(), user.getId(), _time, week, ring, tag)).thenReturn(clock);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        JSONObject object = JSON.parseObject(result);
        assertEquals(clock.isGametype(),object.get("gametype"));
    }

    @Test
    void modifyClock() throws Exception {
        String url = "/clocks/modify/1";

        Boolean gameType = true;
        int gameId = 1;
        String time = "09:00:00";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = 1;
        String tag = "test";
        List<Integer> week = new ArrayList<>();
        week.add(1);

        JSONObject clockObject = new JSONObject();
        clockObject.put("gameType", gameType);
        clockObject.put("gameId", gameId);
        clockObject.put("time", time);
        clockObject.put("ring", ring);
        clockObject.put("tag", tag);
        clockObject.put("week", week);
        String requestJson = JSONObject.toJSONString(clockObject);

        when(clockService.modifyClock(1,gameType,gameId,_time,week,ring,tag)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("true",result);
    }

    @Test
    void deleteClock() throws Exception {
        String url = "/clocks/delete/1";

        when(clockService.deleteClock(1)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("true", result);
    }

    @Test
    void setStatus() throws Exception {
        String url = "/clocks/setStatus";

        int clockId = 1;
        boolean status = true;

        when(clockService.changeStatus(clockId, status)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .param("clockId", String.valueOf(clockId))
                .param("status", String.valueOf(status))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("true", result);
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void getClockList() throws Exception {
        String url = "/clocks/clockList";

        User user = new User();
        user.setId(2);
        List<Clock> clocks = new ArrayList<>();

        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);
        when(clockService.getClockList(user.getId())).thenReturn(clocks);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(result);
        assertEquals(clocks, array);

    }

    @Test
    void getClockById() throws Exception {
        String url = "/clocks/clock/1";

        Clock clock = new Clock();
        clock.setId(1);

        when(clockService.getClockById(1)).thenReturn(clock);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(clock.getId(), object.get("id"));
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void getClockRequestList() throws Exception {
        String url = "/clocks/requestList";

        User user = new User();
        user.setId(2);
        List<ClockRequest> clockRequests = new ArrayList<>();

        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);
        when(clockRequestService.getClockRequestList(user.getId())).thenReturn(clockRequests);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(result);
        assertEquals(clockRequests, array);
    }

    @Test
    void getClockRequestById() throws Exception {
        String url = "/clocks/getRequest/1";

        ClockRequest clockRequest = new ClockRequest();
        clockRequest.setId(1);

        when(clockRequestService.getClockRequestById(1)).thenReturn(clockRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(clockRequest.getId(), object.get("id"));
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void sendClockRequest() throws Exception {
        String url = "/clocks/sendRequest";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);

        Boolean gameType = true;
        int gameId = 1;
        String time = "09:00:00";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time _time = new java.sql.Time(d.getTime());
        int ring = 1;
        String tag = "test";
        List<Integer> week = new ArrayList<>();
        week.add(1);
        int receiver = 1;

        JSONObject clockObject = new JSONObject();
        clockObject.put("gameType", gameType);
        clockObject.put("gameId", gameId);
        clockObject.put("time", time);
        clockObject.put("ring", ring);
        clockObject.put("tag", tag);
        clockObject.put("week", week);
        clockObject.put("receiver", receiver);
        String requestJson = JSONObject.toJSONString(clockObject);

        when(clockRequestService.createClockRequest(user.getId(),receiver,gameType,gameId,_time,week,ring,tag)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("true",result);

    }

    @Test
    void handleRequest() throws Exception {
        String url = "/clocks/handle";

        int requestId = 1;
        int type = 2;

        when(clockRequestService.handleClockRequest(requestId, type)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .param("requestId", String.valueOf(requestId))
                .param("type", String.valueOf(type))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("true", result);
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void createClockRecord() throws Exception {
        String url = "/clocks/createRecord";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);

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

        when(clockRecordService.createClockRecord(user.getId(),startTime,endTime,date)).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .param("start", start)
                .param("end", end)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("false", result);
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void getClockRecord() throws Exception {
        String url = "/clocks/getRecord";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);

        String date = "2020-01-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date _date = new java.sql.Date(d.getTime());

        List<ClockRecord> clockRecords = new ArrayList<>();

        when(clockRecordService.getByDate(user.getId(),_date)).thenReturn(clockRecords);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .param("date", date)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONArray array = JSON.parseArray(result);
        assertEquals(clockRecords, array);
    }
}