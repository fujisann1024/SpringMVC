package com.sample.mvcApp.feature.mypage.appMember.entity;

import java.time.LocalDate;

import com.sample.mvcApp.common.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/*
 * 会員情報OutEntity
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AppMemberOutEntity extends BaseEntity {

	/*
	 * 会員ID
	 */
	private String memberId;

	/*
	 * Eメール
	 */
	private String email;
	
	/*
	 * パスワード
	 */
	private String password;
	
	/*
	 * 姓
	 */
	private String familyName;
	
	/*
	 * 名
	 */
	private String givenName;
	
	/*
	 * 都道府県
	 */
	private String prefecture;
	
	/*
	 * 性別 
	 */
	private String gender;

	/*
	 * 誕生日 
	 */
	private LocalDate birthday;

	/*
	 * 入会日 
	 */
	private LocalDate joinedOn;

	/*
	 * 退会日 
	 */
	private LocalDate leftOn;

	/*
	 * 会員ステータス 
	 */
	private String status;
}
