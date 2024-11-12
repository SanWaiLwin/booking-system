package com.swl.booking.system.request.user;

import java.io.Serializable;
 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -159696360582983147L;

	@NotEmpty(message = "Name is required") 
    private String name;
	
//    @Schema(description = "User email address", example = "user@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

//    @Schema(description = "User password", example = "password123", minLength = 8)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

//    @Schema(description = "Country code for user registration", example = "US", required = true)
    @NotNull(message = "Country is required")
    private String country;

}
