package com.efrei.authenticator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efrei.authenticator.model.Website;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {

	Website findByUrl(String url);
	
	Boolean existsByUrl(String url);
}
