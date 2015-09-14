package com.acme.reservations.domain;

import java.io.Serializable;


public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	
	
	public Customer() {
		//empty
	}
	
	public Customer(Integer id, String username, String firstName, String lastName, String email) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + "]";
	}
	
}
