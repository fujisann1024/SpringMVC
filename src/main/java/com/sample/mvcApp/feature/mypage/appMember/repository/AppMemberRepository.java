package com.sample.mvcApp.feature.mypage.appMember.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberInEntity;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberOutEntity;

@Mapper
public interface AppMemberRepository {

	/**
	 * 会員情報の一覧取得
	 * @return 会員情報の一覧
	 */
	List<AppMemberOutEntity> getAppMemberOutEntityList(@Param("e") AppMemberInEntity inEntity);
}
