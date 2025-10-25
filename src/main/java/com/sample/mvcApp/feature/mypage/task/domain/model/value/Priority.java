package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import com.sample.mvcApp.common.exception.DomainObjectException;

/** 優先度（ラベルのみ） */
public enum Priority {
	
	/** 優先度：高 */
    HIGH("高"),
    /** 優先度：中 */
    MEDIUM("中"),
    /** 優先度：低 */
    LOW("低");

    private final String label;

    Priority(String label) {
        this.label = label; // まずは素直に代入だけ
    }

    /*
     * 優先度ラベルを取得する
     */
    public String getLabel() {
		return label;
	}
    
    /**
	 * 優先度コードからPriority列挙型を取得する
	 * 
	 * @param priority 優先度コード
	 * @return Priority列挙型
	 * @throws DomainObjectException 不正な優先度コードの場合
	 */
    public static Priority parsePriority(String priority) {
        if (priority == null) throw new DomainObjectException("Priority code is required");
        return switch (priority) {
            case "高" -> HIGH;
            case "中" -> MEDIUM;
            case "低" -> LOW;
            default -> throw new DomainObjectException("Unknown code: " + priority);
        };
    }

}
