package com.tasteofbengal.backend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendOtpEmail(String email, String otp) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Taste of Bengal - Email Verification");

		message.setText("Hello,\n\n" + "Thank you for registering with Taste of Bengal.\n\n"
				+ "Your email verification OTP is: " + otp + "\n\n" + "This OTP is valid for 5 minutes.\n\n"
				+ "If you did not create an account, please ignore this email.\n\n" + "Regards,\n" + "Taste of Bengal");


		try {
			mailSender.send(message);
			System.out.println("Email sent successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}