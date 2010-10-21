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
