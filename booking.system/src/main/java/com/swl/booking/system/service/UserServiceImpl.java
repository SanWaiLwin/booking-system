package com.swl.booking.system.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swl.booking.system.entity.User;
import com.swl.booking.system.exception.AlreadyExitException;
import com.swl.booking.system.repository.UserRepository;
import com.swl.booking.system.request.user.UserLoginRequest;
import com.swl.booking.system.request.user.UserProfileRequest;
import com.swl.booking.system.request.user.UserRegisterRequest;
import com.swl.booking.system.request.user.UserUpdateRequest;
import com.swl.booking.system.response.user.UserLoginResponse;
import com.swl.booking.system.response.user.UserProfileResponse; 
import com.swl.booking.system.util.CommonConstant;
import com.swl.booking.system.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void registerUser(UserRegisterRequest req) {
		Optional<User> existingUser = userRepository.findByPhno(req.getPhno());
		if (existingUser.isPresent()) { 
			throw new AlreadyExitException("User with this phone already exists.");
		}

		User user = prepareUserFromReq(req);
		user = userRepository.save(user);
		sendVerifyEmail(user);
	} 

	private boolean sendVerifyEmail(User user) {
		return true;
	}

	private User prepareUserFromReq(UserRegisterRequest req) {
		User entity = new User();
		entity.setName(req.getName());
		entity.setPhno(req.getPhno());
		entity.setPassword(passwordEncoder.encode(req.getPassword()));
		entity.setCountry(req.getCountry());
		return entity;
	} 

	@Override
	public UserLoginResponse authenticateAndGenerateToken(UserLoginRequest req) {
		Optional<User> user = userRepository.findByPhno(req.getPhno());
		if (user.isEmpty() || !passwordEncoder.matches(req.getPassword(), user.get().getPassword())) {
			throw new AlreadyExitException("Invalid credentials for phone no: " + req.getPhno()); 
		} 
		return prepareUserLoginResponse(user.get());
	}

	private UserLoginResponse prepareUserLoginResponse(User user) {
		UserLoginResponse resp = new UserLoginResponse();
		resp.setId(user.getId());
		resp.setName(user.getName());
		resp.setPhno(user.getPhno()); 
		return resp;
	}

	@Override
	public UserProfileResponse getByFilter(UserProfileRequest req) {
		Optional<User> user = userRepository.findByPhno(req.getPhno());
		if (user.isEmpty()) {
			throw new AlreadyExitException("Invalid credentials for phone no: " + req.getPhno());
		}
		return prepareDataForProfile(user.get());
	}

	private UserProfileResponse prepareDataForProfile(User user) {
		UserProfileResponse resp = new UserProfileResponse();
		resp.setId(user.getId());
		resp.setName(user.getName());
		resp.setPhno(user.getPhno());
		resp.setCountry(user.getCountry());
		resp.setRegistrationDate(CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, user.getRegistrationDate()));
		return resp;
	}

	@Override
	public void updateUserProfile(UserUpdateRequest req) {
		Optional<User> userOpt = userRepository.findByPhno(req.getPhno());
		if (userOpt.isEmpty()) {
			throw new AlreadyExitException("Invalid credentials for phone no: " + req.getPhno());
		}
		User user = userOpt.get();
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		userRepository.save(user);
	}

	@Override
	public User findById(Long id) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isEmpty()) {
			throw new AlreadyExitException("Invalid credentials for id: " + id);
		}
		return userOpt.get();
	}
}
