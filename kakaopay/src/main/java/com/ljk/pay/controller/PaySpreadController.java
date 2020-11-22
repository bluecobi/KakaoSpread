package com.ljk.pay.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ljk.pay.dto.ParamDto;
import com.ljk.pay.dto.RoomDto;
import com.ljk.pay.dto.SpreadDto;
import com.ljk.pay.dto.UserDto;
import com.ljk.pay.service.PayRoomService;
import com.ljk.pay.service.PayService;
import com.ljk.pay.service.PaySpreadService;
import com.ljk.pay.service.PayUserService;
import com.ljk.util.digest.TokenUtil;
import com.ljk.util.exception.AppException;
import com.ljk.util.response.ResultResponse;


@RestController
public class PaySpreadController {

	@Autowired 
	PayService payService;
	
	@Autowired 
	PayUserService payUserService;
	
	@Autowired 
	PayRoomService payRoomService;
	
	@Autowired 
	PaySpreadService paySpreadService;
	
	@RequestMapping(value = "/openapi/spreadMoney", method = RequestMethod.POST)
	public ResultResponse spreadMoney(@RequestHeader HttpHeaders headers, @RequestBody ParamDto paramDto) throws AppException {
		String errorCode = "CODE:200";
		String errorMsg  = "OpenAPI-Spread";
		
		String tokenId = "";
		
		try {
			validationParam(paramDto);
			
			int _userId = getUserIdFromHead(headers);
			String _roomId = getRoomIdFromHead(headers);
			
			paramDto.setOwnerId(_userId);
			paramDto.setRoomId(_roomId);
			
			// Hash값을 이용해서 Token을 생성한다.
			errorCode = "CODE:201";
			tokenId = getToken("KakaoPay");
			
			// 뿌리기에 대한 Master 정보 저장 (N 금액을 M 명에서 뿌린다. N X M)
			errorCode = "CODE:211";
			paramDto.setTokenId(tokenId);
			int newSpreadId = createSpreadMaster(paramDto);
			
			// 뿌린 금액 만큼 차감한다. (단, 뿌린 대상 보다 방에 참여한 사람이 적으면 참여자을 고려해서 차감한다.)
			errorCode = "CODE:212";
			updataAmountUser(paramDto);
			
			// 방에 참여한 사용자에게 뿌릴 정보를 저장한다.
			errorCode = "CODE:213";
			paramDto.setSpreadId(newSpreadId);
			createSpreadDetail(paramDto);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(tokenId)
							.build();
	}
	
	private String getToken(String seedKey) throws Exception {
		Date now = new Date();
		String tokenId = "";
		
		tokenId = now.toString() + seedKey;
		tokenId = TokenUtil.md5(tokenId.hashCode()+"");
		tokenId = tokenId.replaceAll("[0-9]","");
		tokenId = tokenId.substring(0, 3);
		
		return tokenId;
	}
	
	private int createSpreadMaster(ParamDto paramDto) throws Exception {
		String _roomId = paramDto.getRoomId();
		int _spreadAmount = paramDto.getSpreadAmounts();
		int _friendCnt = paramDto.getFriendCnt();
		int _ownerId = paramDto.getOwnerId();
		String tokenId = paramDto.getTokenId();
		
		int maxSpreadId = payService.getMaxSequenceId("SPREAD");
		SpreadDto spreadDto = new SpreadDto(maxSpreadId, _ownerId, _roomId, tokenId, _spreadAmount, _friendCnt);
		
		paySpreadService.createSpread(spreadDto);
		
		return maxSpreadId;
	}
	
	private void createSpreadDetail(ParamDto paramDto) throws Exception {
		String _roomId = paramDto.getRoomId();
		int spreadId = paramDto.getSpreadId();
		int _spreadAmount = paramDto.getSpreadAmounts();
		int _ownerId = paramDto.getOwnerId();
		
		SpreadDto spreadDto = new SpreadDto(spreadId, _ownerId, _roomId, null, _spreadAmount, -1);
		paySpreadService.createSpreadDtl(spreadDto);
	}
	
	private void updataAmountUser(ParamDto paramDto) throws Exception {
		String _roomId = paramDto.getRoomId();
		int _spreadAmount = paramDto.getSpreadAmounts();
		int _friendCnt = paramDto.getFriendCnt();
		int _ownerId = paramDto.getOwnerId();
		
		// 금액 배분 하기 
		/* 뿌릴 인원보다 방에 참여한 참가자가 적은 경우을 고려한다.*/
		// 1. 주어진 방에 뿌리기를 시도한 당사자를 제외한 인원을 파악한다.
		RoomDto roomDto = new RoomDto(_roomId, _ownerId);
		List<RoomDto> partys = payRoomService.selectRoom(roomDto);
		
		if(partys != null && partys.size() > 0) {
			// 뿌리려는 대상보다 방에 참여한 참여자가 적은 경우
			if(partys.size() < _friendCnt) {
				_friendCnt = partys.size();
			}
		}
		
		// 돈을 뿌린 사용자의 금액을 차감한다.
		UserDto userDto = new UserDto(_ownerId, _spreadAmount * _friendCnt * -1);
		payUserService.changeAmount(userDto);
	}
	
	private int getUserIdFromHead(HttpHeaders headers) throws Exception {
		String errorCode = "CODE:291";
		String errorMsg  = "OpenAPI-Spread Money-GetUserId From Header";
		
		List<String> _userIds = headers.get("X-USER-ID");
		int _userId = -1;
		
		if(_userIds != null && _userIds.size() > 0 && _userIds.get(0).length() > 0) {
			_userId = Integer.parseInt(headers.get("X-USER-ID").get(0));
		}
		else {
			errorCode = "CODE:292";
			errorMsg = "사용자 정보가 없습니다.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return _userId;
	}
	
	private String getRoomIdFromHead(HttpHeaders headers) throws Exception {
		String errorCode = "CODE:293";
		String errorMsg  = "OpenAPI-Spread Money-GetRoomId From Header";
		
		List<String> _roomIds = headers.get("X-ROOM-ID");
		String _roomId;
		
		if(_roomIds != null && _roomIds.size() > 0 && _roomIds.get(0).length() > 0) {
			_roomId = _roomIds.get(0);
		}
		else {
			errorCode = "CODE:294";
			errorMsg = "방 정보가 없습니다.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return _roomId;
	}
	
	private void validationParam(ParamDto paramDto) throws Exception {
		String errorCode = "CODE:250";
		String errorMsg  = "OpenAPI-Spread Money-Validation";
		
		if(paramDto.getSpreadAmounts() <= 0) {
			errorCode = "CODE:251";
			errorMsg = "마이너스 금액은 뿌릴수가 없습니다.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
		else if (paramDto.getFriendCnt() <= 0) {
			errorCode = "CODE:252";
			errorMsg = "대상을 0보다 큰 값으로 설정하세요.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
	}
	
}
