package com.sample.mvcApp.security.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.sample.mvcApp.security.service.LoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginService userDetailsService;

    public SecurityConfig(LoginService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

	/**
	 * セキュリティフィルターチェーンの設定
	 * @param http
	 * @return
	 * @throws Exception
	 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            	// 静的リソースと特定のパスは認証不要とする
                .requestMatchers(
                		  "/css/**"
                		, "/js/**"
                		, "/images/**"
                		, "/webjars/**"
                		, "/"
                		, "/register/**"
                		, "/mypage/logout-success"
                		,"/mypage/session-timeout"
                		).permitAll()
                // その他のリクエストは認証を要求
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
            	 // カスタムログインページの設定
                .loginPage("/")
                 // ログイン処理のURL設定
                .loginProcessingUrl("/login")
                 // ログイン成功時のリダイレクト先設定
                .defaultSuccessUrl("/mypage/main", true)
                 // ログイン失敗時のリダイレクト先設定
                .failureUrl("/?error")
                 // ログインページは全ユーザーに許可
                .permitAll()
            )
            .logout(logout -> logout
            	// サーバー側のセッション破棄	
                .invalidateHttpSession(true)
                // クッキー情報の削除
                .deleteCookies("JSESSIONID")
            	// ログアウト完了後のリダイレクト先 ( POST /logout で画面側から実行)
                .logoutSuccessUrl("/mypage/logout-success")
                 // ログアウトは全ユーザーに許可
                .permitAll()
            )
            .userDetailsService(userDetailsService)
            .sessionManagement(session -> session
            	// セッションタイムアウト時の遷移先URL設定
                .invalidSessionUrl("/mypage/session-timeout")
            	// 同一ユーザーの同時セッション数を1に制限
                .maximumSessions(1)
                 // 上限に達したら新規ログインを拒否
                .maxSessionsPreventsLogin(true)
            );

        return http.build();
    }
    
    /**
     * セッションイベントリスナーのBean定義
     * 
     * SessionRegistry( どのユーザーが、どのセッション（JSESSIONID）でログインしているか)
     * にセッションの生成、削除イベントを通知するために必要
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    /**
	 * パスワードエンコーダーのBean定義
	 * @return
	 */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}