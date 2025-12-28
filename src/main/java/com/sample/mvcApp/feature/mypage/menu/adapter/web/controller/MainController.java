package com.sample.mvcApp.feature.mypage.menu.adapter.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	/**
	 * メインメニュー画面の初期表示
	 * @return メインメニュー画面
	 */
	@GetMapping("/main")
	public String Initialize() {
		return "menu/index";
	}
	
	/**
	 * ログアウト完了画面の表示
	 * @return ログアウト完了画面
	 */
	@GetMapping("/logout-success")
    public String logoutSuccess() {
        return "menu/logout";
    }
	
	/**
	 * セッションタイムアウト画面の表示
	 * @return
	 */
	@GetMapping("/session-timeout")
	public String sessionTimeout() {
		return "menu/timeout";
	}

}
