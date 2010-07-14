package com.klink.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.klink.IBatisHelper;
import com.klink.dao.PersonDao;
import com.klink.domain.Person;
import com.klink.helper.CookieHelper;

public class HomeController implements Controller {

	private IBatisHelper iBatisHelper ;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return new ModelAndView("homeView");
	}
	
	public void setiBatisHelper(IBatisHelper iBatisHelper) {
		this.iBatisHelper = iBatisHelper;
	}

}
