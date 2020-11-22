package com.ljk.pay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ljk.pay.dto.ParamDto;
import com.ljk.pay.dto.SpreadDto;
import com.ljk.pay.dto.SpreadFriendDto;
import com.ljk.pay.dto.UserDto;
import com.ljk.pay.service.PaySpreadService;
import com.ljk.pay.service.PayUserService;
import com.ljk.util.exception.AppException;
import com.ljk.util.response.ResultResponse;


@RestController
public class PayTakeController {

	@Autowired 
	PayUserService payUserService;
	
	@Autowired 
	PaySpreadService paySpreadService;
	
	@RequestMapping(value = "/openapi/takeMoney", method = RequestMethod.POST)
	public ResultResponse takeMoney(@RequestHeader HttpHeaders headers, @RequestBody ParamDto paramDto) throws AppException {
		String errorCode = "CODE:300";
		String errorMsg  = "OpenAPI-Take Money";
		
		String _tokenId = paramDto.getTokenId();
		int distributeMoney = -1;
		
		try {
			int _userId = getUserIdFromHead(headers);
			String _roomId = getRoomIdFromHead(headers);
			
			// RoomID와 TokenID를 이용해서 Spread된 정보를 구한다.
			errorCode = "CODE:311";
			SpreadDto spreadDto = new SpreadDto();
			spreadDto.setRoomId(_roomId);
			spreadDto.setTokenId(_tokenId);
			spreadDto.setOwnerId(-1);
			
			spreadDto = paySpreadService.getSpread(spreadDto);
			
			// 뿌리기 정보가 있으면 상세 정보를 변경하고 개인 금액을 변경한다.
			if(spreadDto != null && spreadDto.getSpreadId() > -1) {
				// 뿌리는 대상자보다 많은 사람이 가져갈 수 없다.
				checkTakeCount(spreadDto);
				
				errorCode = "CODE:312";
				SpreadFriendDto sfDto = new SpreadFriendDto(spreadDto.getSpreadId(), _userId, _roomId, _tokenId, 0, "N");
				boolean successYn = paySpreadService.takeMoney(sfDto);
				
				sfDto = paySpreadService.getSpreadFriend(sfDto);
				if(sfDto == null) {
					errorCode = "CODE:313";
					errorMsg = "금액을 수령 할 대상이 아닙니다.";
					throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
				}
				
				if(successYn) {
					distributeMoney = sfDto.getAllocatedAmount();
					
					// 수령 한 금액을 자신의 금액에 더한다.
					UserDto userDto = new UserDto(_userId, distributeMoney);
					payUserService.changeAmount(userDto);
				}
				else {
					// 실패했을 경우 원인을 확인해서 사용자에게 알려준다.
					if("Y".equals(sfDto.getGetYn())) {
						errorCode = "CODE:314";
						errorMsg = "이미 금액을 수령했습니다.";
						throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
					}
					else if (sfDto.getDiffSec() > (60*10)) {
						errorCode = "CODE:315";
						errorMsg = "금액을 수령 할 수 있는 시간이 초과되었습니다.";
						throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
					}
				}
			}
			else {
				errorCode = "CODE:316";
				errorMsg = "받을 정보가 없습니다.";
				throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
			}
			
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(distributeMoney)
							.build();
	}
	
	private int getUserIdFromHead(HttpHeaders headers) throws Exception {
		String errorCode = "CODE:391";
		String errorMsg  = "OpenAPI-Take Money-GetUserId From Header";
		
		List<String> _userIds = headers.get("X-USER-ID");
		int _userId = -1;
		
		if(_userIds != null && _userIds.size() > 0 && _userIds.get(0).length() > 0) {
			_userId = Integer.parseInt(headers.get("X-USER-ID").get(0));
		}
		else {
			errorCode = "CODE:392";
			errorMsg = "사용자 정보가 없습니다.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return _userId;
	}
	
	private String getRoomIdFromHead(HttpHeaders headers) throws Exception {
		String errorCode = "CODE:393";
		String errorMsg  = "OpenAPI-Take Money-GetRoomId From Header";
		
		List<String> _roomIds = headers.get("X-ROOM-ID");
		String _roomId;
		
		if(_roomIds != null && _roomIds.size() > 0 && _roomIds.get(0).length() > 0) {
			_roomId = _roomIds.get(0);
		}
		else {
			errorCode = "CODE:394";
			errorMsg = "방 정보가 없습니다.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return _roomId;
	}
	
	private void checkTakeCount(SpreadDto spreadDto) throws Exception {
		String errorCode = "CODE:395";
		String errorMsg  = "머니가 모두 소진되었습니다. 머니를 수령하지 못했습니다.";
		
		if(paySpreadService.checkTakeCount(spreadDto) == 0) {
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
	}
}
