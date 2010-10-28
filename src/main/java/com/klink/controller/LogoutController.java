package com.klink.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.klink.helper.CookieHelper;

public class LogoutController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		Cookie[] cookies = request.getCookies();
		
		ModelAndView modelAndView = new ModelAndView("login");
		
		if(cookies==null) return modelAndView;
		
		CookieHelper.deleteSessionCookie(request, response);
		
		return modelAndView;
	}

}
