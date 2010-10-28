package com.klink.mapper;

import java.util.List;

import com.jklas.search.util.Pair;
import com.klink.domain.Person;
import com.klink.dto.InsertPersonDto;

public interface PersonMapper {
	public Person selectPerson(long id);
	
	public List<Person> selectByCompany(long id);
	
	public void insertPerson(InsertPersonDto insertDto);
	
	public void deletePerson(Person person);

	public void insertContact(Pair<Long, Long> pair);

	public void deleteContacts(long id);

	public int exists(long id);

	public List<Person> retrieveAll();

	public Person selectPerson(String email);

	public String selectPasswordByMail(String email);

	public Person selectPersonByMail(String mail);

	public void addRelation(long a, long b);
}
