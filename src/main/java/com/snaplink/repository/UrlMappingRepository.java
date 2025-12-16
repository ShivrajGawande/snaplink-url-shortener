package com.snaplink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snaplink.models.UrlMapping;
import com.snaplink.models.User;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
	
	UrlMapping findByShortUrl(String shortUrl);
	List<UrlMapping> findUrlsByUser(User user);
}
