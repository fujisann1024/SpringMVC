package com.sample.mvcApp.common.file.exception;

public class FileImportException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public FileImportException(String message) {
        super(message);
    }

    public FileImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
