package com.ljk.pay.dto;

import com.ljk.util.comdto.CommonDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpreadFriendDto extends CommonDto {
	private int spreadId;
	private int friendId;
	private String roomId;
	private String tokenId;
	private int allocatedAmount;
	private String getYn;
	private int diffSec;
	
	public SpreadFriendDto() {}

	public SpreadFriendDto(int spreadId, int friendId, String roomId, String tokenId, int allocatedAmount, String getYn) {
		super();
		this.spreadId = spreadId;
		this.friendId = friendId;
		this.roomId = roomId;
		this.tokenId = tokenId;
		this.allocatedAmount = allocatedAmount;
		this.getYn = getYn;
	}
	
}
