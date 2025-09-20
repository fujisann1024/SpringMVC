package com.sample.mvcApp.common.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *  Entityの基底クラス
 *  
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseEntity {
	/** 入力ユーザーＩＤ */
	private String createdBt;
	
	/** 入力日時 */
	private LocalDateTime createdAt;
	
	/** 更新ユーザーＩＤ */
	private String updatedBt;
	
	/** 更新日時 */
	private LocalDateTime updatedAt;
}
