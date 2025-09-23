package com.sample.mvcApp.feature.mypage.appMember.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sample.mvcApp.feature.mypage.appMember.dto.AppMemberOutDto;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberInEntity;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberOutEntity;
import com.sample.mvcApp.feature.mypage.appMember.repository.AppMemberRepository;

@ExtendWith(MockitoExtension.class)
class AppMemberServiceTest {

	@InjectMocks
	AppMemberServiceImpl service; // テスト対象

	@Mock
	AppMemberRepository repository; // 依存をモック

	@Captor
	ArgumentCaptor<AppMemberInEntity> inEntityCaptor;

	@Test
	@DisplayName("正常系: Repository結果をDTOへマップして返す & offset/limit=0/15 で呼び出す")
	void 正常系_リストが返る() {

		var entityList = List.of(
				new AppMemberOutEntity().builder()
						.memberId("111aaa")
						.email("test1@email.com")
						.password("password1")
						.familyName("familyName1")
						.givenName("givenName1")
						.prefecture("prefecture1")
						.gender("gender1")
						.birthday(LocalDate.of(2020, 1, 5))
						.joinedOn(LocalDate.of(2020, 2, 5))
						.leftOn(LocalDate.of(2020, 2, 5))
						.status("status1")
						.build(),
				new AppMemberOutEntity().builder()
						.memberId("222aaa")
						.email("test2@email.com")
						.password("password2")
						.familyName("familyName2")
						.givenName("givenName2")
						.prefecture("prefecture2")
						.gender("gender2")
						.birthday(LocalDate.of(2021, 1, 5))
						.joinedOn(LocalDate.of(2021, 2, 5))
						.leftOn(LocalDate.of(2021, 2, 5))
						.status("status2")
						.build());

		given(repository.getAppMemberOutEntityList(any(AppMemberInEntity.class)))
				.willReturn(entityList);

		// when
		List<AppMemberOutDto> result = service.getAppMemberOutEntityList();

		// then: Repository呼び出し引数（offset=0, limit=15）を検証
		then(repository).should(times(1)).getAppMemberOutEntityList(inEntityCaptor.capture());
		var captured = inEntityCaptor.getValue();
		assertThat(captured.getOffset()).isEqualTo(0);
		assertThat(captured.getLimit()).isEqualTo(15);

		// DTOへマッピングできていること（フィールド名に合わせて検証を足してください）
		assertThat(result)
				.extracting(
						AppMemberOutDto::getMemberId, AppMemberOutDto::getEmail, AppMemberOutDto::getPassword,
						AppMemberOutDto::getFamilyName, AppMemberOutDto::getGivenName, AppMemberOutDto::getPrefecture,
						AppMemberOutDto::getGender, AppMemberOutDto::getBirthday, AppMemberOutDto::getJoinedOn,
						AppMemberOutDto::getLeftOn, AppMemberOutDto::getStatus)
				.containsExactly(
						tuple(
								"111aaa",
								"test1@email.com",
								"password1",
								"familyName1",
								"givenName1",
								"prefecture1",
								"gender1",
								LocalDate.of(2020, 1, 5),
								LocalDate.of(2020, 2, 5),
								LocalDate.of(2020, 2, 5),
								"status1"),
						tuple(
								"222aaa",
								"test2@email.com",
								"password2",
								"familyName2",
								"givenName2",
								"prefecture2",
								"gender2",
								LocalDate.of(2021, 1, 5),
								LocalDate.of(2021, 2, 5),
								LocalDate.of(2021, 2, 5),
								"status2"));
	}

	@Test
	@DisplayName("境界系: Repositoryが空リストを返したら空リストを返す")
	void 空リスト() {
		// given
		given(repository.getAppMemberOutEntityList(any(AppMemberInEntity.class)))
				.willReturn(List.of());

		// when
		var result = service.getAppMemberOutEntityList();

		// then
		assertThat(result).isEmpty();
		then(repository).should(times(1)).getAppMemberOutEntityList(any(AppMemberInEntity.class));
	}

}
