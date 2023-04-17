package mvcrest.spring6mvcrest.controller;

import mvcrest.spring6mvcrest.service.BeerService;
import mvcrest.spring6mvcrest.service.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void getBeerById() throws Exception {

        var beerTest = beerServiceImpl.listBeer().get(0);

        given(beerService.getBeerById(beerTest.getId())).willReturn(beerTest);

        mockMvc.perform(get("/api/v1/beers/" + beerTest.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is (beerTest.getId().toString())))
                .andExpect(jsonPath("$.beerName", is (beerTest.getBeerName())));

    }

    @Test
    void beerList() {
    }

    @Test
    void testGetBeerById() {
    }

    @Test
    void handleBeerPost() {
    }

    @Test
    void handleBeerUpdate() {
    }

    @Test
    void handlerBeerPatch() {
    }

    @Test
    void handlerBeerDelete() {
    }
}