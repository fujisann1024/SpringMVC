package com.sample.mvcApp.feature.mypage.task.domain.model.aggregate;

import java.util.Optional;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupKeyDto;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;

/**
 * タスクグループ（集約根）
 * 
 * 複数のタスクをまとめるための集約根エンティティ
 */
public final class TaskGroup {

	private final TaskGroupId id;
	private Title title;
	private String description;
	private String taskTypeCode;
	private Priority priority;
	private TimeSlot plannedTime;
	private TimeSlot actualTime;
	private TaskStatus status;
	private boolean template;

	/*
	 * コンストラクタ
	 * 
	 * @param id            タスクグループID(必須)
	 * @param title         タイトル(必須)
	 * @param description   説明
	 * @param taskTypeCode  タスク種別コード(必須)
	 * @param priority      優先度(必須)
	 * @param plannedTime   予定時間
	 * @param actualTime    実績時間
	 * @param status        状態
	 * @param template      テンプレートフラグ
	 */
	private TaskGroup(
			TaskGroupId id,
			Title title,
			String description,
			String taskTypeCode,
			Priority priority,
			TimeSlot plannedTime,
			TimeSlot actualTime,
			TaskStatus status,
			boolean template) {
		this.id = Optional.ofNullable(id)
				.orElseThrow(() -> new DomainObjectException("TaskGroupId is required"));
		this.title = Optional.ofNullable(title)
				.orElseThrow(() -> new DomainObjectException("Title is required"));
		this.description = description;
		this.taskTypeCode = taskTypeCode;
		this.priority = Optional.ofNullable(priority)
				.orElseThrow(() -> new DomainObjectException("Priority is required"));
		this.plannedTime = plannedTime;
		this.actualTime = actualTime;
		this.status = status;
		this.template = template;
	}

	/**
	 * タスクグループ取得
	 * ファクトリーメソッド
	 * 
	 * @param id            タスクグループID
	 * @param title         タイトル
	 * @param description   説明
	 * @param taskTypeCode  タスク種別コード
	 * @param priority      優先度
	 * @param planned       予定時間
	 * @return              タスクグループ集約根
	 *
	 */
	public static TaskGroup newCreate(
			TaskGroupId id
			,Title title
			,String description
			,String taskTypeCode
			,Priority priority
			,TimeSlot planned
			) {
		
		var agg = new TaskGroup(
				 id
				,title
				,description
				,taskTypeCode
				,priority
				,planned
				,null,
				TaskStatus.PLANNED, false);
		return agg;
	}

	public TaskGroupDto toDto() {
		return TaskGroupDto.builder()
				.id(TaskGroupKeyDto.builder()
						.taskGroupId(this.id.groupId())
						.workYmd(this.id.workYmd())
						.build())
				.title(this.title.value())
				.description(this.description)
				.taskTypeCode(this.taskTypeCode)
				.priority(this.priority.getLabel())
				.plannedStartTime(Optional.ofNullable(this.plannedTime)
										  .map(TimeSlot::startTime)
										  .orElse(null))
				.plannedEndTime(Optional.ofNullable(this.plannedTime)
										.map(TimeSlot::endTime)
										.orElse(null))
				.actualStartTime(Optional.ofNullable(this.actualTime)
										.map(TimeSlot::startTime)
										.orElse(null))
				.actualEndTime(Optional.ofNullable(this.actualTime)
									  .map(TimeSlot::endTime)
									  .orElse(null))
				.status(Optional.ofNullable(this.status)
						        .map(TaskStatus::getLabel)
						        .orElse(null))
				.isTemplate(this.template)
				.build();
	}

}
