package com.swl.booking.system.service;

import com.swl.booking.system.request.user.UserLoginRequest;
import com.swl.booking.system.request.user.UserProfileRequest;
import com.swl.booking.system.request.user.UserRegisterRequest;
import com.swl.booking.system.request.user.UserUpdateRequest;
import com.swl.booking.system.response.user.UserLoginResponse;
import com.swl.booking.system.response.user.UserProfileResponse;

public interface UserService {  

	boolean verifyUser(String token); 

	void registerUser(UserRegisterRequest req);

	UserLoginResponse authenticateAndGenerateToken(UserLoginRequest req);

	UserProfileResponse getByFilter(UserProfileRequest req);

	void updateUserProfile(UserUpdateRequest req);
}
