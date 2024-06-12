package com.hibicode.beerstore.error;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	
	private final int statusCode;
	private final List<ApiError> errors;
	
	public static ErrorResponse of(HttpStatus status, List<ApiError> errors) {
		return new ErrorResponse(status.value(), errors);
	}
	
	public static ErrorResponse of(HttpStatus status, ApiError error) {
		return of(status, Collections.singletonList(error));
	}
		
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@RequiredArgsConstructor
	public static class ApiError {
		
		private final String code;
		private final String message;
	}

}
