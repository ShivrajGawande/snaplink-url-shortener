package com.snaplink.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snaplink.dto.ClickEventDto;
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
	
	@GetMapping("/analytics/{shortUrl}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<ClickEventDto>> getUrlAnalytics(@PathVariable String shortUrl,
															  @RequestParam("startDate")String startDate,
															  @RequestParam("endDate")String endDate){
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		LocalDateTime start = LocalDateTime.parse(startDate, formatter);
		LocalDateTime end = LocalDateTime.parse(endDate, formatter);
		
		List<ClickEventDto> clickEventDtos = urlMappingService.getClickEventByDate(shortUrl,start,end);
		return	ResponseEntity.ok(clickEventDtos);	
	}
	
	@GetMapping("/totalClicks")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate(Principal principal,
																   @RequestParam("startDate")String startDate,
																   @RequestParam("endDate")String endDate){
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		User user = userService.findByUsername(principal.getName());
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);
		
		Map<LocalDate,Long> totalClicks = urlMappingService.getToalClicksByUserAndDate(user,start,end);
		 
		return	ResponseEntity.ok(totalClicks);	
	}
		

}
