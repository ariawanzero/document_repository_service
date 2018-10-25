package com.asdp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
	public static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	public static String encryptPassword(String password){
		return passwordEncoder.encode(password);
	}
	public static boolean matchPassword(String password, String newPassword){
		return passwordEncoder.matches(newPassword, password);
	}
}
