package com.ljk.pay.mapper;

import java.util.List;

import com.ljk.pay.dto.UserDto;

public interface PayUserMapper {
	
	public void deleteAllUser() throws Exception;
	
	public boolean createAllUser(List<UserDto> dtos) throws Exception;
	
	public boolean updateUser(UserDto dto) throws Exception;
	
	public boolean changeAmount(UserDto dto) throws Exception;
	
}
