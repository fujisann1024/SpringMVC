package com.sample.mvcApp.feature.mypage.task.adapter.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskDetailItemView;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupDetailInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupDetailOutput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupDetailUseCase;

@WebMvcTest(TaskGroupDetailController.class)
@AutoConfigureMockMvc
public class TaskGroupDetailControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	TaskGroupDetailUseCase taskGroupDetailUseCase; // @Autowired フィールドに差し込まれる

	// Assert: UseCase に渡された引数をキャプチャして中身を検証
	ArgumentCaptor<TaskGroupDetailInput> captor1 = ArgumentCaptor.forClass(TaskGroupDetailInput.class);

	@Test
	@DisplayName("GET /mypage/task/detail: モデルを初期化して task/detail を返す")
	void get_mypage_task_detail() throws Exception {

		TaskGroupDetailOutput output = new TaskGroupDetailOutput(
				"TG001",
				LocalDate.of(2025, 10, 15),
				"実装タスク1",
				"API製造",
				"DEV",
				"高",
				"09:00",
				"11:00",
				"10:00",
				"12:00",
				"未着手",
				false);

		when(taskGroupDetailUseCase.getTaskGroupDetail(any())).thenReturn(output);

		var result = mockMvc.perform(get("/mypage/task/detail")
				.param("groupId", "TG001")
				.param("workYmd", "2025-10-15"))
				.andExpect(status().isOk())
				.andExpect(view().name("task/detail"))
				.andExpect(model().attributeExists("taskDetail"))
				.andReturn();

		var modelMap = result.getModelAndView().getModel();
		TaskDetailItemView detail = (TaskDetailItemView) modelMap.get("taskDetail");
		assertAll(
				() -> assertEquals("TG001", detail.getGroupId()),
				() -> assertEquals("2025/10/15", detail.getWorkDate()),
				() -> assertEquals("実装タスク1", detail.getTitle()),
				() -> assertEquals("API製造", detail.getDescription()),
				() -> assertEquals("DEV", detail.getTaskKind()),
				() -> assertEquals("高", detail.getPriority()),
				() -> assertEquals("09:00 - 11:00", detail.getPlannedTime()),
				() -> assertEquals("10:00 - 12:00", detail.getActualTime()),
				() -> assertEquals("未着手", detail.getStatus()),
				() -> assertFalse(detail.isTemplate())
				);

		verify(taskGroupDetailUseCase, times(1)).getTaskGroupDetail(captor1.capture());
		
		TaskGroupDetailInput in = captor1.getValue();
		assertAll(
				() -> assertEquals("TG001", in.groupId()),
				() -> assertEquals(LocalDate.of(2025, 10, 15), in.workYmd())
				);
	}
}
