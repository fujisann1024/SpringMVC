package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import com.sample.mvcApp.common.exception.DomainObjectException;

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
	
	/**
	 * ラベルからTaskStatus列挙型を取得する
	 * 
	 * @param label タスク状態ラベル
	 * @return TaskStatus列挙型
	 * @throws IllegalArgumentException 不正なタスク状態ラベルの場合
	 */
	public static TaskStatus fromLabel(String label) {
		if(label == null) {
			throw new DomainObjectException("TaskStatus label is required");
		}
		return switch (label) {
			case "未着手" -> PLANNED;
			case "進行中" -> IN_PROGRESS;
			case "完了" -> DONE;
			case "キャンセル" -> CANCELED;
			case "保留" -> ON_HOLD;
			default -> throw new DomainObjectException("Unknown label: " + label);
		};
	}
}
