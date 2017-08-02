package com.vc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vc.model.OAuthProfile;

public interface OAuthProfileRepository extends JpaRepository<OAuthProfile, String> {
}
