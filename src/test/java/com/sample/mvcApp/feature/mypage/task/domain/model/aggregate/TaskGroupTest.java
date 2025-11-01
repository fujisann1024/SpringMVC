package com.sample.mvcApp.feature.mypage.task.domain.model.aggregate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;

public class TaskGroupTest {

	private TaskGroupId id(String gid, String ymd) {
		return new TaskGroupId(gid, LocalDate.parse(ymd));
	}

	private TimeSlot slot(String start, String end) {
		return new TimeSlot(LocalTime.parse(start), LocalTime.parse(end));
	}

	@Nested
	@DisplayName("インスタンス生成:正常系")
	class NewCreate {

		@Test
		@DisplayName("前提条件：すべての項目が設定"
				+ " 実行：TaskGroup.newCreate"
				+ " 期待値：toDtoの結果が期待通り")
		void create_ok() {
			var tg = TaskGroup.newCreate(
					id("TG001", "2025-10-15"),
					new Title("実装タスク"),
					"API実装",
					"DEV",
					Priority.HIGH,
					slot("09:00:00", "12:00:00"));


			assertAll("TaskGroup toDto",
					() -> assertEquals("TG001", tg.id().groupId()),
					() -> assertEquals(LocalDate.parse("2025-10-15"), tg.id().workYmd()),
					() -> assertEquals("実装タスク", tg.title().value()),
					() -> assertEquals("API実装", tg.description().orElse(null)),
					() -> assertEquals("DEV", tg.taskTypeCode().orElse(null)),
					() -> assertEquals(Priority.HIGH, tg.priority()),
					() -> assertEquals(LocalTime.parse("09:00:00"), tg.plannedTime().orElse(null).startTime()),
					() -> assertEquals(LocalTime.parse("12:00:00"), tg.plannedTime().orElse(null).endTime()),
					() -> assertNull(tg.actualTime().orElse(null)),
					() -> assertEquals(TaskStatus.PLANNED, tg.status()),
					() -> assertFalse(tg.template())
			);

		}

		@Test
		@DisplayName("前提条件：必須項目以外すべてNULL"
				+ " 実行：TaskGroup.newCreate"
				+ " 期待値：toDtoの結果が期待通り")
		void create_with_null_planned() {
			var tg = TaskGroup.newCreate(
					id("TG002", "2025-10-16"),
					new Title("レビュー"),
					null,
					null,
					Priority.MEDIUM,
					null // ← 許容（コンストラクタも検証なし）
			);


			assertAll("TaskGroup toDto with null planned",
					() -> assertEquals("TG002", tg.id().groupId()),
					() -> assertEquals(LocalDate.parse("2025-10-16"), tg.id().workYmd()),
					() -> assertEquals("レビュー", tg.title().value()),
					() -> assertNull(tg.description().orElse(null)),
					() -> assertNull(tg.taskTypeCode().orElse(null)),
					() -> assertEquals(Priority.MEDIUM, tg.priority()),
					() -> assertNull(tg.plannedTime().orElse(null)),
					() -> assertNull(tg.actualTime().orElse(null)),
					() -> assertEquals(TaskStatus.PLANNED, tg.status()),
					() -> assertFalse(tg.template())
			);
		}

	}

	@Nested
	@DisplayName("インスタンス生成:異常系")
	class Error {

		@Test
		@DisplayName("前提条件：idがNULL"
				+ " 実行：TaskGroup.newCreate"
				+ " 期待値：DomainObjectException発生")
		void create_ng_id_null() {
			Exception exception = assertThrows(DomainObjectException.class, () -> {
				TaskGroup.newCreate(
						null, // ← IDがNULL
						new Title("API作成"),
						"説明",
						"DEV",
						Priority.LOW,
						slot("13:00:00", "15:00:00"));
			});

			String expectedMessage = "TaskGroupId is required";
			String actualMessage = exception.getMessage();

			assertTrue(actualMessage.equals(expectedMessage));
		}

		@Test
		@DisplayName("前提条件：titleがNULL"
				+ " 実行：TaskGroup.newCreate"
				+ " 期待値：DomainObjectException発生")
		void create_ng_title_null() {
			Exception exception = assertThrows(DomainObjectException.class, () -> {
				TaskGroup.newCreate(
						id("TG003", "2025-10-17"),
						null, // ← タイトルがNULL
						"説明",
						"DEV",
						Priority.LOW,
						slot("13:00:00", "15:00:00"));
			});

			String expectedMessage = "Title is required";
			String actualMessage = exception.getMessage();

			assertTrue(actualMessage.equals(expectedMessage));
		}

		@Test
    	@DisplayName("前提条件：priorityがNULL"
				+ " 実行：TaskGroup.newCreate"
				+ " 期待値：DomainObjectException発生")
    	void create_ng_priority_null() {
    		Exception exception = assertThrows(DomainObjectException.class, () -> {
    			TaskGroup.newCreate(
						id("TG004", "2025-10-18"),
						new Title("API作成"),
						"説明",
						"DEV",
						null, // ← 優先度がNULL
						slot("13:00:00", "15:00:00"));
    			});
    		
    			String expectedMessage = "Priority is required";
    			String actualMessage = exception.getMessage();
    					
    			
    			assertTrue(actualMessage.equals(expectedMessage));
    	}
	}
}
