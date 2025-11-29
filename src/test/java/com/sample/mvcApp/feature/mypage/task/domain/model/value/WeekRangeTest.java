package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.sample.mvcApp.common.exception.DomainObjectException;

public class WeekRangeTest {
	@Test
	@DisplayName("正常：開始日から7日間の WeekRange が生成される")
	void ok_create_week_range() {
		LocalDate start = LocalDate.of(2025, 1, 6); // 月曜日想定
		WeekRange wr = WeekRange.of(start);

		assertEquals(start, wr.getStart());
		assertEquals(start.plusDays(6), wr.getEnd());
	}

	@ParameterizedTest(name = "[{index}] OK: start={0}")
	@CsvSource({
			"2025-01-01",
			"2025-02-10",
			"2025-12-25"
	})
	@DisplayName("正常：異なる開始日で週範囲を生成できる")
	void ok_create_for_various_starts(String s) {
		LocalDate start = LocalDate.parse(s);
		WeekRange wr = WeekRange.of(start);

		assertEquals(start, wr.getStart());
		assertEquals(start.plusDays(6), wr.getEnd());
	}

	@Test
	@DisplayName("異常：start=null")
	void ng_when_start_null() {
		var ex = assertThrows(DomainObjectException.class,
				() -> WeekRange.of(null));
		assertTrue(ex.getMessage().contains("week range is required"));
	}


	@Test
	@DisplayName("正常：開始〜終了の日付リストが 7 日分生成される")
	void ok_get_range_date_list() {
		LocalDate start = LocalDate.of(2025, 1, 1);
		WeekRange wr = WeekRange.of(start);

		List<LocalDate> list = wr.getRangeDateList();

		assertEquals(7, list.size());
		assertEquals(start, list.get(0));
		assertEquals(start.plusDays(6), list.get(6));
	}

}
