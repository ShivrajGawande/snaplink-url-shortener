package com.snaplink.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snaplink.dto.UrlMappingDto;
import com.snaplink.models.User;
import com.snaplink.service.UrlMappingService;
import com.snaplink.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {

    private UserService userService;
	
	private UrlMappingService urlMappingService;
	
	@PostMapping("/shorten")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String,String> request,
														Principal principal){
		String orignalUrl = request.get("originalUrl");
		User user = userService.findByUsername(principal.getName());
		UrlMappingDto urlMappingDto = urlMappingService.createShortUrl(orignalUrl,user);
		
		return ResponseEntity.ok(urlMappingDto);
		 
	}
	
	@GetMapping("/myurls")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<UrlMappingDto>> getUserUrls(Principal principal){
		
		User user = userService.findByUsername(principal.getName());
		List<UrlMappingDto> urls = urlMappingService.geUrlsByUser(user);
		return ResponseEntity.ok(urls);
	}

}
