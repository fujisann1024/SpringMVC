package com.sample.mvcApp.feature.mypage.appMember.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AppMemberModel {
	
	/*
	 * 名前
	 */
	private String fullName;
	
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
	private String birthday;
	
	/*
	 * 年齢
	 */
	private int age;

}
