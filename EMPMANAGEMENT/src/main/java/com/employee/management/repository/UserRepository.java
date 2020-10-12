package com.employee.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.management.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findUserByUsername(String userName);
	
	User findUserByEmail(String email);
	
	
	
	

}
