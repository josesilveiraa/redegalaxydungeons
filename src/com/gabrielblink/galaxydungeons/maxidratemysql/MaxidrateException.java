package com.gabrielblink.galaxydungeons.maxidratemysql;

public class MaxidrateException extends RuntimeException {

	private static final long serialVersionUID = -1746878370430655863L;

	public MaxidrateException() {
		super();
	}

	public MaxidrateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MaxidrateException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaxidrateException(String message) {
		super(message);
	}

	public MaxidrateException(Throwable cause) {
		super(cause);
	}

}
