package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.sample.mvcApp.common.exception.DomainObjectException;

class TimeSlotTest {

	@Test
	@DisplayName("正常: start=null, end=null は生成OK")
	void ok_when_both_null() {
		assertDoesNotThrow(() -> new TimeSlot(null, null));
		var t = new TimeSlot(null, null);
		assertNull(t.startTime());
		assertNull(t.endTime());
	}

	@ParameterizedTest(name = "[{index}] OK: {0} - {1}")
	@CsvSource({
			"00:00:00,00:00:01",
			"09:00:00,12:00:00",
			"23:59:58,23:59:59"
	})
	@DisplayName("正常: start < end のとき生成OK")
	void ok_when_start_before_end(String s, String e) {
		var slot = new TimeSlot(LocalTime.parse(s), LocalTime.parse(e));
		assertEquals(LocalTime.parse(s), slot.startTime());
		assertEquals(LocalTime.parse(e), slot.endTime());
	}

	@Test
	@DisplayName("異常：片方null(start = null は例外)")
	void ng_when_start_null() {
		var ex = assertThrows(DomainObjectException.class,
				() -> new TimeSlot(null, LocalTime.NOON));
		assertTrue(ex.getMessage().contains("開始時間"));
	}

	@Test
	@DisplayName("異常：片方null(end = null は例外)")
	void ng_when_end_null() {
		var ex = assertThrows(DomainObjectException.class,
				() -> new TimeSlot(LocalTime.NOON, null));
		assertTrue(ex.getMessage().contains("終了時間"));
	}

	@ParameterizedTest(name = "[{index}] NG: {0} - {1}")
	@CsvSource({
			"09:00:00,09:00:00", // 同時刻（長さ0）はNG
			"18:00:00,17:59:59" // 逆転はNG
	})
	@DisplayName("異常：不正レンジ(start >= end は例外)")
	void ng_when_invalid_range(String s, String e) {
		assertThrows(DomainObjectException.class,
				() -> new TimeSlot(LocalTime.parse(s), LocalTime.parse(e)));
	}

}
