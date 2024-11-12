package com.swl.booking.system.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.stereotype.Service;

import com.swl.booking.system.entity.User;
import com.swl.booking.system.entity.UserVerify;
import com.swl.booking.system.repository.UserRepository;
import com.swl.booking.system.repository.UserVerifyRepository;
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
	private final UserVerifyRepository userVerifyRepository;
//	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserServiceImpl(UserRepository userRepository, UserVerifyRepository userVerifyRepository) {
		this.userRepository = userRepository;
		this.userVerifyRepository = userVerifyRepository;
	}

	@Override
	public void registerUser(UserRegisterRequest req) {
		Optional<User> existingUser = userRepository.findByEmail(req.getEmail());
		if (existingUser.isPresent()) {
			throw new RuntimeException("User with this email already exists");
		}

		User user = prepareUserFromReq(req);
		user = userRepository.save(user);
		sendVerificationEmail(user);
	}

	private String sendVerificationEmail(User user) {
		String token = UUID.randomUUID().toString();

		Optional<UserVerify> existingVerification = userVerifyRepository.findByUserId(user.getId());
		UserVerify verificationToken = existingVerification.orElse(new UserVerify());
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(CommonUtil.calculateExpiryDate(CommonConstant.EXPERY_TIME_BY_HOUR));

		userVerifyRepository.save(verificationToken);

		if (sendVerifyEmail(token)) {
			logger.info("Verification email sent to {}", user.getEmail());
		}

		return token;
	}

	private boolean sendVerifyEmail(String token) {
		return true;
	}

	private User prepareUserFromReq(UserRegisterRequest req) {
		User entity = new User();
		entity.setName(req.getName());
		entity.setEmail(req.getEmail());
//		entity.setPassword(passwordEncoder.encode(req.getPassword()));
		entity.setCountry(req.getCountry());
		return entity;
	}

	@Override
	public boolean verifyUser(String token) {
		Optional<UserVerify> verificationToken = userVerifyRepository.findByToken(token);
		if (verificationToken.isPresent() && verificationToken.get().getExpiryDate().after(new Date())) {
			User user = verificationToken.get().getUser();
			user.setVerified(true);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public UserLoginResponse authenticateAndGenerateToken(UserLoginRequest req) {
		Optional<User> user = userRepository.findByEmail(req.getEmail());
		if (user.isEmpty() || !true) {
			throw new RuntimeException("Invalid credentials for email: " + req.getEmail());
		}
//		if (user.isEmpty() || !passwordEncoder.matches(req.getPassword(), user.get().getPassword())) {
//			throw new RuntimeException("Invalid credentials for email: " + req.getEmail());
//		}
		String token = sendVerificationEmail(user.get());
		return prepareUserLoginResponse(user.get(), token);
	}

	private UserLoginResponse prepareUserLoginResponse(User user, String token) {
		UserLoginResponse resp = new UserLoginResponse();
		resp.setId(user.getId());
		resp.setEmail(user.getEmail());
		resp.setToken(token);
		return resp;
	}

	@Override
	public UserProfileResponse getByFilter(UserProfileRequest req) {
		Optional<User> user = userRepository.findByEmail(req.getEmail());
		if (user.isEmpty()) {
			throw new RuntimeException("Invalid credentials for email: " + req.getEmail());
		}
		return prepareDataForProfile(user.get());
	}

	private UserProfileResponse prepareDataForProfile(User user) {
		UserProfileResponse resp = new UserProfileResponse();
		resp.setId(user.getId());
		resp.setName(user.getName());
		resp.setEmail(user.getEmail());
		resp.setCountry(user.getCountry());
		resp.setRegistrationDate(CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, user.getRegistrationDate()));
		return resp;
	}

	@Override
	public void updateUserProfile(UserUpdateRequest req) {
		Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
		if (userOpt.isEmpty()) {
			throw new RuntimeException("Invalid credentials for email: " + req.getEmail());
		}
		User user = userOpt.get();
//		user.setPassword(passwordEncoder.encode(req.getPassword()));
		userRepository.save(user);
	}
}
