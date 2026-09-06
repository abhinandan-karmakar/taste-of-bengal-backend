package com.tasteofbengal.backend.authentication;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.exception.BadRequestException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.user.User;
import com.tasteofbengal.backend.user.UserRepo;

@Service
public class ForgetPasswordService {

	@Autowired
	private PasswordResetOtpRepo passwordResetOtpRepo;

	@Autowired
	private PasswordResetTokenRepo passwordResetTokenRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailService emailService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	private String generateOtp() {

		int otp = 100000 + new java.util.Random().nextInt(900000);

		return String.valueOf(otp);
	}

	public String generateResetToken() {

		SecureRandom secureRandom = new SecureRandom();

		byte[] tokenBytes = new byte[32];
		secureRandom.nextBytes(tokenBytes);

		return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
	}

	public String generateOtp(ForgetPasswordRequest forgetPasswordRequest) {
		
		String email = forgetPasswordRequest.getEmail();

		if (userRepo.findByEmail(email) == null) {
			throw new ResourceNotFoundException("Email does not exist");
		}
		
		PasswordResetOtp passwordResetRecord = passwordResetOtpRepo.findByEmail(email);

		if (passwordResetRecord != null) {
			if (passwordResetRecord.getExpiresAt().isAfter(LocalDateTime.now())) {
				throw new BadRequestException("OTP already sent to email");
			}
		}
		
		String otp = generateOtp();

		PasswordResetOtp passwordResetOtp = new PasswordResetOtp();
		passwordResetOtp.setEmail(email);
		passwordResetOtp.setOtp(otp);
		passwordResetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

		emailService.sendOtpEmail(email, otp);

		passwordResetOtpRepo.save(passwordResetOtp);

		return "OTP sent successfully";
	}

	public ForgetPasswordTokenResponse verifyEmailOtp(VerifyEmailOtpRequest emailOtpRequest) {
		String email = emailOtpRequest.getEmail();
		String otp = emailOtpRequest.getOtp();
		PasswordResetOtp recordToVerify = passwordResetOtpRepo.findByEmail(email);

		if (recordToVerify == null) {
			throw new ResourceNotFoundException("Request not found");
		}

		if (recordToVerify.getExpiresAt().isBefore(LocalDateTime.now())) {

			passwordResetOtpRepo.delete(recordToVerify);

			throw new BadRequestException("OTP has expired");
		}

		if (!recordToVerify.getOtp().equals(otp)) {

			throw new BadRequestException("Invalid OTP");
		}

		if (recordToVerify.isUsed()) {
			throw new BadRequestException("OTP already used");
		}

		String token = generateResetToken();

		PasswordResetToken passwordResetToken = new PasswordResetToken();

		passwordResetToken.setEmail(email);
		passwordResetToken.setToken(token);
		passwordResetToken.setExpireAt(LocalDateTime.now().plusMinutes(15));

		passwordResetTokenRepo.save(passwordResetToken);

		passwordResetOtpRepo.delete(recordToVerify);

		return new ForgetPasswordTokenResponse(token);
	}

	public String setNewPassword(NewPasswordRequest newPasswordRequest) {
		
		String token = newPasswordRequest.getToken();
		String newPassword = newPasswordRequest.getPassword();

		PasswordResetToken tokenRecord = passwordResetTokenRepo.findByToken(token);

		if (tokenRecord == null) {
			throw new ResourceNotFoundException("Invalid Token");
		}
		User user = userRepo.findByEmail(tokenRecord.getEmail());

		if (user == null) {
			throw new ResourceNotFoundException("No user found");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(user);
		
		passwordResetTokenRepo.delete(tokenRecord);
		
		return "Password updated successfully";
	}

}
