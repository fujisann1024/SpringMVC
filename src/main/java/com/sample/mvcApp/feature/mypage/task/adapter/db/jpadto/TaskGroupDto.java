package com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto;

import java.time.LocalTime;
import java.time.OffsetDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "task_group", schema = "todo")
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskGroupDto{
	
	@EmbeddedId
	private TaskGroupKeyDto id;

	/**
	 * 	タイトル
	 */
	private String title;

	/**
	 * 	説明
	 */
	private String description;

	/**
	 * 	タスク種別
	 */
	private String taskTypeCode;

	/**
	 * 	優先度
	 */
	private String priority;

	/**
	 * 	予定開始時分秒
	 */
	private LocalTime plannedStartTime;

	/**
	 * 	予定終了時分秒
	 */
	private LocalTime plannedEndTime;

	/**
	 * 当日開始時分秒
	 */
	private LocalTime actualStartTime;

	/**
	 * 当日終了時分秒
	 */
	private LocalTime actualEndTime;

	/**
	 * ステータス
	 */
	private String status;
	
	/**
	 * テンプレートフラグ
	 */
	private boolean isTemplate;
	
	/** 入力ユーザーＩＤ */
	private String createdBy;
	
	/** 入力日時 */
	private OffsetDateTime createdAt;
	
	/** 更新ユーザーＩＤ */
	private String updatedBy;
	
	/** 更新日時 */
	private OffsetDateTime updatedAt;

}
