package com.sample.mvcApp.feature.mypage.task.domain.model.collection;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;

public class TaskGroupCollectionMapTest {

	  // ===== helpers =====
	  private TaskGroupId id(String gid, String ymd) {
	    return new TaskGroupId(gid, LocalDate.parse(ymd));
	  }

	  private TimeSlot slot(String start, String end) {
	    return new TimeSlot(LocalTime.parse(start), LocalTime.parse(end));
	  }

	  private TaskGroup tg(String gid, String ymd, String title, String start, String end, Priority p) {
	    return TaskGroup.newCreate(
	        id(gid, ymd),
	        new Title(title),
	        "desc",
	        "DEV",
	        p,
	        slot(start, end)
	    );
	  }

	  // ===== tests =====

	  @Test
	  @DisplayName("of: 日付でグルーピングされ、各日が DayTaskGroupCollection として作成される")
	  void of_groups_by_date_and_builds_days() {
	    var g1 = tg("A1", "2025-10-15", "T1", "09:00:00", "10:00:00", Priority.HIGH);
	    var g2 = tg("A2", "2025-10-15", "T2", "10:00:00", "11:00:00", Priority.MEDIUM);
	    var g3 = tg("B1", "2025-10-16", "T3", "08:30:00", "09:00:00", Priority.LOW);

	    var map = TaskGroupCollectionMap.of(List.of(g1, g2, g3));

	    assertEquals(2, map.getByDate().size());
	    assertTrue(map.getByDate().containsKey(LocalDate.parse("2025-10-15")));
	    assertTrue(map.getByDate().containsKey(LocalDate.parse("2025-10-16")));

	    var d1 = map.getByDate().get(LocalDate.parse("2025-10-15"));
	    var d2 = map.getByDate().get(LocalDate.parse("2025-10-16"));
	    assertEquals(2, d1.getTaskGroups().size());
	    assertEquals(1, d2.getTaskGroups().size());
	  }

	  @Test
	  @DisplayName("totalPlanned: 期間内の各日の合計予定時間を集計する")
	  void totalPlanned_sums_within_range() {
	    // 2025-10-15: 1h + 1.5h = 2.5h (150min)
	    var g1 = tg("A1", "2025-10-15", "T1", "09:00:00", "10:00:00", Priority.HIGH);
	    var g2 = tg("A2", "2025-10-15", "T2", "13:00:00", "14:30:00", Priority.MEDIUM);
	    // 2025-10-16: 2h = 120min
	    var g3 = tg("B1", "2025-10-16", "T3", "10:00:00", "12:00:00", Priority.LOW);
	    // 2025-10-17: 1h = 60min（期間外にして集計から外れることを確認）
	    var g4 = tg("C1", "2025-10-17", "T4", "09:00:00", "10:00:00", Priority.LOW);

	    var map = TaskGroupCollectionMap.of(List.of(g1, g2, g3, g4));

	    var from = LocalDate.parse("2025-10-15");
	    var to   = LocalDate.parse("2025-10-16");
	    assertEquals(Duration.ofMinutes(150 + 120), map.totalPlanned(from, to));
	  }

	  @Test
	  @DisplayName("asFlatList: 日付昇順→各日の並び順（Day側の並び）でフラット化される")
	  void asFlatList_orders_by_date_then_day_order() {
	    // 15日: 09:00→10:00
	    var d15a = tg("A1", "2025-10-15", "T1", "09:00:00", "09:30:00", Priority.MEDIUM);
	    var d15b = tg("A2", "2025-10-15", "T2", "10:00:00", "11:00:00", Priority.HIGH);
	    // 16日: 08:00のみ
	    var d16  = tg("B1", "2025-10-16", "T3", "08:00:00", "09:00:00", Priority.LOW);

	    var map = TaskGroupCollectionMap.of(List.of(d15b, d16, d15a)); // わざと順不同で投入

	    var flat = map.asFlatList();
	    assertEquals(3, flat.size());
	    // 15日の2件が先、その中は DayTaskGroupCollection のルールで 09:00 → 10:00
	    assertEquals("A1", flat.get(0).id().groupId());
	    assertEquals("A2", flat.get(1).id().groupId());
	    // 次に 16日
	    assertEquals("B1", flat.get(2).id().groupId());
	  }

	  @Test
	  @DisplayName("of: 引数nullで DomainObjectException")
	  void of_null_throws() {
	    assertThrows(DomainObjectException.class, () -> TaskGroupCollectionMap.of(null));
	  }
}
