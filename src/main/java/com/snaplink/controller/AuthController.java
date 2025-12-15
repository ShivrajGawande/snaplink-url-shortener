package com.snaplink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snaplink.dto.LoginRequest;
import com.snaplink.dto.RegisterRequest;
import com.snaplink.models.User;
import com.snaplink.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	

	private UserService userService;
	
	@PostMapping("/public/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
		
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(registerRequest.getPassword());
		user.setRole("ROLE_USER");
		userService.registerUser(user);
		
		return ResponseEntity.ok("User Registred Successfully");
	}
	
	@PostMapping("/public/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
		return ResponseEntity.ok(userService.loginUser(loginRequest));
	}

}
