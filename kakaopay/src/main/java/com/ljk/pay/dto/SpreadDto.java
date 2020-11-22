package com.ljk.pay.dto;

import java.util.List;

import com.ljk.util.comdto.CommonDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpreadDto extends CommonDto {
	private int spreadId;
	private int ownerId;
	private String roomId;
	private String tokenId;
	private int spreadAmounts;
	private int friendCnt;
	private int takenTotalAmount;
	private int diffMin;
	
	private List<SpreadFriendDto> stDtos;
	
	public SpreadDto() {}
	
	public SpreadDto(int spreadId, int ownerId, String roomId, String tokenId, int spreadAmounts, int friendCnt) {
		super();
		this.spreadId = spreadId;
		this.ownerId = ownerId;
		this.roomId = roomId;
		this.tokenId = tokenId;
		this.spreadAmounts = spreadAmounts;
		this.friendCnt = friendCnt;
	}
}
