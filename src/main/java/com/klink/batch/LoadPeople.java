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

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jklas.search.exception.IndexObjectException;
import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.berkeley.BerkeleyGlobalPropertyEditor;
import com.jklas.search.index.berkeley.BerkeleyIndex;
import com.jklas.search.index.berkeley.BerkeleyIndexWriterFactory;
import com.jklas.search.indexer.DefaultIndexerService;
import com.jklas.search.indexer.pipeline.DefaultIndexingPipeline;
import com.jklas.search.util.SearchLibrary;
import com.klink.IBatisHelper;
import com.klink.dao.PersonDao;
import com.klink.domain.Person;


public class FullReindex {

	public static void main(String[] args) throws IOException, SearchEngineMappingException, IndexObjectException {
		
		SearchLibrary.configureAndMap(Person.class);
		
		new BerkeleyGlobalPropertyEditor().setBaseDir("idx/");
		
		BerkeleyIndex.renewAllIndexes();
		
		Log log = LogFactory.getLog(FullReindex.class);
		
		log.info("Starting Person.class reindex...");
		
		String resource = "com/klink/Configuration.xml";
		Resources.setDefaultClassLoader(IBatisHelper.class.getClassLoader());
		Reader reader = Resources.getResourceAsReader(resource);		
				
		PersonDao personDao = new PersonDao(new SqlSessionFactoryBuilder().build(reader).openSession());
		
		List<Person> personList = personDao.retrieveAll();
		
		new DefaultIndexerService(new DefaultIndexingPipeline(),BerkeleyIndexWriterFactory.getInstance()).bulkCreate(personList);
		
		log.info("Person.class reindex finished...");

	}
	
}
