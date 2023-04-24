package mvcrest.spring6mvcrest.repositories;

import jakarta.validation.ConstraintViolationException;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void savedBeerTest() {
        var savedBeer = beerRepository.save(Beer.builder()
                        .beerName("saved test")
                        .beerStyle(BeerStyle.IPA)
                        .price(new BigDecimal("1.24"))
                        .upc("12345")
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void tooLongDataSavedBeertest() {

        assertThrows(ConstraintViolationException.class, () -> {
            var savedBeer = beerRepository.save(Beer.builder()
                    .beerName("saved test 12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890")
                    .beerStyle(BeerStyle.IPA)
                    .price(new BigDecimal("1.24"))
                    .upc("12345")
                    .build());

            beerRepository.flush();
        });
    }
}