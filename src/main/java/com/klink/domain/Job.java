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
package com.klink.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jklas.search.annotations.IndexReference;
import com.jklas.search.annotations.Indexable;
import com.jklas.search.annotations.IndexableContainer;
import com.jklas.search.annotations.SearchCollection;
import com.jklas.search.annotations.SearchContained;
import com.jklas.search.annotations.SearchField;
import com.jklas.search.annotations.SearchId;


@Indexable @IndexableContainer
public class Person {

	@SearchId
	private long id;
	
	@SearchField
	private String firstName;
	
	@SearchField
	private String lastName;
	
	@SearchField
	private String email;
	
	@SearchCollection(reference=IndexReference.BOTH)
	private List<Person> contacts = new LinkedList<Person>();

	@SearchContained(reference=IndexReference.CONTAINER)
	private Company company;
	
	public Person() {
	}
	
	public Person(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public Person(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Person(Long id, String firstName, String lastName, String email, List<Person> contacts) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.contacts = contacts;
	}

	
	public void setContacts(List<Person> contacts) {
		this.contacts = contacts;
	}

	public List<Person> getContacts() {
		if(contacts == null) contacts = new ArrayList<Person>();
		return contacts;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		StringBuffer c = new StringBuffer();
		List<Person> contacts = getContacts();
		
		c.append("CONTACTS: ");
		c.append("[");
		for (Person person : contacts) {
			c.append(person.getId());
			c.append(" ");
		}
		c.append("]");
		
		return 	"ID: " + getId() + "\n" +
				"FIRST NAME: " + getFirstName() + "\n" + 
				"LAST NAME: " + getLastName() + "\n" +
				"EMAIL: " + getEmail() + "\n" +
				c.toString();
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + (int) (id ^ (id >>> 32));
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Person other = (Person) obj;
		
		return this.getId() == other.getId();
	}
	
}
