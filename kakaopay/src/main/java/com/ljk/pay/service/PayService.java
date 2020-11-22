package com.ljk.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljk.pay.mapper.PayMapper;

@Service
public class PayService {
	@Autowired
	PayMapper payMapper;
	
	public int getMaxSequenceId(String tableName) throws Exception {
		return payMapper.getMaxSequenceId(tableName);
	}
	
}
