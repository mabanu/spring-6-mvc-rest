package mvcrest.spring6mvcrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.mappers.BeerMapper;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerController beerController;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void listBeersTest() {
        var dtos = beerController.beerList();

        assertThat(dtos).hasSize(3);
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

    @Test
    void beerPatchNoNameTest() throws Exception {

        var patchBeer = beerRepository.findAll().get(0);

        var beerMap = new HashMap<String, Object>();
        beerMap.put("beerName", "new name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        MvcResult result = mockMvc.perform(patch(BeerController.BEER_PATH_ID, patchBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}