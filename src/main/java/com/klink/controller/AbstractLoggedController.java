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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.ModelAndView;

import com.klink.dao.PersonDao;
import com.klink.domain.Person;

public class AddContactController extends AbstractLoggedController {
		
	@Override	
	protected ModelAndView internalHandleRequest(HttpServletRequest request, HttpServletResponse response) {
		
		Long idParameter = null;
		try {
			idParameter = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException nfe) {
			return new ModelAndView("badParameters");
		}
		
		
		SqlSession openSession = getiBatisHelper().getSqlSessionFactory().openSession();
		
		try {
			PersonDao personDao = new PersonDao(openSession);
			
			Person newContact = personDao.selectPerson(idParameter);
			
			if(newContact == null)	return new ModelAndView("badParameters");
			
			Person myself = getMyself();
			
			if(myself.getContacts().contains(newContact)
				|| newContact.getContacts().contains(myself)) return new ModelAndView("badParameters"); 
			
			personDao.addContact(myself, newContact);
			
			openSession.commit();
		} catch(Exception e) {
			openSession.rollback();
			e.printStackTrace();
		} finally {
			if(openSession!=null) openSession.close();
		}
					
		return new ModelAndView("addContactView");	
		
	}
}
