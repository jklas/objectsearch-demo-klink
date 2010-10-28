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

public class LoginController implements Controller {

	private IBatisHelper iBatisHelper ;

	public void setiBatisHelper(IBatisHelper iBatisHelper) {
		this.iBatisHelper = iBatisHelper;
	}

	public LoginController() {
		
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String subaction = request.getParameter("subaction") !=null ? request.getParameter("subaction") : "";

		if("".equals(subaction)) return new ModelAndView("login");

		SqlSession session = iBatisHelper.getSqlSessionFactory().openSession();

		Map<String,String> map = new HashMap<String,String>();
		try {

			if("try".equals(subaction)) {
				String mail = request.getParameter("mail") !=null ? request.getParameter("mail") : "";
				String password = request.getParameter("password") !=null ? request.getParameter("password") : "";

				PersonDao personDao = new PersonDao(session);

				String retrievedPassword = personDao.selectPasswordByMail(mail);

				if(password.equals(retrievedPassword)) {
					Person person = personDao.selectPersonByMail(mail);
					CookieHelper.setSessionCookieId(person.getId(), response);
					return new ModelAndView("homeView");
				} else {
					map.put("success", "false");
					map.put("message", "We couldn't find any user matching the username and password you provided, please check the info again or signup using the link below");
				}
			}
		} finally {
			if(session!=null) session.close();
		}

		return new ModelAndView("login",map);

	}
}
