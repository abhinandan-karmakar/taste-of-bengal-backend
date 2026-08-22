package com.tasteofbengal.backend.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.authentication.LoginResponse;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.user.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepo refreshTokenRepo;

	@Autowired
	private JwtService jwtService;

	@Value("${jwt.refresh-expiration-days}")
	private long refreshExpirationDays;

	private String generateRefreshToken() {

		SecureRandom secureRandom = new SecureRandom();

		byte[] randomBytes = new byte[32];
		secureRandom.nextBytes(randomBytes);

		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

	private String hashToken(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

			return HexFormat.of().formatHex(hash);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to hash refresh token", e);
		}
	}


	public String createRefreshToken(User user, HttpServletRequest request) {

		String rawToken = generateRefreshToken();

		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(user);
		refreshToken.setTokenHash(hashToken(rawToken));
		refreshToken.setExpiresAt(LocalDateTime.now().plusDays(refreshExpirationDays));
		refreshToken.setRevoked(false);
		refreshToken.setDeviceInfo(request.getHeader("User-Agent"));
		refreshToken.setIpAddress(request.getRemoteAddr());

		refreshTokenRepo.save(refreshToken);

		return rawToken;
	}

	public RefreshToken validateRefreshToken(String rawToken) {

		String tokenHash = hashToken(rawToken);

		RefreshToken refreshToken = refreshTokenRepo.findByTokenHash(tokenHash)
				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));

		if (refreshToken.isRevoked()) {
			throw new RuntimeException("Refresh token has been revoked");
		}

		if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Refresh token has expired");
		}

		return refreshToken;
	}


	public void revokeRefreshToken(RefreshToken refreshToken) {
		refreshToken.setRevoked(true);
		refreshTokenRepo.save(refreshToken);
	}

	public void revokeRefreshToken(String rawToken) {
		String tokenHash = hashToken(rawToken);

		RefreshToken refreshToken = refreshTokenRepo.findByTokenHash(tokenHash)
				.orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

		refreshToken.setRevoked(true);
		refreshTokenRepo.save(refreshToken);
	}

	public LoginResponse refreshAccessToken(String refreshToken, HttpServletRequest request) {

		RefreshToken oldRefreshToken = validateRefreshToken(refreshToken);

		User user = oldRefreshToken.getUser();

		// Revoke old refresh token
		revokeRefreshToken(oldRefreshToken);

		// Generate new refresh token
		String newRefreshToken = createRefreshToken(user, request);

		// Generate new access token
		CustomUserDetails userDetails = new CustomUserDetails(user);

		String accessToken = jwtService.generateToken(userDetails);

		return new LoginResponse(accessToken, newRefreshToken);
	}

}
