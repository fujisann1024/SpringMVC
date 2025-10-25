package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import java.time.LocalDate;
import java.util.Objects;

import com.sample.mvcApp.common.exception.DomainObjectException;

/**
 * タスクグループID
 * 
 * <pre>
 *  複合主キーのため、値オブジェクトとして定義
 * </pre>
 * 
 * @param groupId タスクグループID
 * @param workYmd 実施年月日
 */
public record TaskGroupId(String groupId, LocalDate workYmd) {
	
	/**
	 * コンストラクタ
	 * 
	 * @param groupId タスクグループID
	 * @param workYmd 実施年月日
	 * @throws DomainObjectException 不正な値オブジェクトの場合
	 */
	public TaskGroupId {
		if (Objects.isNull(groupId) || groupId.isBlank()) {
			throw new DomainObjectException("TaskGroupId:グループタスクIDは必須です");
		}
		if (Objects.isNull(workYmd)) {
			throw new DomainObjectException("TaskGroupId:実施年月日は必須です");
		}
	}

}
