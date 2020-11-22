package com.ljk.pay.dto;

import com.ljk.util.comdto.CommonDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends CommonDto {
	private int userId;
	private int amounts;

	public UserDto() {}
	
	public UserDto(int userId, int amounts) {
		super();
		this.userId = userId;
		this.amounts = amounts;
	}
}
