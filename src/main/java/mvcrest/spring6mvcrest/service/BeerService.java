package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDTO> beerPage(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO savedNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> beerUpdate(UUID id, BeerDTO beerDTO);

    Optional<BeerDTO> beerPatch(UUID id, BeerDTO beerDTO);

    Boolean beerDelete(UUID id);
}
