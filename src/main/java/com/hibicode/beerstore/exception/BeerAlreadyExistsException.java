package com.hibicode.beerstore.exception;

import org.springframework.http.HttpStatus;

public class BeerAlreadyExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public BeerAlreadyExistsException() {
		super("beers-5", HttpStatus.BAD_REQUEST);
	}

}
