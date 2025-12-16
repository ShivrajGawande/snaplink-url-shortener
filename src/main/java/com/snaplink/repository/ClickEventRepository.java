package com.snaplink.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snaplink.models.ClickEvent;
import com.snaplink.models.UrlMapping;


@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
	
	List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping ,LocalDateTime startDate,LocalDateTime endDate);
	List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMapping ,LocalDateTime startDate,LocalDateTime endDate);	
}
