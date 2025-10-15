package com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TaskGroupInDto {
	
	/**
	 * タスクグループID
	 */
	private String taskGroupId; 
	
	/**
	 *  実施年月日
	 */
	private LocalDate workYmd; 
}
