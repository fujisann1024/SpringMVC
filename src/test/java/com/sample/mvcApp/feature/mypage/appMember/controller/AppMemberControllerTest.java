package com.sample.mvcApp.feature.mypage.appMember.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sample.mvcApp.feature.mypage.appMember.dto.AppMemberOutDto;
import com.sample.mvcApp.feature.mypage.appMember.service.AppMemberService;

@WebMvcTest(controllers = AppMemberListController.class)
@AutoConfigureMockMvc
class AppMemberControllerTest {

	@Autowired
    MockMvc mockMvc;

	@MockitoBean
    AppMemberService service;
    
    @Test
    @DisplayName("GET /mypage/app-member/list 正常: view名・model属性・呼び出し回数を検証")
    void getAppMemberList_ok() throws Exception {
        // Arrange
    	var outDtoList = List.of(
				new AppMemberOutDto().builder()
						.memberId("111aaa")
						.email("test1@email.com")
						.password("password1")
						.familyName("familyName1")
						.givenName("givenName1")
						.prefecture("prefecture1")
						.gender("gender1")
						.birthday(LocalDate.of(2020, 1, 5))
						.joinedOn(LocalDate.of(2020, 2, 5))
						.leftOn(LocalDate.of(2020, 2, 5))
						.status("status1")
						.build(),
				new AppMemberOutDto().builder()
						.memberId("222aaa")
						.email("test2@email.com")
						.password("password2")
						.familyName("familyName2")
						.givenName("givenName2")
						.prefecture("prefecture2")
						.gender("gender2")
						.birthday(LocalDate.of(2021, 1, 5))
						.joinedOn(LocalDate.of(2021, 2, 5))
						.leftOn(LocalDate.of(2021, 2, 5))
						.status("status2")
						.build());
    	 when(service.getAppMemberOutEntityList()).thenReturn(outDtoList);

         // Act & Assert: Helperは実行され、Modelに詰められた内容をプロパティで検証
         mockMvc.perform(get("/mypage/app-member/list"))
             .andExpect(status().isOk())
             .andExpect(view().name("appMember/list"))
             .andExpect(model().attribute("msg", "タイムリーフ"))
             .andExpect(model().attribute("appMembers", hasSize(2)))
             .andExpect(model().attribute("appMembers", contains(
                 allOf(
                     hasProperty("fullName", is("familyName1　givenName1")),
                     hasProperty("prefecture", is("prefecture1")),
                     hasProperty("gender", is("gender1")),
                     hasProperty("birthday", is("2020年1月5日"))
                 ),
                 allOf(
                		 hasProperty("fullName", is("familyName2　givenName2")),
                         hasProperty("prefecture", is("prefecture2")),
                         hasProperty("gender", is("gender2")),
                         hasProperty("birthday", is("2021年1月5日")))
                 )
             ));

         // 呼び出し回数の検証
         verify(service, times(1)).getAppMemberOutEntityList();
        }
}
