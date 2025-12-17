package com.snaplink.service;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.snaplink.dto.ClickEventDto;
import com.snaplink.dto.UrlMappingDto;
import com.snaplink.models.ClickEvent;
import com.snaplink.models.UrlMapping;
import com.snaplink.models.User;
import com.snaplink.repository.ClickEventRepository;
import com.snaplink.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlMappingService {
	
	private UrlMappingRepository urlMappingRepository;
	private ClickEventRepository clickEventRepository;

	public  UrlMappingDto createShortUrl(String orignalUrl,User user) {
		String shortUrl = generateShortUrl();
		UrlMapping urlMapping = new UrlMapping();
		urlMapping.setOrignalUrl(orignalUrl);
		urlMapping.setUser(user);
		urlMapping.setCreatedAt(LocalDateTime.now());;
		urlMapping.setShortUrl(shortUrl);
		
		UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
		return convertToUrlMappingDto(savedUrlMapping);
	}
	
	private UrlMappingDto convertToUrlMappingDto(UrlMapping urlMapping) {
		
		UrlMappingDto urlMappingDto = new UrlMappingDto();
		urlMappingDto.setId(urlMapping.getId());
		urlMappingDto.setOriginalUrl(urlMapping.getOrignalUrl());
		urlMappingDto.setShortUrl(urlMapping.getShortUrl());
		urlMappingDto.setClickCount(urlMapping.getClickCount());
		urlMappingDto.setCreatedDate(urlMapping.getCreatedAt());
		urlMappingDto.setUsername(urlMapping.getUser().getUsername());
		return urlMappingDto;
	}

	private static String generateShortUrl() {
		
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder shortUrl = new StringBuilder(8);
		
		for(int i = 1; i <= 8; i++)
			shortUrl.append(characters.charAt
					(random.nextInt(characters.length())));
		
		return shortUrl.toString();
	}

	public List<UrlMappingDto> geUrlsByUser(User user) {
	
		return urlMappingRepository.findUrlsByUser(user).stream()
				.map(this::convertToUrlMappingDto)
				.toList();
		
	}

	public List<ClickEventDto> getClickEventByDate(String shortUrl, 
												LocalDateTime start, LocalDateTime end) {
		
		UrlMapping urlMapping= urlMappingRepository.findByShortUrl(shortUrl);
		
		if(urlMapping != null) {
			return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
					.collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),Collectors.counting()))
					.entrySet().stream()
					.map(entry -> {
							ClickEventDto clickEventDto = new ClickEventDto();
							clickEventDto.setClickDate(entry.getKey());
							clickEventDto.setCount(entry.getValue());
							return clickEventDto;
					})
							.collect(Collectors.toList());			
		}
		
		return null;
	}

	public Map<LocalDate, Long> getToalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
		
		List<UrlMapping> urlMappings = urlMappingRepository.findUrlsByUser(user);
		List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
		
		return clickEvents.stream()
				.collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),Collectors.counting()));
	}

	public UrlMapping getOriginalUrl(String shortUrl) {
		
		UrlMapping urlMapping =  urlMappingRepository.findByShortUrl(shortUrl);
		if(urlMapping != null) {
			urlMapping.setClickCount(urlMapping.getClickCount()+1);
			urlMappingRepository.save(urlMapping);
			
			ClickEvent clickEvent = new ClickEvent();
			clickEvent.setClickDate(LocalDateTime.now());
			clickEvent.setUrlMapping(urlMapping);
			clickEventRepository.save(clickEvent);
		}
		return urlMapping;
		
	}

}
