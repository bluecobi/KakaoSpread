package com.ljk.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljk.pay.dto.UserDto;
import com.ljk.pay.mapper.PayUserMapper;

@Service
public class PayUserService {
	@Autowired
	PayUserMapper payUserMapper;
	
	public void deleteAllUser() throws Exception {
		payUserMapper.deleteAllUser();
	}
	
	public boolean createAllUser(List<UserDto> dtos) throws Exception {
		return payUserMapper.createAllUser(dtos);
	}
	
	public boolean updateUser(UserDto dto) throws Exception {
		return payUserMapper.updateUser(dto);
	}
	
	public boolean changeAmount(UserDto dto) throws Exception {
		return payUserMapper.changeAmount(dto);
	}
	
}
