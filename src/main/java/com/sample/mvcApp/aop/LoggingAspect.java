package com.sample.mvcApp.aop;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		return logExecution(joinPoint, "Controller");
	}

	@Around("within(@org.springframework.stereotype.Service *)")
	public Object logServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		return logExecution(joinPoint, "Service");
	}

	@Around("within(@org.springframework.stereotype.Repository *)")
	public Object logRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		return logExecution(joinPoint, "Repository");
	}

	private Object logExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
		String signature = joinPoint.getSignature().toShortString();
		String args = Arrays.stream(joinPoint.getArgs())
				.map(arg -> arg == null ? "null" : arg.toString())
				.collect(Collectors.joining(", "));
		long start = System.nanoTime();
		logger.info("[{}] start {} args=[{}]", layer, signature, args);
		try {
			Object result = joinPoint.proceed();
			long elapsedMs = (System.nanoTime() - start) / 1_000_000;
			logger.info("[{}] end {} elapsedMs={}", layer, signature, elapsedMs);
			return result;
		} catch (Throwable ex) {
			long elapsedMs = (System.nanoTime() - start) / 1_000_000;
			logger.error("[{}] error {} elapsedMs={} message={}", layer, signature, elapsedMs, ex.getMessage(), ex);
			throw ex;
		}
	}
}