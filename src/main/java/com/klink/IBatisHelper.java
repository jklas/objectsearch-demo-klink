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
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.util.SearchLibrary;
import com.klink.domain.Company;
import com.klink.domain.Person;
import com.klink.dto.InsertPersonDto;

public class IBatisHelper {

	private String resource = null;
	
	private SqlSessionFactory sqlSessionFactory = null;
	
	static {
		try {
			SearchLibrary.configureAndMap(new Class<?>[]{Person.class,Company.class,InsertPersonDto.class});
			
		} catch (SearchEngineMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IBatisHelper(String resourceLocation) throws IOException {
		this.resource =resourceLocation;
		Resources.setDefaultClassLoader(IBatisHelper.class.getClassLoader());
		Reader reader = Resources.getResourceAsReader(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);	
	}
	
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	
	public void setSampleDb(Object dummy) throws ClassNotFoundException, SQLException {

	}
	
}
