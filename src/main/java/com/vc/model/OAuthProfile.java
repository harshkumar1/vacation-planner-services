package com.vc.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OAuthProfile {
	@Id
	private String id;

	private String name;

	private String email;

	private String picture;

	@JsonIgnore
	@OneToOne(mappedBy = "linkedAccount", fetch = FetchType.EAGER)
	public User user;

	public OAuthProfile() {
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPicture() {
		return picture;
	}
}
