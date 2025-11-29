package com.sample.mvcApp.feature.mypage.task.domain.model.transitions;

import java.util.Map;
import java.util.Set;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;

public class TaskStatusTransitions {
	
	private static final Map<TaskStatus, Set<TaskStatus>> ALLOWED;
	
	static {
		// 許可されている状態遷移を定義
		ALLOWED = Map.of(
			TaskStatus.PLANNED, Set.of(TaskStatus.IN_PROGRESS, TaskStatus.CANCELED, TaskStatus.ON_HOLD),
			TaskStatus.IN_PROGRESS, Set.of(TaskStatus.DONE, TaskStatus.CANCELED, TaskStatus.ON_HOLD),
			TaskStatus.ON_HOLD, Set.of(TaskStatus.IN_PROGRESS, TaskStatus.CANCELED),
			TaskStatus.DONE, Set.of(),
			TaskStatus.CANCELED, Set.of()
		);
	}
	
	
	/**
	 * 指定された状態遷移が許可されていることを保証します。
	 * @param from
	 * @param to
	 * @return
	 */
	 public TaskStatus ensureAllowedTransition(TaskStatus from, TaskStatus to) {
		if (!ALLOWED.getOrDefault(from, Set.of()).contains(to)) {
			
			throw new DomainObjectException(
				String.format("Invalid status transition from %s to %s", from, to)
			);
		}
		return to;
	}

}
