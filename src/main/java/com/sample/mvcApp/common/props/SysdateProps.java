package com.sample.mvcApp.common.props;

import java.time.LocalDate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "todo.sysdate")
@Getter
@Setter
public class SysdateProps {
	
	private LocalDate value;
    private boolean useflag;


}
