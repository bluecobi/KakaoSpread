package com.ljk.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljk.pay.dto.RoomDto;
import com.ljk.pay.mapper.PayRoomMapper;

@Service
public class PayRoomService {
	@Autowired
	PayRoomMapper payRoomMapper;
	
	public void deleteAllRoom() throws Exception {
		payRoomMapper.deleteAllRoom();
	}
	
	public boolean createAllRoom(List<RoomDto> dtos) throws Exception {
		return payRoomMapper.createAllRoom(dtos);
	}
	
	public List<RoomDto> selectRoom(RoomDto dto) throws Exception {
		return payRoomMapper.selectRoom(dto);
	}
	
}
