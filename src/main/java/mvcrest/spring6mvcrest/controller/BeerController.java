package mvcrest.spring6mvcrest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Beer;
import mvcrest.spring6mvcrest.service.BeerService;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id){
        log.debug("Controller debug beerId: " + id);

        return beerService.getBeerById(id);
    }
}
