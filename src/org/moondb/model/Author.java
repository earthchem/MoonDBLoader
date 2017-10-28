package org.moondb.model;


public class Author {
	private String fullName;
	private String firstName;
	private String lastName;
	private Integer authorOrder;
	
	public Integer getAuthorOrder() {
		return authorOrder;
	}
	public void setAuthorOrder(Integer authorOrder) {
		this.authorOrder = authorOrder;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
