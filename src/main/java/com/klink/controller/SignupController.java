package com.klink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.klink.IBatisHelper;
import com.klink.dao.PersonDao;
import com.klink.domain.Company;
import com.klink.domain.Person;
import com.klink.dto.InsertCompanyDto;
import com.klink.mapper.CompanyMapper;

public class SignupController implements Controller {

	private IBatisHelper iBatisHelper ;

	public void setiBatisHelper(IBatisHelper iBatisHelper) {
		this.iBatisHelper = iBatisHelper;
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subaction = request.getParameter("subaction") !=null ? request.getParameter("subaction") : "";
		String email = request.getParameter("email") !=null ? request.getParameter("email") : "";
		String firstName = request.getParameter("firstName") !=null ? request.getParameter("firstName") : "";
		String lastName = request.getParameter("lastName") !=null ? request.getParameter("lastName") : "";
		String companyName = request.getParameter("companyName") !=null ? request.getParameter("companyName") : "";
		String countryName = request.getParameter("countryName") !=null ? request.getParameter("countryName") : "";
		String password = request.getParameter("password") !=null ? request.getParameter("password") : "";
		
		if("".equals(subaction) || "".equals(email)
				|| "".equals(firstName) || "".equals(lastName)
				|| "".equals(password)) return new ModelAndView("signup");

		Map<String,String> model = new HashMap<String, String>();
		SqlSession openSession = iBatisHelper.getSqlSessionFactory().openSession();
		try {
			
			
			if ( validateMailUniqueness(openSession, email) ) {
				signin(openSession, email, firstName, lastName, companyName, countryName, password);
				openSession.commit();
				model.put("success","true");
				model.put("message","Registration was succesful, please login now");
				return new ModelAndView("login",model);
			} else {
				model.put("success","false");
				model.put("message","It seems that you've already registered with this email, please user another one");
				return new ModelAndView("signup",model);
			}
			
			
		} catch(Exception e){			
			model.put("success","false");
			model.put("message","Registration was unsuccesful, please contact webmaster for help");
			
			if(openSession!=null) openSession.rollback();
			return new ModelAndView("login",model);
		} finally {
			if(openSession!=null) openSession.close();
		}
		
	}

	private void signin(SqlSession openSession, String email, String firstName,
			String lastName, String companyName, String countryName,
			String password)
	{
		CompanyMapper mapper = (CompanyMapper) openSession.getMapper(CompanyMapper.class);
		
		Company company = mapper.selectCompanyByName(companyName);
		
		if(company == null) {
			company = new Company();
			company.setName(companyName);
			InsertCompanyDto insertCompanyDto = new InsertCompanyDto();
			insertCompanyDto.setCompany(company);
			mapper.insertCompany(insertCompanyDto);			
		}
		
		Person person = new Person(firstName, lastName, email);
		person.setCompany(company);
		person.setContacts(new ArrayList<Person>());
		
		new PersonDao(openSession).insertPerson(new Person[]{person}, new String[]{password});
	}

	private boolean validateMailUniqueness(SqlSession openSession, String email) {
		return new PersonDao(openSession).selectPersonByMail(email)==null;
	}

}
