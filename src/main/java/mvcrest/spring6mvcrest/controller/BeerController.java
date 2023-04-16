package mvcrest.spring6mvcrest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Beer;
import mvcrest.spring6mvcrest.service.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {
    private final BeerService beerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Beer> beerList(){
        return beerService.listBeer();
    }

    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID id){
        log.debug("Controller debug beerId: " + id);

        return beerService.getBeerById(id);
    }

    @PostMapping
    public ResponseEntity<Beer> handleBeerPost(@RequestBody Beer beerPost){

        Beer beerSaved = beerService.savedNewBeer(beerPost);

        var headers = new HttpHeaders();

        headers.add("location", "/api/v1/beers/" + beerSaved.getId().toString());

        return new ResponseEntity<>( headers, HttpStatus.CREATED);
    }
}
