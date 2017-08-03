package com.vc.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vc.model.OAuthProfile;
import com.vc.model.User;
import com.vc.repository.OAuthProfileRepository;

@RestController
@RequestMapping("/oauth-profiles")
public class OAuthProfileService {
	private final OAuthProfileRepository oAuthProfileRepository;

	@Autowired
	public OAuthProfileService(OAuthProfileRepository oAuthProfileRepository) {
		this.oAuthProfileRepository = oAuthProfileRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	List<OAuthProfile> getUsers() {
		return this.oAuthProfileRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	OAuthProfile createUser(@RequestBody OAuthProfile profile) {
		this.oAuthProfileRepository.save(profile);

		return profile;
	}

	@RequestMapping(value = "{oAuthId}", method = RequestMethod.GET)
	OAuthProfile getOAuthProfile(@PathVariable("oAuthId") String oAuthId) {
		OAuthProfile profile = this.oAuthProfileRepository.findOne(oAuthId);

		return profile;
	}

	@RequestMapping(value = "{oAuthId}/user", method = RequestMethod.GET)
	ResponseEntity<User> getUserFromOAuthProfile(@PathVariable("oAuthId") String oAuthId) {
		OAuthProfile profile = this.oAuthProfileRepository.findOne(oAuthId);

		if (profile == null || profile.user == null) {
			return ResponseEntity.notFound().build();
		}

		User user = new User(profile.user.getName(), profile.user.getPassword());
		user.id = profile.user.getId();
		user.username = profile.user.getUsername();
		user.linkedAccount = profile;

		return ResponseEntity.ok(user);
	}
}
