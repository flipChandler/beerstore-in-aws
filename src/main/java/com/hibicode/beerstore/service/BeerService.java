package com.hibicode.beerstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hibicode.beerstore.exception.BeerAlreadyExistsException;
import com.hibicode.beerstore.exception.EntityNotFoundException;
import com.hibicode.beerstore.model.Beer;
import com.hibicode.beerstore.repository.BeerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeerService {

	private final BeerRepository beerRepository;

	public List<Beer> findAll() {
		return beerRepository.findAll();
	}

	public Beer save(final Beer beer) {
		beerAlreadyExists(beer);
		return beerRepository.save(beer);
	}

	private void beerAlreadyExists(final Beer beer) {
		Optional<Beer> optionalBeer = beerRepository.findByNameAndType(beer.getName(), beer.getType());

		if (optionalBeer.isPresent() && (beer.isNew() || isUpdatingToADifferentBeer(beer, optionalBeer))) {
			throw new BeerAlreadyExistsException();
		}
	}

	private boolean isUpdatingToADifferentBeer(Beer beer, Optional<Beer> beerByNameAndType) {
		return beer.alreadyExists() && !beerByNameAndType.get().equals(beer);
	}

	public void delete(Long id) {
		Optional<Beer> optionalBeer = beerRepository.findById(id);
		if (!optionalBeer.isPresent()) {
			throw new EntityNotFoundException();
		}
		beerRepository.deleteById(id);
	}
}
