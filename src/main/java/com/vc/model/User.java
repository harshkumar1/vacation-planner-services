package com.vc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User {
	@Id
	@GeneratedValue
	public Long id;

	public String password;

	@Column(unique = true)
	public String username;
	public String name;
	public String email;

	@OneToOne
	public OAuthProfile linkedAccount;

	User() { // jpa only
	}

	public User(String name, String password) {
		this.username = name;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}
