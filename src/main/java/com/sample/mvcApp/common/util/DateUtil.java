package com.sample.mvcApp.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * 日付ユーティリティ
 */
public class DateUtil {

	/**
	 * 現在のOffsetDateTimeを取得する
	 * @return 現在のOffsetDateTime
	 */
	public static OffsetDateTime getNowOffsetDateTime() {
		return OffsetDateTime.now();
	}
	
	/**
	 * 今週の月曜日の日付を取得する
	 * @return 今週の月曜日の日付
	 */
	public static LocalDate getThisWeekMonday() {
		return LocalDate.now().with(DayOfWeek.MONDAY);
	}
}
