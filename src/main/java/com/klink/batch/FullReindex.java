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
package com.klink.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jklas.search.SearchEngine;
import com.jklas.search.exception.IndexObjectException;
import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.berkeley.BerkeleyGlobalPropertyEditor;
import com.jklas.search.index.berkeley.BerkeleyIndex;
import com.jklas.search.index.berkeley.BerkeleyIndexWriterFactory;
import com.jklas.search.indexer.DefaultIndexerService;
import com.jklas.search.indexer.IndexerService;
import com.jklas.search.indexer.online.OnlineIndexer;
import com.jklas.search.indexer.pipeline.DefaultIndexingPipeline;
import com.jklas.search.util.Utils;
import com.klink.IBatisHelper;
import com.klink.dao.PersonDao;
import com.klink.domain.Company;
import com.klink.domain.Country;
import com.klink.domain.Person;


public class LoadPeople {

	public static void main(String[] args) throws IOException, IndexObjectException, SearchEngineMappingException {
		LogFactory.useStdOutLogging();
		
		String resource = "com/klink/Configuration.xml";
		Resources.setDefaultClassLoader(IBatisHelper.class.getClassLoader());
		Reader reader = Resources.getResourceAsReader(resource);		
		
		SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
		
		SqlSession session = sqlMapper.openSession();
		try {
			
			PersonDao personDao = new PersonDao(session);
			
			SearchEngine.getInstance().newConfiguration();
			
			Utils.configureAndMap(Person.class);
			Utils.configureAndMap(Company.class);
			
			new BerkeleyGlobalPropertyEditor().setBaseDir("idx");
			
			new LoadPeople().indexBerkeley(personDao);
			
			session.commit();
		} finally {
			if(session!=null) session.close();
		}
	}
	
	public void indexBerkeley(PersonDao personDao) throws IOException, IndexObjectException {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("person.csv");

		BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

		reader.readLine();

		String currentLine ;
		StringTokenizer tabTokenizer, spaceTokenizer ;
		String name, email, firstName, lastName;
		
		List<Person> personList = new ArrayList<Person>(30000);
		
		int maxDirectors = 1000;
		int count = 1;
		
		while((currentLine = reader.readLine()) != null && count++ <= maxDirectors) {

			tabTokenizer = new StringTokenizer(currentLine, "\t");
			name = tabTokenizer.hasMoreElements() ? tabTokenizer.nextToken().trim() : "";
			
			spaceTokenizer = new StringTokenizer(name, " ");
			
			firstName = spaceTokenizer.hasMoreElements() ? spaceTokenizer.nextToken().trim() : "";
			lastName = spaceTokenizer.hasMoreElements() ? spaceTokenizer.nextToken().trim() : "";
			
			email = firstName+"."+lastName+"@company.com";
			
//			System.out.println(currentLine);
			
			Person person = new Person();
			person.setFirstName(firstName);
			person.setLastName(lastName);
			person.setEmail(email+"@something.com");
			
			Country country = new Country();
			country.setId(0);
			country.setMainLanguageId(0);
			country.setName("World");
			
			Company company = new Company();
			company.setId(0);
			company.setName("something");
			company.setCountry(country);
			person.setCompany(company);
			
			personList.add(person);
		}

		for (int i = 0; i < personList.size(); i++) {
			Person person = personList.get(i);

			List<Person> contactList = new ArrayList<Person>();
			
			person.setContacts(contactList);
			
			for (int j = -3; j < 2 ; j++) {
				if(i+j < personList.size() && i+j>0) contactList.add(personList.get(i+j));
			}
			
		}
		
		BerkeleyIndex.renewAllIndexes();
		List<String> password = new ArrayList<String>(personList.size()); 
		
		for (int i = 0; i < personList.size(); i++) {
			Person person = personList.get(i);

			List<Person> contactList = new ArrayList<Person>(5);
			
			person.setContacts(contactList);
			
			for (int j = -5; j < 0 ; j++) {
				if(i+j < 5 && i+j>0) contactList.add( personList.get(i+j) );
			}
			password.add(person.getFirstName());
			
		}
		
		personDao.insertPerson(personList,password);

		IndexerService onlineIndexer = new OnlineIndexer(new DefaultIndexerService(new DefaultIndexingPipeline(), BerkeleyIndexWriterFactory.getInstance()));
				
		long init = System.currentTimeMillis();		
		onlineIndexer.bulkCreate(personList);		
		long end= System.currentTimeMillis();		
		float total = ((float)(end-init))/1000;
		System.out.println(personList.size()+" directors: "+total+" (ms)");
		System.out.println("Index size: "+BerkeleyIndex.getDefaultIndex().getObjectCount());

	}
	
}
