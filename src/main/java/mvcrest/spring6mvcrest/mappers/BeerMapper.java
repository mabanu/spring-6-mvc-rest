package mvcrest.spring6mvcrest.mappers;

import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);
}
