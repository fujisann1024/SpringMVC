package com.sample.mvcApp.common.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
class DateUtilTest {

	  @Nested
	    class ToLocalDateTest {

	        @ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
	        @MethodSource("validDates")
	        void parse_ok(String input, LocalDate expected) {
	            assertEquals(expected, DateUtil.toLocalDate(input));
	        }

	        static Stream<Arguments> validDates() {
	            return Stream.of(
	                arguments("2025-10-31", LocalDate.of(2025,10,31)),
	                arguments("2025/7/1",   LocalDate.of(2025,7,1)),   // 片桁OK
	                arguments("20251031",   LocalDate.of(2025,10,31)),
	                arguments("   2025-10-31   ", LocalDate.of(2025,10,31)) // trimされる
	            );
	        }

	        @ParameterizedTest
	        @ValueSource(strings = {
	            "2025-02-30",  // 不正日付
	            "2025/13/01",  // 不正月
	            "2025-10-31 10:00", // パターン不一致
	            "abc",
	            "" // 空
	        })
	        void parse_ng(String input) {
	            assertThrows(DateTimeParseException.class, () -> DateUtil.toLocalDate(input));
	        }
	    }

	    // ---------------------------
	    // toLocalDateTime
	    // ---------------------------
	    @Nested
	    class ToLocalDateTimeTest {

	        @ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
	        @MethodSource("validDateTimes")
	        void parse_ok(String input, LocalDateTime expected) {
	            assertEquals(expected, DateUtil.toLocalDateTime(input));
	        }

	        static Stream<Arguments> validDateTimes() {
	            return Stream.of(
	                arguments("2025-10-31 10:20:30", LocalDateTime.of(2025,10,31,10,20,30)),
	                arguments("2025-10-31 10:20",    LocalDateTime.of(2025,10,31,10,20)),
	                arguments("2025/10/31 10:20:30", LocalDateTime.of(2025,10,31,10,20,30)),
	                arguments("2025/10/31 10:20",    LocalDateTime.of(2025,10,31,10,20)),
	                arguments("2025-10-31T10:20:30", LocalDateTime.of(2025,10,31,10,20,30)),
	                arguments("2025-10-31T10:20",    LocalDateTime.of(2025,10,31,10,20)),
	                arguments("   2025-10-31 10:20   ", LocalDateTime.of(2025,10,31,10,20)) // trim
	            );
	        }

	        @ParameterizedTest
	        @ValueSource(strings = {
	            "2025/10/31T10:20",  // このT形式は許可していない
	            "2025-10-31",        // 日付のみ
	            "2025-10-31 25:00",  // 不正時刻
	            "abc",
	            ""
	        })
	        void parse_ng(String input) {
	            assertThrows(DateTimeParseException.class, () -> DateUtil.toLocalDateTime(input));
	        }
	    }

	    // ---------------------------
	    // toLocalTime
	    // ---------------------------
	    @Nested
	    class ToLocalTimeTest {

	        @ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
	        @MethodSource("validTimes")
	        void parse_ok(String input, LocalTime expected) {
	            assertEquals(expected, DateUtil.toLocalTime(input));
	        }

	        static Stream<Arguments> validTimes() {
	            return Stream.of(
	                arguments("10:20:30", LocalTime.of(10,20,30)),
	                arguments("09:00",    LocalTime.of(9,0)),
	                arguments("   09:00   ", LocalTime.of(9,0)) // trim
	            );
	        }

	        @ParameterizedTest
	        @ValueSource(strings = { "25:00", "10:61", "10", "10:20:61", "abc", "" })
	        void parse_ng(String input) {
	            assertThrows(DateTimeParseException.class, () -> DateUtil.toLocalTime(input));
	        }
	    }

	    // ---------------------------
	    // formatLocalDate / formatLocalTime
	    // ---------------------------
	    @Nested
	    class FormatTest {

	        @Test
	        void format_date_ok() {
	            var d = LocalDate.of(2025, 11, 1);
	            assertEquals("2025/11/01", DateUtil.formatLocalDate(d, "uuuu/MM/dd"));
	        }

	        @Test
	        void format_time_ok() {
	            var t = LocalTime.of(6, 7, 8);
	            assertEquals("06:07", DateUtil.formatLocalTime(t, "HH:mm"));
	        }

	        @Test
	        void format_null_returns_null() {
	            assertNull(DateUtil.formatLocalDate(null, "uuuu/MM/dd"));
	            assertNull(DateUtil.formatLocalTime(null, "HH:mm"));
	        }
	    }

	    // ---------------------------
	    // getNowOffsetDateTime
	    // ---------------------------
	    @Test
	    void now_offset_datetime_is_close_to_system_now() {
	        var before = OffsetDateTime.now();
	        var actual = DateUtil.getNowOffsetDateTime();
	        var after  = OffsetDateTime.now();

	        // 多少の誤差を許容（±2秒）
	        assertFalse(actual.isBefore(before.minusSeconds(2)));
	        assertFalse(actual.isAfter(after.plusSeconds(2)));
	    }

	    // ---------------------------
	    // getThisWeekMonday
	    // ---------------------------
	    @Test
	    void this_week_monday_matches_system_calc() {
	        LocalDate expected = LocalDate.now().with(DayOfWeek.MONDAY);
	        assertEquals(expected, DateUtil.getThisWeekMonday());
	    }
}
