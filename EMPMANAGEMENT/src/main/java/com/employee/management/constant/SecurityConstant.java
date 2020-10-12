package com.employee.management.constant;

public class SecurityConstant {
	public static final long EXPIRATION_TIME=432_000_000; //5days
	public static final String TOKEN_PREFIX="Bearer ";
	public static final String JWT_TOKEN_HEADER="Jwt-Token";
	public static final String TOKEN_CANNOT_BE_VERIFIED="Token cannot be verified";
	public static final String GET_ARRAYS_LLC="Get ARRAYS,LLC";//company name
	public static final String GET_ARRAYS_ADMINSTRATION="User Management Portal";
	public static final String AUTHORITIES="authorities";
	public static final String FORBIDDEN_MESSAGE="You need to login to access the page";
	public static final String ACCESS_DENIED_MESSAGE="You Do not have permission to access the page";
	public static final String OPTION_HTTP_METHOD="OPTIONS";
	public static final String[] PUBLIC_URLS= {"/user/login", "/user/register", "/user/resetpassword/**", "/user/image/**"};
	
	//public static final String[] PUBLIC_URLS= {"**"};
	
	

}
