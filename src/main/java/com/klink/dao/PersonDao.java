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
package com.klink.dao;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.jklas.search.util.Pair;
import com.klink.domain.Person;
import com.klink.dto.InsertPersonDto;
import com.klink.mapper.PersonMapper;

public class PersonDao {

	private PersonMapper personMapper;
		
	public PersonDao(SqlSession session) {
		this.personMapper = session.getMapper(PersonMapper.class);
	}
	
	public void insertPerson(Person[] person, String[] password) {
		insertPerson(Arrays.asList(person),Arrays.asList(password));
	}

	public void insertPerson(List<Person> person, List<String> password) {
		for (int i = 0; i < person.size(); i++) {			
			InsertPersonDto pDto = new InsertPersonDto();
			pDto.setPassword(password.get(i));
			pDto.setPerson(person.get(i));
			personMapper.insertPerson(pDto);			
		}
		
		for (int i = 0; i < person.size(); i++) {
			Person current = person.get(i);
			
			for (Person contact : current.getContacts()) {
				personMapper.insertContact(
						new Pair<Long,Long>(person.get(i).getId(),
								contact.getId()));
			}
		}
	}
	
	public void deletePerson(Person person) {
		this.personMapper.deletePerson(person);
		this.personMapper.deleteContacts(person.getId());
	}

	public Person selectPerson(long id) {
		return this.personMapper.selectPerson(id);
	}

	public boolean existsPersonById(long id) {		
		return new Integer(1).equals(this.personMapper.exists(id));
	}

	public List<Person> retrieveAll() {
		return this.personMapper.retrieveAll();
	}

	public String selectPasswordByMail(String mail) {
		return this.personMapper.selectPasswordByMail(mail);
	}

	public Person selectPersonByMail(String mail) {
		return this.personMapper.selectPersonByMail(mail);
	}

	public void addContact(Person myself, Person newContact) {
		this.personMapper.insertContact(new Pair<Long,Long>(myself.getId(),newContact.getId()));
		this.personMapper.insertContact(new Pair<Long,Long>(newContact.getId(),myself.getId()));
	}
	
}
