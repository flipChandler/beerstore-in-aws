package com.hibicode.beerstore.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
	
	private final String code;
	private final HttpStatus status;
	
}
