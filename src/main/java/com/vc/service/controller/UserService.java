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
import com.vc.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserService {
	private final UserRepository userRepository;
	private final OAuthProfileRepository oAuthProfileRepository;

	@Autowired
	public UserService(UserRepository userRepository, OAuthProfileRepository oAuthProfileRepository) {
		this.userRepository = userRepository;
		this.oAuthProfileRepository = oAuthProfileRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	List<User> getUsers() {
		return this.userRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	User createUser(@RequestBody User user) {
		this.userRepository.save(user);

		return user;
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	ResponseEntity<User> login(@RequestBody User user) {
		User userFromDb = this.userRepository.findByUsername(user.getUsername());

		if (userFromDb == null) {
			return ResponseEntity.notFound().build();
		}

		if (!userFromDb.password.equals(user.password)) {
			return ResponseEntity.status(401).build();
		}

		return ResponseEntity.ok(userFromDb);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	ResponseEntity<User> getUser(@PathVariable("id") Long id) {
		User user = this.userRepository.findOne(id);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(user);
	}

	@RequestMapping(value = "{id}/link/{oAuthId}", method = RequestMethod.POST)
	ResponseEntity<User> linkUser(@PathVariable("id") Long id, @PathVariable("oAuthId") String oAuthId) {
		OAuthProfile profile = this.oAuthProfileRepository.findOne(oAuthId);
		User user = this.userRepository.findOne(id);

		if (profile == null) {
			return ResponseEntity.notFound().build();
		}

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		System.out.println("Linking " + user + " to " + profile);

		user.linkedAccount = profile;
		this.userRepository.save(user);

		return ResponseEntity.ok(user);
	}
}
