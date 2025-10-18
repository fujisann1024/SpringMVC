package com.sample.mvcApp.feature.mypage.task.domain.model.value;

/**
 * タスクの状態
 */
public enum  TaskStatus {
	 /** 状態：未着手 */
	 PLANNED("未着手"), 
	 /** 状態：進行中 */
	 IN_PROGRESS("進行中"), 
	 /** 状態：完了 */
	 DONE("完了"), 
	 /** 状態：キャンセル */
	 CANCELED("キャンセル"), 
	 /** 状態：保留 */
	 ON_HOLD("保留");
	
	private final String label;

	TaskStatus(String label) {
		this.label = label; // まずは素直に代入だけ
	}

	public String getLabel() {
		return label;
	}
}
