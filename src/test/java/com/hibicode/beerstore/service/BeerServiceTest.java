package com.hibicode.beerstore.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hibicode.beerstore.enums.BeerType;
import com.hibicode.beerstore.exception.BeerAlreadyExistsException;
import com.hibicode.beerstore.exception.EntityNotFoundException;
import com.hibicode.beerstore.model.Beer;
import com.hibicode.beerstore.repository.BeerRepository;


public class BeerServiceTest {
	
	@InjectMocks
	private BeerService beerService;
	@Mock
	private BeerRepository beerRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Test(expected = BeerAlreadyExistsException.class)
	public void save_shouldDenyCreationOfBeer_ifItAlreadyExists() {
		Beer beerInDatabase = Beer.of("Heineken", BeerType.LAGER, new BigDecimal("355"));
		beerInDatabase.setId(10L);
		
		when(beerRepository.findByNameAndType("Heineken", BeerType.LAGER))
		.thenReturn(Optional.of(beerInDatabase));
		
		Beer beer = Beer.of("Heineken", BeerType.LAGER, new BigDecimal("355"));
		beerService.save(beer);
	}
	
	@Test
	public void save_shouldCreateNewBeer_ifItDoesntExist() {
		Beer beerInDatabase = Beer.of("Heineken", BeerType.LAGER, new BigDecimal("355"));
		beerInDatabase.setId(10L);
				
		Beer beer = Beer.of("Heineken", BeerType.LAGER, new BigDecimal("355"));
		when(beerRepository.save(beer)).thenReturn(beerInDatabase);
		
		Beer savedBeer = beerService.save(beer);
		
		assertThat(savedBeer.getId(), equalTo(10L));
		assertThat(savedBeer.getName(), equalTo("Heineken"));
		assertThat(savedBeer.getType(), equalTo(BeerType.LAGER));
	}
	
	@Test
    public void save_shouldUpdateBeerVolume_ifItIsTheSameObject() {
        final Beer beerInDatabase = Beer.of("Devassa", BeerType.PILSEN, new BigDecimal("300"));
        beerInDatabase.setId(10L);

        when(beerRepository.findByNameAndType("Devassa", BeerType.PILSEN))
        .thenReturn(Optional.of(beerInDatabase));

        final Beer beerToUpdate = Beer.of("Devassa", BeerType.PILSEN, new BigDecimal("200"));
        beerToUpdate.setId(10L);

        final Beer beerMocked = Beer.of("Devassa", BeerType.PILSEN, new BigDecimal("200"));
        beerMocked.setId(10L);

        when(beerRepository.save(beerToUpdate)).thenReturn(beerMocked);

        final Beer beerUpdated = beerService.save(beerToUpdate);
        assertThat(beerUpdated.getId(), equalTo(10L));
        assertThat(beerUpdated.getName(), equalTo("Devassa"));
        assertThat(beerUpdated.getType(), equalTo(BeerType.PILSEN));
        assertThat(beerUpdated.getVolume(), equalTo(new BigDecimal("200")));
    }

    @Test(expected = BeerAlreadyExistsException.class)
    public void save_shouldDenyUpdate_ifObjectsAreDifferent() {
        final Beer beerInDatabase = Beer.of("Heineken", BeerType.LAGER, new BigDecimal("355"));
        beerInDatabase.setId(10L);

        when(beerRepository.findByNameAndType("Heineken", BeerType
                .LAGER)).thenReturn(Optional.of(beerInDatabase));

        final Beer beerToUpdate = Beer.of("Heineken", BeerType.LAGER, new BigDecimal("355"));
        beerToUpdate.setId(5L);

        beerService.save(beerToUpdate);
    }
    
    @Test
    public void delete_shouldDeleteEntity_ifItExists() {
        final Beer beerInDatabase = Beer.of("Devassa", BeerType.PILSEN, new BigDecimal("300"));
        beerInDatabase.setId(10L);

        when(beerRepository.findById(10L))
        .thenReturn(Optional.of(beerInDatabase));     

        beerService.delete(10L);
        
        Mockito.verify(beerRepository).deleteById(eq(10L));
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_shouldDenyDeleteEntity_ifItDoesNotExist() {
        beerService.delete(5L);
    }
}
