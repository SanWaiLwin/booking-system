package com.swl.booking.system.request.user;

import java.io.Serializable;
  
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 4577872606404204584L;
 
	@NotBlank(message = "Phone no is required") 
	private String phno;
 
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password should be at least 8 characters long")
	private String password;

}
