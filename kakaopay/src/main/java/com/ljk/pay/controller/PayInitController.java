package com.ljk.pay.controller;

import java.util.ArrayList;
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
import com.ljk.pay.dto.UserDto;
import com.ljk.pay.service.PayRoomService;
import com.ljk.pay.service.PaySpreadService;
import com.ljk.pay.service.PayUserService;
import com.ljk.util.exception.AppException;
import com.ljk.util.response.ResultResponse;


@RestController
public class PayInitController {

	@Autowired 
	PayUserService payUserService;
	
	@Autowired 
	PayRoomService payRoomService;
	
	@Autowired 
	PaySpreadService paySpreadService;
	
	@RequestMapping(value = "/openapi/clear/all", method = RequestMethod.POST)
	public ResultResponse clearAll() throws AppException {
		String errorCode = "CODE:110";
		String errorMsg  = "OpenAPI-AllClear";
		
		// 환경을 초기화 한다.
		try {
			// 모든 사용자를 초기화 한다.
			errorCode = "CODE:111";
			deleteAllUser();
			
			// 모든 방과 참여자를 초기화 한다.
			errorCode = "CODE:112";
			deleteAllRoom();
			
			// 모든 뿌리기 정보를 초기화 한다.
			errorCode = "CODE:113";
			deleteAllSpread();
			
			errorCode = "CODE:114";
			deleteAllSpreadFriend();
			
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(null)
							.build();
	}
	
	@RequestMapping(value = "/openapi/clear/allUser", method = RequestMethod.POST)
	public ResultResponse clearAllUser() throws AppException {
		String errorCode = "CODE:150";
		String errorMsg  = "OpenAPI-AllUser";
		
		// 환경을 초기화 한다.
		try {
			// 모든 사용자를 초기화 한다.
			errorCode = "CODE:111";
			deleteAllUser();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(null)
							.build();
	}
	
	@RequestMapping(value = "/openapi/clear/allRoom", method = RequestMethod.POST)
	public ResultResponse clearAllRoom() throws AppException {
		String errorCode = "CODE:160";
		String errorMsg  = "OpenAPI-AllRoom";
		
		try {
			// 모든 방과 참여자를 초기화 한다.
			errorCode = "CODE:112";
			deleteAllRoom();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(null)
							.build();
	}
	
	@RequestMapping(value = "/openapi/clear/allSpread", method = RequestMethod.POST)
	public ResultResponse clearAllSpread() throws AppException {
		String errorCode = "CODE:170";
		String errorMsg  = "OpenAPI-AllSpread";
		
		try {
			// 모든 뿌리기 정보를 초기화 한다.
			errorCode = "CODE:113";
			deleteAllSpread();
			
			errorCode = "CODE:114";
			deleteAllSpreadFriend();
			
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(null)
							.build();
	}
	
	@RequestMapping(value = "/openapi/init/all", method = RequestMethod.POST)
	public ResultResponse initAll(@RequestHeader HttpHeaders headers, @RequestBody ParamDto paramDto) throws AppException {
		String errorCode = "CODE:120";
		String errorMsg  = "OpenAPI-Init";
		
		// 환경을 세팅한다.
		try {
			String _roomId = getRoomIdFromHead(headers);
			paramDto.setRoomId(_roomId);
			
			// 요청한 만큼 사용자를 생성한다.
			errorCode = "CODE:121";
			initAllUser(paramDto);
			
			// 사용자를 채팅방에 참여시킨다.
			errorCode = "CODE:122";
			initAllUserIntoRoom(paramDto);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(null)
							.build();
	}
	
	private void deleteAllUser() throws Exception {
		payUserService.deleteAllUser();
	}
	
	private void deleteAllRoom() throws Exception {
		payRoomService.deleteAllRoom();
	}
	
	private void deleteAllSpread() throws Exception {
		paySpreadService.deleteAllSpread();
	}
	
	private void deleteAllSpreadFriend() throws Exception {
		paySpreadService.deleteAllSpreadFriend();
	}
	
	private void initAllUser(ParamDto paramDto) throws Exception {
		int _userCnt = paramDto.getUserCnt();
		
		List<UserDto> newUsers = new ArrayList<UserDto>();
		for(int i=0; i<_userCnt; i++) {
			UserDto newUser = new UserDto(i, paramDto.getAmounts());
			newUsers.add(newUser);
		}
		payUserService.createAllUser(newUsers);
	}
	
	private void initAllUserIntoRoom(ParamDto paramDto) throws Exception {
		String _roomId = paramDto.getRoomId();
		int _userCnt = paramDto.getUserCnt();
		
		List<RoomDto> newRooms = new ArrayList<RoomDto>();
		for(int i=0; i<_userCnt; i++) {
			RoomDto newRoom = new RoomDto(_roomId, i);
			newRooms.add(newRoom);
		}
		payRoomService.createAllRoom(newRooms);
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
}
