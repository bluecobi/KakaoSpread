package com.ljk.pay.dto;

import com.ljk.util.comdto.CommonDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto extends CommonDto {
	private String roomId;
	private int userId;
	
	public RoomDto() {}
	
	public RoomDto(String roomId, int userId) {
		this.roomId = roomId;
		this.userId = userId;
	}
}
