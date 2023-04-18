package mvcrest.spring6mvcrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mvcrest.spring6mvcrest.model.Beer;
import mvcrest.spring6mvcrest.service.BeerService;
import mvcrest.spring6mvcrest.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor = ArgumentCaptor.forClass(Beer.class);

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getBeerById() throws Exception {

        var beerTest = beerServiceImpl.listBeer().get(0);

        given(beerService.getBeerById(beerTest.getId())).willReturn(Optional.of(beerTest));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, beerTest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(beerTest.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beerTest.getBeerName())));

    }

    @Test
    void beerList() throws Exception {

        var beerListTest = beerServiceImpl.listBeer();

        given(beerService.listBeer()).willReturn(beerListTest);

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void handleBeerPost() throws Exception {

        var beerCreated = beerServiceImpl.listBeer().get(0);

        given(beerService.savedNewBeer(any(Beer.class))).willReturn(beerCreated);

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerCreated)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void handleBeerUpdate() throws Exception {

        var beerUpdate = beerServiceImpl.listBeer().get(0);

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beerUpdate.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerUpdate)))
                .andExpect(status().isNoContent());
    }

    @Test
    void handlerBeerPatch() throws Exception {

        var patchBeer = beerServiceImpl.listBeer().get(0);

        var beerMap = new HashMap<String, Object>();
        beerMap.put("beerName", "new name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, patchBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).beerPatch(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(patchBeer.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }

    @Test
    void handlerBeerDelete() throws Exception {

        var deleteBeer = beerServiceImpl.listBeer().get(0);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, deleteBeer.getId()))
                .andExpect(status().isNoContent());

        verify(beerService).beerDelete(uuidArgumentCaptor.capture());

        assertThat(deleteBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void notFoundExceptionTest() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}