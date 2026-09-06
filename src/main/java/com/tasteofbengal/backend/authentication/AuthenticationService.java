package com.tasteofbengal.backend.authentication;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.exception.BadRequestException;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.security.CustomUserDetails;
import com.tasteofbengal.backend.security.JwtService;
import com.tasteofbengal.backend.security.RefreshTokenService;
import com.tasteofbengal.backend.user.Role;
import com.tasteofbengal.backend.user.User;
import com.tasteofbengal.backend.user.UserRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailOtpRepo emailOtpRepo;

	@Autowired
	private PendingRegistrationRepo pendingRegistrationRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	private User mapToUser(PendingRegistration pendingUser) {
		User user = new User();

		if (userRepo.findByEmail(pendingUser.getEmail()) == null) {
			user.setEmail(pendingUser.getEmail());
		} else {
			throw new ConflictException("Email already exists");
		}

		if (userRepo.findByPhone(pendingUser.getPhone()) == null) {
			user.setPhone(pendingUser.getPhone());
		} else {
			throw new ConflictException("Phone number already exists");
		}

		user.setName(pendingUser.getUserName());
		user.setPassword(pendingUser.getPassword());
		user.setRole(Role.USER);

		return user;

	}

	private String generateOtp() {

		int otp = 100000 + new java.util.Random().nextInt(900000);

		return String.valueOf(otp);
	}

	public String registerUser(RegisterUserRequest userRequest) {


		if (userRepo.findByEmail(userRequest.getEmail()) != null) {
			throw new ConflictException("Email already registered");
		}

		String otp = generateOtp();

		PendingRegistration pending = pendingRegistrationRepo.findByEmail(userRequest.getEmail())
				.orElse(new PendingRegistration());

		pending.setEmail(userRequest.getEmail());
		pending.setUserName(userRequest.getName());
		pending.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		pending.setPhone(userRequest.getPhone());
		pending.setExpiresAt(LocalDateTime.now().plusMinutes(10));

		pendingRegistrationRepo.save(pending);

		EmailOtp emailOtp = emailOtpRepo.findByEmail(userRequest.getEmail()).orElse(new EmailOtp());

		emailOtp.setEmail(userRequest.getEmail());
		emailOtp.setOtp(otp);
		emailOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

		emailOtpRepo.save(emailOtp);

		emailService.sendOtpEmail(userRequest.getEmail(), otp);

		return "OTP sent to your email";
	}

	@Transactional
	public String verifyEmailOtp(VerifyEmailOtpRequest request) {

		EmailOtp emailOtp = emailOtpRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new BadRequestException("OTP not found or expired"));

		if (emailOtp.getExpiresAt().isBefore(LocalDateTime.now())) {

			emailOtpRepo.delete(emailOtp);

			throw new BadRequestException("OTP has expired");
		}

		if (!emailOtp.getOtp().equals(request.getOtp())) {

			throw new BadRequestException("Invalid OTP");
		}

		PendingRegistration pending = pendingRegistrationRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new BadRequestException("Registration request not found"));

		if (userRepo.findByEmail(request.getEmail()) != null) {

			throw new ConflictException("Email already registered");
		}

		User user = mapToUser(pending);

		userRepo.save(user);

		emailOtpRepo.deleteByEmail(user.getEmail());
		pendingRegistrationRepo.deleteByEmail(user.getEmail());

		return "Registration successful";
	}

	public LoginResponse loginUser(LoginRequest loginRequest, HttpServletRequest request) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		User user = userRepo.findById(userDetails.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User nor found"));

		String refreshToken = refreshTokenService.createRefreshToken(user, request);
		String accessToken = jwtService.generateToken(userDetails);

		return new LoginResponse(accessToken, refreshToken);

	}



}
