package com.employee.management.service.impl;

import static com.employee.management.constant.UserImplimentionConstant.EAMIL_ALREADY_EXIST;
import static com.employee.management.constant.UserImplimentionConstant.FOUND_USER_BY_USERNAME;
import static com.employee.management.constant.UserImplimentionConstant.NO_USER_FOUND_BY_USER_NAME;
import static com.employee.management.constant.UserImplimentionConstant.USER_ALREADY_EXIST;
import static com.employee.management.constant.UserImplimentionConstant.USER_IMAGE_PROFILE_TEMP;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.employee.management.domain.User;
import com.employee.management.domain.UserPrincipal;
import com.employee.management.enumeration.Role;
import com.employee.management.exception.domain.EmailExistException;
import com.employee.management.exception.domain.UsernameExistException;
import com.employee.management.exception.domain.UserNotFoundException;
import com.employee.management.repository.UserRepository;
import com.employee.management.service.UserService;

@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceIMPL implements UserService, UserDetailsService {
	

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private LoginAttemptService loginAttemptService;

	@Autowired
	public UserServiceIMPL(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,LoginAttemptService loginAttemptService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService=loginAttemptService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USER_NAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USER_NAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

	

	@Override
	public User register(String firstName, String lastName, String username, String email)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        //emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }

	@Override
	public List<User> getUsers() {

		return userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String userName) {

		return userRepository.findUserByUsername(userName);
	}

	@Override
	public User findUserByEmail(String email) {

		return userRepository.findUserByEmail(email);
	}

	private String getTemporaryProfileImageUrl(String username) {

		return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PROFILE_TEMP).toUriString();
	}

	private String encodePassword(String password) {

		return passwordEncoder.encode(password);
	}

	private String generatePassword() {

		return RandomStringUtils.randomAlphabetic(10);
	}

	private String generateUserId() {

		return RandomStringUtils.randomNumeric(10);
	}

	private User validateNewUsernameAndEmail(String currentUserName, String newUserName, String newEmail)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
		User userByNewUserName = findUserByUsername(newUserName);
		User userByNewEmail = findUserByEmail(newEmail);
		if (StringUtils.isNotBlank(currentUserName)) {
			User currentUser = findUserByUsername(currentUserName);
			if (currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USER_NAME + currentUserName);
			}

			if (userByNewUserName != null && !currentUser.getId().equals(userByNewUserName.getId())) {
				throw new UsernameExistException(USER_ALREADY_EXIST);
			}

			if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
				throw new EmailExistException(EAMIL_ALREADY_EXIST);
			}
			return currentUser;

		} else {

			if (userByNewUserName != null) {
				throw new UsernameExistException(USER_ALREADY_EXIST);
			}

			if (userByNewEmail != null) {
				throw new EmailExistException(EAMIL_ALREADY_EXIST);
			}
			return null;
		}

	}
	
	private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

}
