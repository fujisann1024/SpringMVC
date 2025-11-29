package com.sample.mvcApp.common;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.sample.mvcApp.common.file.util.CSVUtilTest;
import com.sample.mvcApp.common.util.DateUtilTest;

@Suite
@SelectClasses({
	DateUtilTest.class,
	CSVUtilTest.class
})
public class AllUtilTest {

}
