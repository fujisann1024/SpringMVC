package com.sample.mvcApp.feature.mypage.appMember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sample.mvcApp.feature.mypage.appMember.helper.AppMemberHelper;
import com.sample.mvcApp.feature.mypage.appMember.service.AppMemberService;

/*
 * 会員一覧Controller
 */
@Controller
public class AppMemberListController {
	
	@Autowired
	private AppMemberService service;
	
	@GetMapping("app-member/list")
	public String GetAppMember(Model model) {
		
		var outDtoList = this.service.getAppMemberOutEntityList();
		
		var modelList = outDtoList.stream()
								.map( outDto -> AppMemberHelper.convertAppMemberModel(outDto))
								.toList();
		
		model.addAttribute("msg","タイムリーフ");
		model.addAttribute("appMembers",modelList);
		
		return "appMember/list";
	}
}
