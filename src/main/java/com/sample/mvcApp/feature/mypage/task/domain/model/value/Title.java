package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import com.sample.mvcApp.common.exception.DomainObjectException;

public record Title( String value) {
	
	public Title {
		if (value == null || value.isEmpty()) {
			throw new DomainObjectException("タイトルは必須です");
		}
		if (value.length() > 100) {
			throw new DomainObjectException("タイトルは100文字以内で入力してください");
		}
	}

}
