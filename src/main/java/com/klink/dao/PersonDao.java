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
