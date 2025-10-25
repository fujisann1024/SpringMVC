package com.sample.mvcApp.feature.mypage.task.adapter.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sample.mvcApp.common.util.IdUtil;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupUseCase;

@WebMvcTest(TaskGroupController.class)
@AutoConfigureMockMvc
class TaskGroupControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	TaskGroupUseCase taskGroupUseCase; // @Autowired フィールドに差し込まれる

	// Assert: UseCase に渡された引数をキャプチャして中身を検証
	ArgumentCaptor<TaskGroupCreateInput> captor1 = ArgumentCaptor.forClass(TaskGroupCreateInput.class);

	@Test
	@DisplayName("POST /task/new: 正常系")
	void postCreate_callsUsecase_and_redirects() throws Exception {

		MockedStatic<IdUtil> mocked = mockStatic(IdUtil.class);
		mocked.when(IdUtil::generateId).thenReturn("TG001");

		mockMvc.perform(post("/mypage/task/new")
				.param("title", "実装タスク")
				.param("targetYmd", "2025-10-15")
				.param("explanation", "API実装")
				.param("taskKind", "DEV")
				.param("priority", "高")
				.param("plannedStart", "09:00")
				.param("plannedEnd", "12:00"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/mypage/task/list"));

		verify(taskGroupUseCase, times(1)).create(captor1.capture());
		TaskGroupCreateInput in = captor1.getValue();

		mocked.verify(() -> IdUtil.generateId(), times(1));
		
		assertAll(
				() -> assertEquals("TG001", in.taskGroupId()),
				() -> assertEquals(LocalDate.parse("2025-10-15"), in.workYmd()),
				() -> assertEquals("実装タスク", in.title()),
				() -> assertEquals("API実装", in.description()),
				() -> assertEquals("DEV", in.taskTypeCode()),
				() -> assertEquals("高", in.priority()),
				() -> Assertions.assertEquals(LocalTime.parse("09:00"), in.plannedStartTime()),
				() -> Assertions.assertEquals(LocalTime.parse("12:00"), in.plannedEndTime()));
	}
}
