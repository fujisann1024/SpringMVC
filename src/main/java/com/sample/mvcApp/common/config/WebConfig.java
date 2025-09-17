package com.sample.mvcApp.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	 @Override
	  public void configurePathMatch(PathMatchConfigurer configurer) {
	    // com.sample.mvcApp.feature.mypageパッケージ配下のControllerにだけ /mypage を付与
	    configurer.addPathPrefix("/mypage",
	        HandlerTypePredicate.forBasePackage("com.sample.mvcApp.feature.mypage"));
	  }
}
