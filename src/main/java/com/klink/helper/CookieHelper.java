package com.klink.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

	public final static int NO_ID = -1;
	
	public static long getSessionCookieId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		for (Cookie cookie : cookies) {
			if("session".equals(cookie.getName()))
				return  Long.parseLong(cookie.getValue());
		}
		
		return NO_ID;		
	}
	
	public static void setSessionCookieId(long id, HttpServletResponse response) {	
		response.addCookie(new Cookie("session",String.valueOf(id)));
	}

	public static void deleteSessionCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		
		for (Cookie cookie : cookies) {
			if("session".equals(cookie.getName())) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	

}
