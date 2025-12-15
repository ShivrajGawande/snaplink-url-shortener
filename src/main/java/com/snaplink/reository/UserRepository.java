package com.snaplink.reository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snaplink.models.User;

public interface UserRepository extends JpaRepository<User , Long> {

	public Optional<User> findByUsername(String username);
}
