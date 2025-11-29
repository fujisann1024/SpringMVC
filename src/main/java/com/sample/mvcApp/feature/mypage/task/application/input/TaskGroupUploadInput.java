package com.sample.mvcApp.feature.mypage.task.application.input;

import java.util.List;

public record TaskGroupUploadInput(
		List<TaskGroupCreateInput> inputList
		) {

}
