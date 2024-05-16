package com.reelpay.api.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;


public final class Tools {
	public static final String CHARSET = "UTF-8";

	// 返回当前系统时间(秒)时间戳
	public static Long time() {
		return System.currentTimeMillis() / 1000L;
	}
	
	// 返回当前系统时间(毫秒)时间戳
	public static Long mtime() {
		return System.currentTimeMillis();
	}
	
	//md5
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}

	public static boolean in_array(String targetValue, String[] arr) {
		return ArrayUtils.contains(arr, targetValue);
	}
}