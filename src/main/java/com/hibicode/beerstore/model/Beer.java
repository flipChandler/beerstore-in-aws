package com.hibicode.beerstore.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hibicode.beerstore.enums.BeerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "beer")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Beer {
	
	@Id
	@SequenceGenerator(name = "beer_seq", sequenceName = "beer_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "beer_seq")
	private Long id;
	
	@NotBlank(message = "beers-1")
	private String name;
	
	@NotNull(message = "beers-2")
	//@Enumerated(EnumType.STRING)
	private BeerType type;
	
	@NotNull(message = "beers-3")
	@DecimalMin(value = "0", message = "beers-4")
	private BigDecimal volume;
	
	public static Beer of(
			String name,
			BeerType type,
			BigDecimal volume) {
		return Beer.builder()
				.name(name)
				.type(type)
				.volume(volume)
				.build();
	}
	
	@JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

    @JsonIgnore
    public boolean alreadyExists() {
        return getId() != null;
    }

}
