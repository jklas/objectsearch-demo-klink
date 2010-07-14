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

public abstract class AbstractLoggedController implements Controller {

	private IBatisHelper iBatisHelper ;
	
	private Person myself = null;
	
	private long sessionCookieId; 
	
	public void setiBatisHelper(IBatisHelper iBatisHelper) {
		this.iBatisHelper = iBatisHelper;
	}
	
	public IBatisHelper getiBatisHelper() {
		return iBatisHelper;
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		sessionCookieId = CookieHelper.getSessionCookieId(request);
		
		if(sessionCookieId == CookieHelper.NO_ID) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("success", "false");
			map.put("message", "You must be logged in to add contacts, please do it here:");
			return new ModelAndView("login",map);
		}
		
		return internalHandleRequest(request,response);
	}

	protected abstract ModelAndView internalHandleRequest(HttpServletRequest request, HttpServletResponse response);
	
	protected Person getMyself() {
		
		if(myself!=null) return myself;
		
		SqlSession openSession = iBatisHelper.getSqlSessionFactory().openSession();
		
		try {
			myself = new PersonDao(openSession).selectPerson(sessionCookieId);			
		} finally {
			if(openSession!=null) openSession.close();
		}
		
		return myself;
	}
	
}
