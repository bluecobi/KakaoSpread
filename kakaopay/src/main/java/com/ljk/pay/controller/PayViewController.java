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
import com.ljk.pay.service.PaySpreadService;
import com.ljk.util.exception.AppException;
import com.ljk.util.response.ResultResponse;


@RestController
public class PayViewController {

	@Autowired 
	PaySpreadService paySpreadService;
	
	@RequestMapping(value = "/openapi/viewSpread", method = RequestMethod.POST)
	public ResultResponse viewSpread(@RequestHeader HttpHeaders headers, @RequestBody ParamDto paramDto) throws AppException {
		String errorCode = "CODE:400";
		String errorMsg  = "OpenAPI-View Spread Money";
		
		int _userId = -1; 
		String _tokenId = paramDto.getTokenId();
		SpreadDto spreadDto = new SpreadDto();
		
		try {
			_userId = getUserIdFromHead(headers);
			
			// Spread된 상세 정보를 구한다.
			errorCode = "CODE:401";
			spreadDto.setOwnerId(_userId);
			spreadDto.setTokenId(_tokenId);
			
			spreadDto = paySpreadService.getSpread(spreadDto);
			
			if(spreadDto == null) {
				errorCode = "CODE:401";
				errorMsg = "조회 권한이 없습니다.";
				throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
			}
			else if(spreadDto.getDiffMin() > 60*24*7) {
				errorCode = "CODE:402";
				errorMsg = "조회 기간이 지났습니다.";
				throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
			}
			else {
				errorCode = "CODE:403";
				SpreadFriendDto sfDto = new SpreadFriendDto();
				sfDto.setSpreadId(spreadDto.getSpreadId());
				sfDto.setGetYn("Y");
				List<SpreadFriendDto> sfDtos = paySpreadService.selectSpreadFriend(sfDto);
				spreadDto.setStDtos(sfDtos);
				System.out.println("");
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			throw new AppException(e,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return ResultResponse.builder()
							.success(true)
							.result(spreadDto)
							.build();
	}
	
	private int getUserIdFromHead(HttpHeaders headers) throws Exception {
		String errorCode = "CODE:491";
		String errorMsg  = "OpenAPI-Take Money-GetUserId From Header";
		
		List<String> _userIds = headers.get("X-USER-ID");
		int _userId = -1;
		
		if(_userIds != null && _userIds.size() > 0 && _userIds.get(0).length() > 0) {
			_userId = Integer.parseInt(headers.get("X-USER-ID").get(0));
		}
		else {
			errorCode = "CODE:492";
			errorMsg = "사용자 정보가 없습니다.";
			throw new AppException(null,  "[Error: " + errorCode + "] " + errorMsg);
		}
		
		return _userId;
	}
}
