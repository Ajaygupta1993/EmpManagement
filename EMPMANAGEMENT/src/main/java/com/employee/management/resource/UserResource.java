package com.employee.management.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.constant.SecurityConstant;
import com.employee.management.domain.User;
import com.employee.management.domain.UserPrincipal;
import com.employee.management.exception.domain.EmailExistException;
import com.employee.management.exception.domain.ExceptionHandling;
import com.employee.management.exception.domain.UsernameExistException;
import com.employee.management.exception.domain.UserNotFoundException;
import com.employee.management.service.UserService;
import com.employee.management.utility.JWTTokenProvider;

@RestController
@RequestMapping(path  = {"/", "/user"})
public class UserResource extends ExceptionHandling{
	private UserService userService;
	private AuthenticationManager authenticationManager;
	private JWTTokenProvider jwtTokenprovider;
	@Autowired
	public UserResource(UserService userService,AuthenticationManager authenticationManager,JWTTokenProvider jwtTokenprovider) {
		this.userService= userService;
		this.authenticationManager=authenticationManager;
		this.jwtTokenprovider=jwtTokenprovider;
	}
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException   {
		User newUser=userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
		
		return new ResponseEntity<>(newUser, HttpStatus.OK);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user){
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }
	private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
		HttpHeaders headers=new HttpHeaders();
		headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenprovider.generateJwtToken(userPrincipal));
		return headers;
	}
	private void authenticate(String userName, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		
	}

}
