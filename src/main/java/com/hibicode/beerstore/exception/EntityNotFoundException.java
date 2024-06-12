package com.hibicode.beerstore.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
		super("beers-6", HttpStatus.NOT_FOUND);
	}

}
