package com.sample.mvcApp.common.file.util.row;

import java.util.List;

/**
 * CSVの1行分を変換した結果。
 */
public record CsvMappedRow<T>(
		/** 行番号*/
		int rowIndex
		, T payload
		/**エラーメッセージ*/
		, List<String> mappingErrors
		) {

	/**
	 * エラー存在フラグ
	 * */
    public boolean isError() {
        return mappingErrors != null && !mappingErrors.isEmpty();
    }
}
