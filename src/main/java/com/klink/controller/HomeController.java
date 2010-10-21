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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.jklas.search.engine.VectorSearch;
import com.jklas.search.engine.dto.VectorRankedResult;
import com.jklas.search.engine.score.DefaultVectorRanker;
import com.jklas.search.index.berkeley.BerkeleyGlobalPropertyEditor;
import com.jklas.search.index.berkeley.BerkeleyIndexReaderFactory;
import com.jklas.search.query.vectorial.VectorQuery;
import com.jklas.search.query.vectorial.VectorQueryParser;
import com.klink.IBatisHelper;
import com.klink.dao.PersonDao;
import com.klink.domain.Person;
import com.klink.helper.CookieHelper;

public class SearchController implements Controller {

	private IBatisHelper iBatisHelper ;

	private String baseDir ;

	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
		new BerkeleyGlobalPropertyEditor().setBaseDir(baseDir);
	}
	
	public String getBaseDir() {
		return baseDir;
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception {		

		String searchExpression = request.getParameter("q");
		
		VectorQuery query = new VectorQueryParser(searchExpression).getQuery();
		
		long init = System.currentTimeMillis();
		VectorSearch vectorSearch = new VectorSearch(query, BerkeleyIndexReaderFactory.getInstance());
		long totalTime = System.currentTimeMillis() - init;

		List<VectorRankedResult> results = vectorSearch.search(new DefaultVectorRanker());

		Map<String, Object> model = new HashMap<String,Object>();

		model.put("search.query", searchExpression);

		if(results.size()==0) {
			model.put("results.start", 0);		
			model.put("results.end", 0);
		} else {
			model.put("results.start", (query.getPage()-1)*query.getPageSize()+1);		
			model.put("results.end", (query.getPage()-1)*query.getPageSize()+results.size());			
		}

		model.put("search.time", ((float)totalTime)/1000);

		SqlSession session = iBatisHelper.getSqlSessionFactory().openSession();
		
		try {
			PersonDao personDao = new PersonDao(session);
						
			List<Object> resultList = new ArrayList<Object>();			
			model.put("results", resultList);

			long myId = CookieHelper.getSessionCookieId(request);

			HashSet<Long> myContacts = new HashSet<Long>();
			if(results.size() > 0 && myId != CookieHelper.NO_ID) {
				Person me = personDao.selectPerson(myId);
				
				for (Person person : me.getContacts()) {
					myContacts.add(person.getId());
				}
			}
			
			for (VectorRankedResult result : results) {
				Class<?> resultClass = result.getKey().getClazz();
				Serializable resultId = result.getKey().getId();
				
				if(Person.class.equals(resultClass) && !resultId.equals(myId)) {
					resultList.add(
						new ResultRow(personDao.selectPerson((Long)resultId),result.getScore(), myContacts.contains(resultId))
					);					
				}
				
			}
		} finally {
			if(session!=null) session.close();
		}

		return new ModelAndView("searchResultsView","searchResults",model);
	}

	public void setiBatisHelper(IBatisHelper iBatisHelper) {
		this.iBatisHelper = iBatisHelper;
	}

	public class ResultRow {
		public final Object result;
		public final Double score;
		public final boolean isContact;
		
		public ResultRow(Object result, Double score, boolean isContact) {
			this.result = result;
			this.score = score;
			this.isContact = isContact;
		}

		public Object getResult() {
			return result;
		}

		public Double getScore() {
			return score;
		}
		
		public boolean isContact() {
			return isContact;
		}
		
		public boolean getIsContact() {
			return isContact;
		}
	}
}