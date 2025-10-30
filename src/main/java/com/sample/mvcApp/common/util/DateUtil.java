package com.sample.mvcApp.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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
	
	/**
	 * LocalDateを指定のフォーマットの文字列に変換する
	 */
	public static String formatLocalDate(LocalDate date, String pattern) {
		if (date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(formatter);
	}
	
	/**
	 * LocalTimeを指定のフォーマットの文字列に変換する
	 */
	public static String formatLocalTime(LocalTime time, String pattern) {
		if (time == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return time.format(formatter);
	}
}
