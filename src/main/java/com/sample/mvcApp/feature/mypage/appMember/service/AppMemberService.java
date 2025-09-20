package com.sample.mvcApp.feature.mypage.appMember.service;

import java.util.List;

import com.sample.mvcApp.feature.mypage.appMember.dto.AppMemberOutDto;

public interface AppMemberService {

	/**
	 * 会員一覧取得
	 * @return
	 */
	List<AppMemberOutDto> getAppMemberOutEntityList();
}
