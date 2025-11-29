package com.sample.mvcApp.feature.mypage.task.adapter.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sample.mvcApp.common.util.IdUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.web.form.TaskGroupCreateForm;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupUploadInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupResultOutput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupSummaryOutput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupWeekOutput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupUseCase;

@WebMvcTest(TaskGroupController.class)
@AutoConfigureMockMvc
public class TaskGroupControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	TaskGroupUseCase taskGroupUseCase; // @Autowired フィールドに差し込まれる

	// Assert: UseCase に渡された引数をキャプチャして中身を検証
	ArgumentCaptor<TaskGroupCreateInput> captor1 = ArgumentCaptor.forClass(TaskGroupCreateInput.class);

	@Test
	@DisplayName("POST /task/new: 正常系")
	void post_mypage_task_new() throws Exception {

		try (MockedStatic<IdUtil> mocked = mockStatic(IdUtil.class);){
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
	
	@Test
    @DisplayName("GET /mypage/task/list: モデルを初期化して task/list を返す")
    void get_mypage_task_list() throws Exception {
        // --- arrange: UseCase が返すダミーデータを用意 ---
        Map<LocalDate, List<TaskGroupSummaryOutput>> week = new HashMap<>();
        week.put(LocalDate.of(2025, 10, 15), List.of(
            new TaskGroupSummaryOutput("TG001", "実装タスク1", "09:00", "10:00", "高", "DEV"),
            new TaskGroupSummaryOutput("TG002", "実装タスク1", "10:00", "11:00", "高", "DEV")
        ));
        week.put(LocalDate.of(2025, 10, 16), List.of()); // 空の日が混在してもOK

        when(taskGroupUseCase.getTaskGroupWeekRange()).thenReturn(new TaskGroupWeekOutput(week));

        // --- act & assert ---
        var result = mockMvc.perform(get("/mypage/task/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("task/list"))
            .andExpect(model().attributeExists("taskSummaryMaps", "taskGroupCreateForm"))
            .andReturn();

        // --- verify: モデルの中身を軽く検証 ---
        var modelMap = result.getModelAndView().getModel();

        Object mapsObj = modelMap.get("taskSummaryMaps");
        assertNotNull(mapsObj, "taskSummaryMaps should be present");
        assertTrue(mapsObj instanceof Map, "taskSummaryMaps should be a Map");

        @SuppressWarnings("unchecked")
        Map<LocalDate, ?> taskSummaryMaps = (Map<LocalDate, ?>) mapsObj;
        assertTrue(taskSummaryMaps.containsKey(LocalDate.of(2025, 10, 15)));
        assertTrue(taskSummaryMaps.containsKey(LocalDate.of(2025, 10, 16)));

        Object formObj = modelMap.get("taskGroupCreateForm");
        assertNotNull(formObj);
        assertTrue(formObj instanceof TaskGroupCreateForm);

        // UseCase が1回呼ばれたこと
        verify(taskGroupUseCase, times(1)).getTaskGroupWeekRange();
    }

	@Test
	@DisplayName("POST /mypage/task/upload: ファイルアップロード(正常)")
	void post_mypage_task_upload() throws Exception {

		// --- Arrange: multipart で送信するダミーCSV ---
		var csv = new StringBuilder()
				.append("対象日,タイトル,説明,タスク種別,優先度,予定開始,予定終了\n")
				.append("2025/10/01,API製造,基盤側のAPIを製造,開発,高,10:23:34,17:23:34\n")
				.append("2025/10/02,APIテスト,基盤側のAPIをテスト,テスト,中,11:23:34,18:23:34");
		byte[] csvBytes = csv.toString().getBytes();
		MockMultipartFile file = new MockMultipartFile(
				"file", // @RequestParam 名
				"test.csv",
				"text/csv",
				csvBytes);
		
		try (MockedStatic<IdUtil> mocked = mockStatic(IdUtil.class);){
			mocked.when(IdUtil::generateId).thenReturn("TG001");
		
			// UseCase 戻り値
			when(taskGroupUseCase.uploadTaskGroup(any())).thenReturn(new TaskGroupResultOutput(2));

			// --- Act ---
			mockMvc.perform(multipart("/mypage/task/upload")
					.file(file))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/mypage/task/list"));

			// --- Assert: UseCase が呼ばれたか検証 ---
			ArgumentCaptor<TaskGroupUploadInput> captor = ArgumentCaptor.forClass(TaskGroupUploadInput.class);

			List<TaskGroupCreateInput> expectedInputList = List.of(
					new TaskGroupCreateInput(
						 "TG001"
						,LocalDate.of(2025,10,1)
						,"API製造"
						,"基盤側のAPIを製造"
						,"開発"
						, "高"
						,LocalTime.of(10,23,34)
						,LocalTime.of(17,23,34)
						),
					new TaskGroupCreateInput(
						 "TG001"
						,LocalDate.of(2025,10,2)
						,"APIテスト"
						,"基盤側のAPIをテスト"
						,"テスト"
						, "中"
						,LocalTime.of(11,23,34)
						,LocalTime.of(18,23,34)
						)
					);
		mocked.verify(() -> IdUtil.generateId(), times(2));
		verify(taskGroupUseCase, times(1)).uploadTaskGroup(captor.capture());
		List<TaskGroupCreateInput> capturedList = captor.getValue().inputList();
		for(int i = 0; i < capturedList.size(); i++) {
        	assertEquals(capturedList.get(i).toString(), expectedInputList.get(i).toString());
        }
	  }

	}
}
