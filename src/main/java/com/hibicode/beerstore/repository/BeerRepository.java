package com.hibicode.beerstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hibicode.beerstore.enums.BeerType;
import com.hibicode.beerstore.model.Beer;

public interface BeerRepository extends JpaRepository<Beer, Long> {
	
	Optional<Beer> findByNameAndType(String name, BeerType type);
}
