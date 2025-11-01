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

public class DayTaskGroupCollectionTest {

	  // ======== helpers ========
	  private TaskGroupId id(String gid, String ymd) {
	    return new TaskGroupId(gid, LocalDate.parse(ymd));
	  }

	  private TimeSlot slot(String start, String end) {
	    return new TimeSlot(LocalTime.parse(start), LocalTime.parse(end));
	  }

	  private TimeSlot slotNullable(String start, String end) {
	    LocalTime s = (start == null) ? null : LocalTime.parse(start);
	    LocalTime e = (end == null) ? null : LocalTime.parse(end);
	    return new TimeSlot(s, e);
	  }

	  private TaskGroup tg(String gid, String ymd, String title, String start, String end, Priority p) {
	    return TaskGroup.newCreate(
	        id(gid, ymd),
	        new Title(title),
	        "desc",
	        "DEV",
	        p,
	        start == null && end == null ? null : slot(start, end)
	    );
	  }

	  // ======== tests ========

	  @Test
	  @DisplayName("正常：同日タスクのみ抽出され、startTime→priority→id で安定ソートされる")
	  void create_filters_and_sorts() {
	    var date = "2025-10-15";
	    var others = tg("X3", "2025-10-16", "次日", "09:00:00", "10:00:00", Priority.LOW); // 別日→除外

	    var g1 = tg("A1", date, "同日", "10:00:00", "11:00:00", Priority.HIGH);
	    var g2 = tg("A2", date, "同日", "11:00:00", "12:00:00", Priority.MEDIUM);

	    var col = DayTaskGroupCollection.of(LocalDate.parse(date), List.of(g1, g2, others));

	    assertEquals(LocalDate.parse(date), col.getTaskDate());
	    var list = col.getTaskGroups();
	    assertEquals(2, list.size());
	    // 開始予定時刻(昇順)で並んでいるか
	    assertEquals("A1", list.get(0).id().groupId());
	    assertEquals("A2", list.get(1).id().groupId());
	  }

	  @Test
	  @DisplayName("正常：区間の隣接（prevEnd == currStart）は重複とみなさずOK")
	  void adjacent_is_ok() {
	    var date = LocalDate.parse("2025-10-15");
	    var g1 = tg("A", "2025-10-15", "前半", "09:00:00", "10:00:00", Priority.HIGH);
	    var g2 = tg("B", "2025-10-15", "後半", "10:00:00", "11:00:00", Priority.HIGH);

	    assertDoesNotThrow(() -> DayTaskGroupCollection.of(date, List.of(g2, g1))); // 並び替えも検証
	  }

	  @Test
	  @DisplayName("異常：時間帯が真に重なる（prevEnd > currStart）と DomainObjectException")
	  void overlapping_throws() {
	    var date = LocalDate.parse("2025-10-15");
	    var g1 = tg("A", "2025-10-15", "前半", "09:00:00", "10:01:00", Priority.HIGH);
	    var g2 = tg("B", "2025-10-15", "後半", "10:00:00", "11:00:00", Priority.HIGH);

	    assertThrows(DomainObjectException.class,
	        () -> DayTaskGroupCollection.of(date, List.of(g1, g2)));
	  }

	  @Test
	  @DisplayName("異常：同一ID（TaskGroupId）が同日に重複すると DomainObjectException")
	  void duplicate_id_throws() {
	    var date = LocalDate.parse("2025-10-15");
	    var g1 = tg("DUP", "2025-10-15", "A", "09:00:00", "10:00:00", Priority.HIGH);
	    var g2 = tg("DUP", "2025-10-15", "B", "10:00:00", "11:00:00", Priority.MEDIUM);

	    assertThrows(DomainObjectException.class,
	        () -> DayTaskGroupCollection.of(date, List.of(g1, g2)));
	  }

	  @Test
	  @DisplayName("正常：totalPlanned は null/未設定の plannedTime を無視して合計を返す")
	  void totalPlanned_ignores_nulls() {
	    var date = LocalDate.parse("2025-10-15");

	    // 正味 2h + 1.5h = 3.5h
	    var g1 = tg("A", "2025-10-15", "A", "09:00:00", "11:00:00", Priority.HIGH);
	    var g2 = tg("B", "2025-10-15", "B", "13:00:00", "14:30:00", Priority.LOW);
	    // plannedTime 未設定（null許可の TimeSlot を使う／newCreate に null を渡す）
	    var g3 = TaskGroup.newCreate(
	        id("C", "2025-10-15"),
	        new Title("C"),
	        "desc",
	        "DEV",
	        Priority.MEDIUM,
	        slotNullable(null, null) // or null でも可（設計に合わせて）
	    );

	    var col = DayTaskGroupCollection.of(date, List.of(g1, g2, g3));
	    assertEquals(Duration.ofMinutes(210), col.totalPlanned()); // 3.5h = 210min
	  }
}
