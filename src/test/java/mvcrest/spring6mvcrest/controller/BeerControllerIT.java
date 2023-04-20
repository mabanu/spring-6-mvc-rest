package mvcrest.spring6mvcrest.controller;

import jakarta.transaction.Transactional;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.mappers.BeerMapper;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerController beerController;

    @Autowired
    BeerMapper beerMapper;

    @Test
    void listBeersTest() {
        var dtos = beerController.beerList();

        assertThat(dtos).hasSize(2);
    }

    @Rollback
    @Transactional
    @Test
    void emptyBeersListTest() {
        beerRepository.deleteAll();

        var dtos = beerController.beerList();

        assertThat(dtos).isEmpty();
    }

    @Test
    void getByIdTest() {
        var beer = beerRepository.findAll().get(0);

        var dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void beerIdNotFound() {
        assertThrows(NotFoundException.class, () ->
                beerController.getBeerById(UUID.randomUUID())
        );
    }

    @Rollback
    @Transactional
    @Test
    void savedBeerTest() {
        BeerDTO dto = BeerDTO.builder()
                .beerName("saved test")
                .build();

        ResponseEntity<BeerDTO> response = beerController.handleBeerPost(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] location = response.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[4]);

        BeerDTO beerResponse = beerController.getBeerById(savedUUID);

        assertThat(beerResponse).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void updateBeerTest() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String updateName = "UPDATE";
        beerDto.setBeerName(updateName);

        ResponseEntity<BeerDTO> response = beerController.handleBeerUpdate(beer.getId(), beerDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


        beerRepository.findById(beer.getId())
                .ifPresent(checkUpdate -> assertThat(checkUpdate.getBeerName()).isEqualTo(updateName));
    }

    @Test
    void updateIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> beerController.handleBeerUpdate(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteBeerTest() {
        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity<BeerDTO> response = beerController.handlerBeerDelete(beer.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void deleteBeerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.handlerBeerDelete(UUID.randomUUID()));
    }
}