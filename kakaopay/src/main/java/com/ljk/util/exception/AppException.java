package com.ljk.util.exception;

public class AppException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msgCode;

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public AppException(Throwable cause, String msgCode) {
		super(msgCode, cause);
		this.msgCode = msgCode;
	}
	
	
}
