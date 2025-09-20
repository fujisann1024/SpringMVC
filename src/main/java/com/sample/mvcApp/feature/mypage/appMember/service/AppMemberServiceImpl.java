package com.sample.mvcApp.feature.mypage.appMember.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.mvcApp.feature.mypage.appMember.dto.AppMemberOutDto;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberInEntity;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberOutEntity;
import com.sample.mvcApp.feature.mypage.appMember.repository.AppMemberRepository;

@Service
public class AppMemberServiceImpl implements AppMemberService {

	@Autowired
	private AppMemberRepository repository;

	@Override
	public List<AppMemberOutDto> getAppMemberOutEntityList() {

		var inEntity = new AppMemberInEntity().builder()
						.offset(0)
						.limit(15)
						.build();
		
		List<AppMemberOutEntity> outEntityList = this.repository.getAppMemberOutEntityList(inEntity);

		List<AppMemberOutDto> outDtoList = outEntityList.stream().map(outEntity -> {
			var outDto = new AppMemberOutDto();
			BeanUtils.copyProperties(outEntity, outDto);
			return outDto;
		}).toList();

		return outDtoList;

	}

}
