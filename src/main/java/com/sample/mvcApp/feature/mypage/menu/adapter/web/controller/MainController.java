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
	 * Spring Securityのログアウト設定により、ログアウト完了時に自動的にこの画面にリダイレクトされる。
	 * @return ログアウト完了画面
	 */
	@GetMapping("/logout-success")
    public String logoutSuccess() {
        return "menu/logout";
    }
	
	/**
	 * セッションタイムアウト画面の表示
	 * Spring Securityのセッション管理設定により、セッションタイムアウト時に自動的にこの画面にリダイレクトされる。
	 * @return セッションタイムアウト画面
	 */
	@GetMapping("/session-timeout")
	public String sessionTimeout() {
		return "menu/timeout";
	}

}
