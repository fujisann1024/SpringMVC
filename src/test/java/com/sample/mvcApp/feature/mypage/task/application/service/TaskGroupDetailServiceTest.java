package com.sample.mvcApp.feature.mypage.task.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupDetailInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupDetailOutput;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupQuery;

@ExtendWith(MockitoExtension.class)
public class TaskGroupDetailServiceTest {

	@Mock
	TaskGroupQuery query;

	@InjectMocks
	TaskGroupDetailService service;

	ArgumentCaptor<TaskGroupId> captor1 = ArgumentCaptor.forClass(TaskGroupId.class);

	@Test
	@DisplayName("正常: タスクグループ詳細取得")
	void test_GetTaskGroupDetail_Succses() {

		var taskGroup = new TaskGroup(
				  new TaskGroupId("TG001", LocalDate.of(2025, 10, 15))
				, new Title("実装タスク1")
				, Optional.ofNullable("API製造")
				, Optional.ofNullable("DEV")
				, Priority.HIGH
				, Optional.ofNullable(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(11, 0)))
				, Optional.ofNullable(new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0)))
				, TaskStatus.DONE
				, false
				);
		
		when(query.getTaskGroupById(captor1.capture())).thenReturn(taskGroup);
		
		TaskGroupDetailOutput output = service.getTaskGroupDetail(
				new TaskGroupDetailInput(
						"TG001",
						LocalDate.of(2025, 10, 15)
						)
				);
		
		TaskGroupId capturedId = captor1.getValue();
		
		assertAll(
				() -> assertEquals("TG001", output.groupId()),
				() -> assertEquals(LocalDate.of(2025, 10, 15), output.workYmd()),
				() -> assertEquals("実装タスク1", output.title()),
				() -> assertEquals("API製造", output.description()),
				() -> assertEquals("DEV", output.taskTypeCode()),
				() -> assertEquals("高", output.priority()),
				() -> assertEquals("09:00", output.plannedStartTime()),
				() -> assertEquals("11:00", output.plannedEndTime()),
				() -> assertEquals("10:00", output.actualStartTime()),
				() -> assertEquals("12:00", output.actualEndTime()),
				() -> assertEquals("完了", output.status()),
				() -> assertFalse(output.template())
				);
		
		assertAll(
				() -> assertEquals("TG001", capturedId.groupId()),
				() -> assertEquals(LocalDate.of(2025, 10, 15), capturedId.workYmd())
				);
		
	}
}
