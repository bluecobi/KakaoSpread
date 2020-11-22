package com.ljk.pay.mapper;

import java.util.List;

import com.ljk.pay.dto.RoomDto;

public interface PayRoomMapper {
	
	public void deleteAllRoom() throws Exception;
	
	public boolean createAllRoom(List<RoomDto> dtos) throws Exception;
	
	public List<RoomDto> selectRoom(RoomDto dto) throws Exception;
	
}
