package org.moondb.dao;

import java.util.List;

import org.moondb.model.*;
import org.moondb.util.DatabaseUtil;

public class PersonDao {
	private Citation citation;
	
	public PersonDao(Citation citation) {
		this.citation = citation;
	}
	
	public void saveData() {
		List<Author> authors = citation.getAuthors();
		String query;
		Integer authorOrder = 0;
		for(Author author: authors) {
			System.out.println(author.getFullName());
			authorOrder += 1;
			if(!isPersonExist(author)) {
				String firstName = author.getFirstName();
				String lastName = author.getLastName();
				//save data to person table
				query = "INSERT INTO person(first_name,last_name) VALUES('"+firstName+"','"+lastName+"')";
				DatabaseUtil.update(query);
				//save data to author_list table
				query = "INSERT INTO author_list(citation_num,person_num,author_order) values("+citation.getCitationNum()+","+getPersonNum(author)+","+authorOrder+")";
				DatabaseUtil.update(query);
			} else {
				//save data to author_list table
				query = "INSERT INTO author_list(citation_num,person_num,author_order) values("+citation.getCitationNum()+","+getPersonNum(author)+","+authorOrder+")";
				DatabaseUtil.update(query);				
			}
		}
	}
	
	public boolean isPersonExist(Author author) {
		String query = "SELECT COUNT(*) FROM person WHERE last_name='"+author.getLastName()+"' AND first_name='"+author.getFirstName()+"'";
		Long count = (Long)DatabaseUtil.getUniqueResult(query);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public Integer getPersonNum(Author author) {
		String query = "SELECT person_num FROM person WHERE last_name='"+author.getLastName()+ "' AND first_name='"+author.getFirstName()+"'";
		return (Integer)DatabaseUtil.getUniqueResult(query);
	}

}