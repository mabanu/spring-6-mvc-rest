package mvcrest.spring6mvcrest.service;

import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Beer;
import mvcrest.spring6mvcrest.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        var beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 1")
                .beerStyle(BeerStyle.LAGER)
                .upc("I dont know what upc mean")
                .quantityOnHand(500)
                .price(new BigDecimal("35.67"))
                .createdDate(LocalDateTime.now())
                .UpdateDate(LocalDateTime.now())
                .build();

        var beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 2")
                .beerStyle(BeerStyle.IPA)
                .upc("I dont know what upc mean")
                .quantityOnHand(500)
                .price(new BigDecimal("35.67"))
                .createdDate(LocalDateTime.now())
                .UpdateDate(LocalDateTime.now())
                .build();

        var beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 3")
                .beerStyle(BeerStyle.GOSE)
                .upc("I dont know what upc mean")
                .quantityOnHand(500)
                .price(new BigDecimal("35.67"))
                .createdDate(LocalDateTime.now())
                .UpdateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> listBeer(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Beer service debug beerId: " + id);

        return beerMap.get(id);
    }

    @Override
    public Beer savedNewBeer(Beer beer) {
        var beerSaved = Beer.builder()
                .id(UUID.randomUUID())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .version(beer.getVersion())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .UpdateDate(LocalDateTime.now())
                .build();

        beerMap.put(beerSaved.getId(), beerSaved);

        return beerSaved;
    }

    @Override
    public Beer beerUpdate(UUID id, Beer beer) {
        var beerUpdate = beerMap.get(id);

        beerUpdate.setBeerName(beer.getBeerName());
        beerUpdate.setBeerStyle(beer.getBeerStyle());
        beerUpdate.setUpdateDate(LocalDateTime.now());
        beerUpdate.setPrice(beer.getPrice());
        beerUpdate.setQuantityOnHand(beer.getQuantityOnHand());
        beerUpdate.setUpc(beer.getUpc());

        beerMap.put(beerUpdate.getId(), beerUpdate);

        return beerUpdate;
    }
}
