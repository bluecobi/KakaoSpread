package com.ljk.pay.mapper;

import java.util.List;

import com.ljk.pay.dto.SpreadDto;
import com.ljk.pay.dto.SpreadFriendDto;

public interface PaySpreadMapper {
	
	public void deleteAllSpread() throws Exception;
	
	public void deleteAllSpreadFriend() throws Exception;
	
	public boolean createSpread(SpreadDto dto) throws Exception;
	
	public boolean createSpreadDtl(SpreadDto dto) throws Exception;
	
	public SpreadDto getSpread(SpreadDto dto) throws Exception;
	
	public boolean takeMoney(SpreadFriendDto dto) throws Exception;
	
	public SpreadFriendDto getSpreadFriend(SpreadFriendDto dto) throws Exception;
	
	public List<SpreadFriendDto> selectSpreadFriend(SpreadFriendDto dto) throws Exception;
}
