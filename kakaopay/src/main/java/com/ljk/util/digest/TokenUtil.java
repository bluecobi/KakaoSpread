package com.ljk.util.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenUtil {

	public static String md5(String msg) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(msg.getBytes());
		
		return byteToHexString(md.digest());
	}
	
	public static String byteToHexString(byte[] data) {
		
		StringBuilder sb = new StringBuilder();
		for(byte b : data) {
			sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
}
