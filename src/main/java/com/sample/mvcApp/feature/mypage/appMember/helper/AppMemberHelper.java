package com.sample.mvcApp.feature.mypage.appMember.helper;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import com.sample.mvcApp.feature.mypage.appMember.dto.AppMemberOutDto;
import com.sample.mvcApp.feature.mypage.appMember.model.AppMemberModel;

public class AppMemberHelper {

	public static AppMemberModel convertAppMemberModel(AppMemberOutDto outDto) {
		
		var birthday = outDto.getBirthday();

		// フルネームの取得
		var fullName =   Objects.toString(outDto.getFamilyName(),"") 
				       + "　"
				       + Objects.toString(outDto.getGivenName(),"");
		
		// 誕生日の取得
		var jp = DateTimeFormatter.ofPattern("uuuu年M月d日", Locale.JAPAN);
		var strBirthday = birthday.format(jp);
		
		// 年齢の取得
		var today = LocalDate.now(ZoneId.of("Asia/Tokyo"));
		var age = Period.between(birthday, today).getYears();
		
		var model = AppMemberModel.builder()
				.fullName(fullName)
				.prefecture(outDto.getPrefecture())
				.gender(outDto.getGender())
				.birthday(strBirthday)
				.age(age)
				.build();
		
		return model;
	}

}
