package mvcrest.spring6mvcrest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Beer;
import mvcrest.spring6mvcrest.service.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beers";

    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public List<Beer> beerList() {
        return beerService.listBeer();
    }

    @GetMapping(BEER_PATH_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID id) {
        log.debug("Controller debug beerId: " + id);

        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<Beer> handleBeerPost(@RequestBody Beer beerPost) {

        Beer beerSaved = beerService.savedNewBeer(beerPost);

        var headers = new HttpHeaders();

        headers.add("location", BEER_PATH + "/" + beerSaved.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> handleBeerUpdate(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {

        beerService.beerUpdate(id, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> handlerBeerPatch(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {

        beerService.beerPatch(id, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> handlerBeerDelete(@PathVariable("beerId") UUID id) {

        beerService.beerDelete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
