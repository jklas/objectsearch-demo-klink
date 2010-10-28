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
