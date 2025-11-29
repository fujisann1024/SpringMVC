package com.sample.mvcApp.common.file.util.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.common.file.util.value.CsvMappedRow;

public final class  CsvMappedRowCollection<T> {
	
	private final List<CsvMappedRow<T>> csvRowList;
	
	private CsvMappedRowCollection(List<CsvMappedRow<T>> csvRowList) {
		this.csvRowList = requireNonNull(csvRowList, "csvRowList is required");
	}

	/**
	 * 必須チェック
	 * @param <T>
	 * @param obj 対象オブジェクト
	 * @param message エラーメッセージ
	 * @return 対象オブジェクト
	 */
	private static <T> T requireNonNull(T obj, String message) {
	    return Optional.ofNullable(obj)
	        .orElseThrow(() -> new DomainObjectException(message));
	}
	
	public List<CsvMappedRow<T>> getCsvRowList(){
		return List.copyOf(this.csvRowList);
	}
	
	/**
	 * エラーが一つでも存在するか
	 * @return
	 */
	public boolean isError() {
		for(CsvMappedRow<T> row :this.csvRowList) {
			if(row.isError()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * エラー一覧のマップを取得
	 */
	public Map<Integer, List<String>> getAllErrorsMap() {
		Map<Integer, List<String>> errorMap = new HashMap<>();
		for(CsvMappedRow<T> row :this.csvRowList) {
			if(row.isError()) {
				errorMap.put(row.rowIndex(), row.mappingErrors());
			}
		}
		return errorMap;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param csvRowList
	 * @return
	 */
	public static <T> CsvMappedRowCollection<T> of(List<CsvMappedRow<T>> csvRowList) {
		return new CsvMappedRowCollection<T>(csvRowList);
	}

}
