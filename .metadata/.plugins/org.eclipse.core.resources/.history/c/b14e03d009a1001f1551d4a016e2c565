package com.swl.booking.system.request.user;

import java.io.Serializable;
 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 4577872606404204584L;

//	@Schema(description = "User email address", example = "user@example.com")
	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;

//	@Schema(description = "User password", example = "password123", minLength = 8)
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password should be at least 8 characters long")
	private String password;

}
