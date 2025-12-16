package com.snaplink.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.snaplink.dto.LoginRequest;
import com.snaplink.models.User;
import com.snaplink.repository.UserRepository;
import com.snaplink.security.jwt.JwtAuthenticationResponce;
import com.snaplink.security.jwt.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;
	
	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public JwtAuthenticationResponce loginUser(LoginRequest login) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername(),login.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		String jwt = jwtUtils.generateToken(userDetails);
		return new JwtAuthenticationResponce(jwt);
	}

	public User findByUsername(String name) {
		
		return userRepository.findByUsername(name)
				.orElseThrow(()->new UsernameNotFoundException("User not found with username : "+name));
	}

}
