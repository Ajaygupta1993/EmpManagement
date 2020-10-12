package com.employee.management.enumeration;

import static com.employee.management.constant.Authority.USER_AUTHORITY;

import static com.employee.management.constant.Authority.HR_AUTHORITY;
import static com.employee.management.constant.Authority.MANAGER_AUTHORITY;
import static com.employee.management.constant.Authority.ADMIN_AUTHORITY;
import static com.employee.management.constant.Authority.SUPER_ADMIN_AUTHORITY;

public enum Role {
	ROLE_USER(USER_AUTHORITY),
	ROLE_HR(HR_AUTHORITY),
	ROLE_MANAGER(MANAGER_AUTHORITY),
	ROLE_ADMIN(ADMIN_AUTHORITY),
	ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITY);
	
	private String[] authorities;
	
	Role(String... authorities){
		this.authorities=authorities;
	}
	public String[] getAuthorities() {
		return authorities;
	}

}
