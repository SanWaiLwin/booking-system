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
	
    @NotBlank(message = "Email is required") 
    private String phno;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotNull(message = "Country is required")
    private String country;

}
