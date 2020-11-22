package com.ljk.util.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResultResponse {
	private boolean success;
	private String message;
	private Object result;
	
	@Builder
	public ResultResponse(boolean success, String message, Object result) {
		this.success = success;
		this.message = message;
		this.result = result;
	}
}
