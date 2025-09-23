package com.sample.mvcApp.feature.mypage.appMember.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberInEntity;
import com.sample.mvcApp.feature.mypage.appMember.entity.AppMemberOutEntity;


@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // ← 組み込みDBへの差し替えを禁止
@DBRider
@DBUnit(
		caseInsensitiveStrategy = Orthography.LOWERCASE, 
		qualifiedTableNames = true) // or CaseInsensitiveStrategy.LOWERCASE
class AppMemberRepositoryTest {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AppMemberRepository repo;
	
	// DbRider に JDBC を渡す
	private final ConnectionHolder connectionHolder = () -> dataSource.getConnection();
	
	private final String testPath = "com/sample/mvcApp/feature/mypage/appMember/repository/";

	  @Test
	  @DisplayName("レコード取得 複数件")
	  @DataSet(
	      value = testPath + "Test_1.xlsx",  // Excelファイル
	      cleanBefore = true                   // 事前にテーブルをTRUNCATE
	  )
	  void testGetAppMemberOutEntityList() {
		  
		var entity = new AppMemberInEntity().builder().limit(4).offset(2).build();
		  
	    List<AppMemberOutEntity> list = repo.getAppMemberOutEntityList(entity);
	    
	    assertThat(list)
	    .extracting(
	    		AppMemberOutEntity::getFamilyName
	    	  , AppMemberOutEntity::getGivenName
	    	  , AppMemberOutEntity::getPrefecture
	    	  , AppMemberOutEntity::getGender
	    	  , AppMemberOutEntity::getBirthday
	    		)
	    .containsExactly(
	        tuple("高橋","光","愛知県","その他",LocalDate.of(2001, 3, 18)),
	        tuple("井上","大和","長野県","男性",LocalDate.of(1988, 4, 3)),
	        tuple("佐々木","駿","宮城県","男性",LocalDate.of(1992, 1, 19)),
	        tuple("渡辺","美咲","埼玉県","女性",LocalDate.of(1985, 12, 5))
	    );
	    
	  }

}
