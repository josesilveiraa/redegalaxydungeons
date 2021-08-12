package com.gabrielblink.galaxydungeons.maxidratemysql;

public class MaxidrateSQLException extends MaxidrateException {

	private static final long serialVersionUID = -7551990292204014819L;

	public MaxidrateSQLException() {
		super();
	}

	public MaxidrateSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MaxidrateSQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaxidrateSQLException(String message) {
		super(message);
	}

	public MaxidrateSQLException(Throwable cause) {
		super(cause);
	}

}
