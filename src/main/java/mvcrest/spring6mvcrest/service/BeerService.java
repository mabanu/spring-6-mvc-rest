package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeer();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO savedNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> beerUpdate(UUID id, BeerDTO beerDTO);

    Optional<BeerDTO> beerPatch(UUID id, BeerDTO beerDTO);

    Boolean beerDelete(UUID id);
}
