package com.tasteofbengal.backend.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_SECONDS = 60;

	private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();


		if (!path.equals("/api/v1/auth/login") && !path.equals("/api/v1/auth/refresh")) {

			filterChain.doFilter(request, response);
			return;
		}

		String ipAddress = request.getRemoteAddr();


		RequestCounter counter = requestCounts.computeIfAbsent(ipAddress, key -> new RequestCounter());

		synchronized (counter) {

			long currentTime = Instant.now().getEpochSecond();

			if (currentTime - counter.windowStart >= WINDOW_SECONDS) {
				counter.windowStart = currentTime;
				counter.count = 0;
			}

			counter.count++;


			if (counter.count > MAX_REQUESTS) {


				response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
				response.setContentType("application/json");

				response.getWriter().write("""
						{
						    "status": 429,
						    "message": "Too many requests. Please try again later."
						}
						""");

				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private static class RequestCounter {

		private long windowStart = Instant.now().getEpochSecond();
		private int count = 0;
	}
}