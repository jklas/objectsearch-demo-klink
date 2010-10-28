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
