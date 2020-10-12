package com.employee.management.service;

import java.util.List;

import com.employee.management.domain.User;
import com.employee.management.exception.domain.EmailExistException;
import com.employee.management.exception.domain.UsernameExistException;
import com.employee.management.exception.domain.UserNotFoundException;

public interface UserService {
	 User register(String firstName, String lastName, String username, String email)throws UserNotFoundException, UsernameExistException, EmailExistException;

	    List<User> getUsers();

	    User findUserByUsername(String username);

	    User findUserByEmail(String email);

}
