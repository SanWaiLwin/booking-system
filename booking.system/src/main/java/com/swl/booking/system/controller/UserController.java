package com.swl.booking.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.swl.booking.system.exception.RdpException;
import com.swl.booking.system.request.ApiRequest;
import com.swl.booking.system.request.user.UserLoginRequest;
import com.swl.booking.system.request.user.UserProfileRequest;
import com.swl.booking.system.request.user.UserRegisterRequest;
import com.swl.booking.system.request.user.UserUpdateRequest;
import com.swl.booking.system.response.ApiResponse;
import com.swl.booking.system.response.user.UserLoginResponse;
import com.swl.booking.system.response.user.UserProfileResponse;
import com.swl.booking.system.security.JwtTokenProvider;
import com.swl.booking.system.service.UserService;
import com.swl.booking.system.util.CommonConstant;

@RestController
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class); 

	private final UserService userService;

	private final JwtTokenProvider jwtTokenProvider;

	public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@RequestMapping(value = "api/register", method = RequestMethod.POST)
	public ApiResponse<Void> registerUser(@RequestBody ApiRequest<UserRegisterRequest> apiRequest) throws RdpException {
		try {
			logger.info("** registerUser api start **");
			UserRegisterRequest req = apiRequest.getData();
			userService.registerUser(req);

			ApiResponse<Void> response = new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS,
					"Registration successful. Please check your email to verify.");
			logger.info("** registerUser api success **");
			return response;
		} catch (Exception e) {
			logger.error("Error occurred during user registration: " + e.getMessage(), e);

			ApiResponse<Void> errorResponse = new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED,
					"An error occurred while processing your request. Please try again later.");
			return errorResponse;
		}
	}

	@RequestMapping(value = "api/login", method = RequestMethod.POST)
	public ApiResponse<UserLoginResponse> loginUser(@RequestBody ApiRequest<UserLoginRequest> apiRequest) {
		try {
			logger.info("** loginUser api start **");
			UserLoginRequest req = apiRequest.getData();
			UserLoginResponse resp = userService.authenticateAndGenerateToken(req);
			String token = jwtTokenProvider.generateToken(resp.getPhno());
			resp.setToken(token);

			ApiResponse<UserLoginResponse> response = new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS,
					"Login successful", resp);
			logger.info("** loginUser api success **");
			return response;
		} catch (Exception e) {
			logger.error("Error occurred during user login: " + e.getMessage(), e);
			return new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED,
					"An error occurred while processing your request. Please try again later.");
		}
	}

	@RequestMapping(value = "api/auth/update-profile", method = RequestMethod.POST) 
	public ApiResponse<String> updateUserProfile(@RequestBody ApiRequest<UserUpdateRequest> apiRequest) {
		try {
			logger.info("** updateUserProfile api start **");
			UserUpdateRequest req = apiRequest.getData();
			userService.updateUserProfile(req);
			logger.info("** updateUserProfile api success **");
			return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Update successful");
		} catch (Exception e) {
			logger.error("Error occurred during user login: " + e.getMessage(), e);
			return new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED,
					"An error occurred while processing your request. Please try again later.");
		}
	}

	@RequestMapping(value = "api/auth/get-user", method = RequestMethod.POST)  
	public ApiResponse<UserProfileResponse> getUsers(@RequestBody ApiRequest<UserProfileRequest> apiRequest) {
		try {
			logger.info("** getUsers api start **");
			UserProfileRequest req = apiRequest.getData();
			UserProfileResponse resp = userService.getByFilter(req);
			logger.info("** getUsers api success **");
			return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Users retrieved successfully", resp);
		} catch (Exception e) {
			logger.error("Error occurred during get user: " + e.getMessage(), e);
			return new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED,
					"An error occurred while processing your request. Please try again later.");
		}
	}
}
