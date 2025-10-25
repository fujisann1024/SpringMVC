package com.sample.mvcApp.common.util;

import java.util.UUID;

public class IdUtil {

	/**
	 * UUIDを生成する
	 * 
	 * @return UUID文字列
	 */
	public static String generateId() {
		return UUID.randomUUID().toString();
	}
}
