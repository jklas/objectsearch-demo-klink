/**
 * Object Search Framework
 *
 * Copyright (C) 2010 Julian Klas
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
