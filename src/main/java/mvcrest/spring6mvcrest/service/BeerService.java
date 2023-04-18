package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeer();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO savedNewBeer(BeerDTO beerDTO);

    void beerUpdate(UUID id, BeerDTO beerDTO);

    void beerPatch(UUID id, BeerDTO beerDTO);

    void beerDelete(UUID id);
}
