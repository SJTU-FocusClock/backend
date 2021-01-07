package com.se.focusclock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.se.focusclock.entity.User;
import com.se.focusclock.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@ContextConfiguration
@SpringBootTest
class UserControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void currentUser1() throws Exception {
        String url = "/users/user";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertThat(result, containsString("Not logged in"));
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void currentUser2() throws Exception {
        String url = "/users/user";

        String username = null;


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertThat(result, containsString("Not logged in"));
    }

    @Test
    void ulogin() throws Exception {
        String url = "/users/Ulogin";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(result, "Please Login in");
    }

    @Test
    void plogin() throws Exception {
        String url = "/users/Plogin";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(result, "Please Login in");
    }

    @Test
    void register() throws Exception {
        String url = "/users/register";
        String phone = "13320204630";
        String password = "jyx2000124";

        User user2 = new User();
        user2.setId(2);

        // 手机格式错误验证
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .param("phone", "188e")
                .param("password", password)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, containsString("status"));

        // 手机号已存在验证
        when(userService.load(-1, null, "18996460510")).thenReturn(user2);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .param("phone", "18996460510")
                .param("password", password))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        assertThat(result, containsString("status"));

        // 密码格式错误
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .param("phone", phone)
                .param("password", "12346"))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        assertThat(result, containsString("status"));

        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .param("phone", phone)
                .param("password", password))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(user.getPhone(), object.get("phone"));

    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void alterInfo() throws Exception {
        String url = "/users/alterInfo";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);

        String nickname = "hahaha";
        boolean sex = true;
        user.setNickname(nickname);
        user.setSex(sex);

        when(userService.setInfo(user.getId(), nickname, sex)).thenReturn(user);

        JSONObject newObject = new JSONObject();
        newObject.put("nickname", nickname);
        newObject.put("sex", sex);
        String requestJson = JSONObject.toJSONString(newObject);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(user.getId(), object.get("id"));
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void getUserInfo() throws Exception {
        String url = "/users/getUserInfo/2";

        User user = new User();
        user.setId(2);
        when(userService.load(2,null,null)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(user.getId(), object.get("id"));
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void getUserCredit() throws Exception {
        String url = "/users/getUserCredit";

        User user = new User();
        user.setId(2);
        user.setCredit(1);

        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);
        when(userService.load(user.getId(),null,null)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(""+user.getCredit(), result);
    }

    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void setPassword() throws Exception {
        String url = "/users/setPassword";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);
        user.setPassword("testpassword");
        when(userService.save(user)).thenReturn(user);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .param("password","testpassword")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(user.getId(), object.get("id"));

    }


    @Test
    @WithMockUser(username = "abc18996460510", password = "jyx2000124")
    void setEmail() throws Exception {
        String url = "/users/setEmail";

        User user = new User();
        user.setId(2);
        when(userService.load(-1,"abc18996460510",null)).thenReturn(user);

        when(userService.setEmail(2,"testemail")).thenReturn(user);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .param("email","testemail")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JSONObject object = JSON.parseObject(result);
        assertEquals(user.getId(), object.get("id"));

    }
}