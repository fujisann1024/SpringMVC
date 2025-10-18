package com.sample.mvcApp.common.util;

import java.time.OffsetDateTime;

/**
 * 日付ユーティリティ
 */
public class DateUtil {

	public static OffsetDateTime getNowOffsetDateTime() {
		return OffsetDateTime.now();
	}
}
