package com.sample.mvcApp.common.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *  Dtoの基底クラス
 *  
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public abstract class BaseDto {
	/** 入力ユーザーＩＤ */
	private String createdBy;
	
	/** 入力日時 */
	private OffsetDateTime createdAt;
	
	/** 更新ユーザーＩＤ */
	private String updatedBy;
	
	/** 更新日時 */
	private OffsetDateTime updatedAt;
}
