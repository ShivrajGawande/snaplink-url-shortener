package com.snaplink.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.snaplink.models.UrlMapping;
import com.snaplink.service.UrlMappingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RedirectController {
	
	private UrlMappingService urlMappingService;
	
	@GetMapping("/{shortUrl}")
	public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
		
		UrlMapping urlMapping =  urlMappingService.getOriginalUrl(shortUrl);
		
			if(urlMapping != null) {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location", urlMapping.getOrignalUrl());
			return ResponseEntity.status(302).headers(httpHeaders).build();
		}
			else {
				return ResponseEntity.notFound().build();
			}
		
	}

}
