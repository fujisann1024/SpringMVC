package com.sample.mvcApp.feature.mypage.appMember.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AppMemberController {
	
	@GetMapping("app-member")
	public String GetAppMember(Model model) {
		model.addAttribute("msg","タイムリーフ");
		return "member/list";
	}
}
