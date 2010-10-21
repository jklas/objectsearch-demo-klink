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
package com.klink;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import junit.framework.Assert;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jklas.search.SearchEngine;
import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.memory.MemoryIndex;
import com.klink.dao.PersonDao;
import com.klink.domain.Company;
import com.klink.domain.Person;
import com.klink.dto.InsertCompanyDto;
import com.klink.mapper.CompanyMapper;


public class AbmTest {

	private static SqlSession session = null;
	private static SqlSessionFactory sqlMapper = null;

	@BeforeClass
	public static void startup() throws IOException, ClassNotFoundException, SQLException, SearchEngineMappingException {		
		LogFactory.useStdOutLogging();
		
		String resource = "test-Configuration.xml";
		Resources.setDefaultClassLoader(IBatisHelper.class.getClassLoader());
		Reader reader = Resources.getResourceAsReader(resource);		
		sqlMapper = new SqlSessionFactoryBuilder().build(reader);
		setupDDL();
		SearchEngine.getInstance().newConfiguration();
	}

	private static void setupDDL() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:klink","sa","");
//		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/KLINK","sa","");
		conn.setAutoCommit(false);
		Statement st = conn.createStatement();
		st.execute("CREATE SCHEMA KLINK AUTHORIZATION DBA;");
		st.execute("CREATE TABLE KLINK.PERSON (ID BIGINT NOT NULL, FIRST_NAME VARCHAR(256) NOT NULL, LAST_NAME VARCHAR(256) NOT NULL, EMAIL VARCHAR(256), PASSWORD VARCHAR(32) NOT NULL, COMPANY_ID BIGINT, PRIMARY KEY(ID))");
		st.execute("CREATE TABLE KLINK.COMPANY (ID BIGINT NOT NULL, NAME VARCHAR(256) NOT NULL, PRIMARY KEY(ID))");
		st.execute("CREATE TABLE KLINK.JOB (ID BIGINT NOT NULL, TITLE VARCHAR(256) NOT NULL, PRIMARY KEY(ID))");
		st.execute("CREATE TABLE KLINK.EMPLOYEE (PERSON_ID BIGINT NOT NULL, COMPANY_ID BIGINT NOT NULL, PRIMARY KEY(PERSON_ID,COMPANY_ID))");
		st.execute("CREATE TABLE KLINK.CONTACT (FROM_ID BIGINT NOT NULL, TO_ID BIGINT NOT NULL, PRIMARY KEY(FROM_ID, TO_ID))");
		st.execute("CREATE TABLE KLINK.COUNTRY(ID BIGINT NOT NULL, NAME VARCHAR(256), MAIN_LANG_ID BIGINT, PRIMARY KEY(ID))");
		st.execute("CREATE TABLE KLINK.COUNTRY_LANG(COUNTRY_ID BIGINT NOT NULL, LANG_ID BIGINT NOT NULL, PRIMARY KEY(COUNTRY_ID, LANG_ID))");
		st.execute("CREATE TABLE KLINK.LANGUAGE(ID BIGINT NOT NULL, BASE_NAME VARCHAR(256), LOCAL_NAME VARCHAR(256))");

		st.execute("create sequence klink.person_id start with 1 increment by 1");
		st.execute("create sequence klink.company_id start with 1 increment by 1");
		st.execute("create sequence klink.general_id start with 1 increment by 1");

		st.execute("CREATE TABLE DUAL(DUMMY VARCHAR(5))");
		st.execute("INSERT INTO DUAL VALUES('DUMMY')");
		st.execute("SET TABLE DUAL READONLY TRUE");		
		st.close();
		conn.commit();
		conn.close();
	}

	@Before
	public void before() {
		MemoryIndex.renewAllIndexes();
		session = sqlMapper.openSession(false);
	}
	
	@After
	public void after() {		
		if(session!=null) {		
//			session.commit();
			session.rollback();
			session.close();
		}
	}


	@Test
	public void PersonIsIndexed() throws IOException {		
		Company c = new Company();
		c.setId(1);
		Person p = new Person("Can","Cun","can@cun.com");
		p.setCompany(c);
		
		PersonDao personDao = new PersonDao(session);
		personDao.insertPerson(new Person[]{p}, new String[]{"123456"});
		
		Assert.assertEquals(p,personDao.selectPerson(p.getId()));
	}
	
	@Test
	public void PersonAndContactsAreStored() throws IOException {
		Company c = new Company();
		c.setId(1);
		
		Person q = new Person("Q","Q","q@q.com");
		q.setCompany(c);
		
		Person p = new Person("P","P","p@p.com");
		p.setCompany(c);

		LinkedList<Person> pContacts = new LinkedList<Person>();
		pContacts.add(q);
		p.setContacts(pContacts);
		
		LinkedList<Person> qContacts = new LinkedList<Person>();
		qContacts.add(p);
		q.setContacts(qContacts);
				
		PersonDao personDao = new PersonDao(session);
		personDao.insertPerson(new Person[]{q,p}, new String[]{"654321","123456"});
		
		Assert.assertEquals(p,personDao.selectPerson(p.getId()));				
		Assert.assertEquals(q,personDao.selectPerson(q.getId()));
		
		Assert.assertEquals(q,personDao.selectPerson(p.getId()).getContacts().get(0));
		Assert.assertEquals(p,personDao.selectPerson(q.getId()).getContacts().get(0));
	}
	
	@Test
	public void CompanyIsInserted() throws IOException {
		Company c = new Company();
		c.setName("Something");
		
		CompanyMapper mapper = session.getMapper(CompanyMapper.class);
		
		InsertCompanyDto insertCompanyDto = new InsertCompanyDto();
		insertCompanyDto.setCompany(c);
		mapper.insertCompany(insertCompanyDto);
				
		Assert.assertNotNull( mapper.selectCompanyByName("Something") );		
	}

	@Test
	public void CompanySequenceWorks() throws IOException {
		Company c = new Company();
		c.setName("Something0");
		
		CompanyMapper mapper = session.getMapper(CompanyMapper.class);
		
		InsertCompanyDto insertCompanyDto = new InsertCompanyDto();
		insertCompanyDto.setCompany(c);
		mapper.insertCompany(insertCompanyDto);
		
		c = new Company();
		c.setName("Something1");
		
		insertCompanyDto = new InsertCompanyDto();
		insertCompanyDto.setCompany(c);
		mapper.insertCompany(insertCompanyDto);
				
		Assert.assertNotNull( mapper.selectCompanyByName("Something0") );		
		Assert.assertNotNull( mapper.selectCompanyByName("Something1") );
	}
	
}
