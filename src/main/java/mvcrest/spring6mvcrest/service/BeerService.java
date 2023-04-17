package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeer();

    Beer getBeerById(UUID id);

    Beer savedNewBeer(Beer beer);

    void beerUpdate(UUID id, Beer beer);

    void beerPatch(UUID id, Beer beer);

    void beerDelete(UUID id);
}
