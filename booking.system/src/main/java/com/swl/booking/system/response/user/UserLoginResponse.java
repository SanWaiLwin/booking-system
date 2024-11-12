package com.swl.booking.system.response.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserLoginResponse implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -4949512248971401757L;

	private Long id;
	
	private String name;
	
	private String phno;
	
	private String token;
}
