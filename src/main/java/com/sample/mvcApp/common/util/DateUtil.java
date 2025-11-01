package com.sample.mvcApp.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * 日付ユーティリティ
 */
public class DateUtil {


	/** 文字列→LocalDate（厳密） */
	public static LocalDate toLocalDate(String s) {
		
		List<DateTimeFormatter> DATE_FMTS = List.of(
				strict("uuuu-MM-dd"), // 2025-10-31
				strict("uuuu/M/d"), // 2025/10/31, 2025/7/1
				strict("uuuuMMdd") // 20251031
		);
		return tryParse(s, DATE_FMTS, LocalDate::from);
	}


	/** 文字列→LocalDateTime（厳密, オフセット無しの想定） */
	public static LocalDateTime toLocalDateTime(String s) {
		
		List<DateTimeFormatter> DATETIME_FMTS = List.of(
				strict("uuuu-MM-dd HH:mm:ss"),
				strict("uuuu-MM-dd HH:mm"),
				strict("uuuu/MM/dd HH:mm:ss"),
				strict("uuuu/MM/dd HH:mm"),
				strict("uuuu-MM-dd'T'HH:mm:ss"),
				strict("uuuu-MM-dd'T'HH:mm"));
		return tryParse(s, DATETIME_FMTS, LocalDateTime::from);
	}

	/** 文字列→LocalTime（厳密） */
	public static LocalTime toLocalTime(String s) {
		List<DateTimeFormatter> TIME_FMTS = List.of(
				strict("HH:mm:ss"),
				strict("HH:mm"));
		return tryParse(s, TIME_FMTS, LocalTime::from);
	}

	/**
	 * 厳密なDateTimeFormatterを作成する
	 * @param pattern パターン文字列
	 * @return DateTimeFormatter
	 */
	private static DateTimeFormatter strict(String pattern) {
		return new DateTimeFormatterBuilder()
				.parseCaseInsensitive() // 大文字・小文字を区別しない
				.appendPattern(pattern) // パターンを追加
				.toFormatter(Locale.ROOT) // ロケールをROOTに設定
				.withResolverStyle(ResolverStyle.STRICT); // 厳密な解決スタイルを使用
	}

	/**
	 * 文字列をLocalDateに変換する
	 * @param <T>
	 * @param text 変換対象文字列
	 * @param fmts フォーマット一覧
	 * @param finisher 変換後処理
	 * @return 変換結果
	 */
	private static <T> T tryParse(
			String text, List<DateTimeFormatter> fmts, Function<TemporalAccessor, T> finisher) {
		DateTimeParseException last = null;
		String s = text.trim();
		
		// フォーマットを順に試す
		// 最初に成功したものを返す
		// 全て失敗した場合は最後の例外をスローする
		for (DateTimeFormatter f : fmts) {
			try {
				return finisher.apply(f.parse(s));
			} catch (DateTimeParseException e) {
				last = e;
			}
		}
		throw last != null ? last : new DateTimeParseException("Unparseable date/time", text, 0);
	}

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
