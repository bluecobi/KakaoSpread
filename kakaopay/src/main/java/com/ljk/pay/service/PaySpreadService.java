package com.ljk.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljk.pay.dto.SpreadDto;
import com.ljk.pay.dto.SpreadFriendDto;
import com.ljk.pay.mapper.PaySpreadMapper;

@Service
public class PaySpreadService {
	@Autowired
	PaySpreadMapper paySpreadMapper;
	
	public void deleteAllSpread() throws Exception {
		paySpreadMapper.deleteAllSpread();
	}
	
	public void deleteAllSpreadFriend() throws Exception {
		paySpreadMapper.deleteAllSpreadFriend();
	}
	
	public boolean createSpread(SpreadDto dto) throws Exception {
		return paySpreadMapper.createSpread(dto);
	}
	
	public boolean createSpreadDtl(SpreadDto dto) throws Exception {
		return paySpreadMapper.createSpreadDtl(dto);
	}
	
	public SpreadDto getSpread(SpreadDto dto) throws Exception {
		return paySpreadMapper.getSpread(dto);
	}
	
	public boolean takeMoney(SpreadFriendDto dto) throws Exception {
		return paySpreadMapper.takeMoney(dto);
	}
	
	public SpreadFriendDto getSpreadFriend(SpreadFriendDto dto) throws Exception {
		return paySpreadMapper.getSpreadFriend(dto);
	}
	
	public List<SpreadFriendDto> selectSpreadFriend(SpreadFriendDto dto) throws Exception {
		return paySpreadMapper.selectSpreadFriend(dto);
	}
	
	public int checkTakeCount(SpreadDto dto) throws Exception {
		return paySpreadMapper.checkTakeCount(dto);
	}
}
