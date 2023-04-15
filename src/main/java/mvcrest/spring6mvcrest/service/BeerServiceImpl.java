package mvcrest.spring6mvcrest.service;

import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Beer;
import mvcrest.spring6mvcrest.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Beer service debug beerId: " + id);

        return Beer.builder()
                .id(id)
                .version(1)
                .beerName("Good Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("I dont know what upc mean")
                .quantityOnHand(500)
                .price(new BigDecimal("35.67"))
                .createdDate(LocalDateTime.now())
                .UpdateDate(LocalDateTime.now())
                .build();
    }
}
