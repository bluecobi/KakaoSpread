package com.ljk.pay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamDto {
	private int userId;
	
	private int userCnt;
	private int amounts;
	private String roomId;
	
	
	private int ownerId;
	private int spreadId;
	private String tokenId;
	private int spreadAmounts;
	private int friendCnt;
}
