package mvcrest.spring6mvcrest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.model.BeerStyle;
import mvcrest.spring6mvcrest.service.BeerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beers";

    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public Page<BeerDTO> beerPage(@RequestParam(required = false) String beerName,
                                  @RequestParam(required = false) BeerStyle beerStyle,
                                  @RequestParam(required = false) Boolean showInventory,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize) {
        return beerService.beerPage(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID id) {
        log.debug("Controller debug beerId: " + id);

        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> handleBeerPost(@Validated @RequestBody BeerDTO beerDTOPost) {

        BeerDTO beerDTOSaved = beerService.savedNewBeer(beerDTOPost);

        var headers = new HttpHeaders();

        headers.add("location", BEER_PATH + "/" + beerDTOSaved.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> handleBeerUpdate(@PathVariable("beerId") UUID id, @RequestBody BeerDTO beerDTO) {

        if (beerService.beerUpdate(id, beerDTO).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> handlerBeerPatch(@PathVariable("beerId") UUID id, @RequestBody BeerDTO beerDTO) {

        beerService.beerPatch(id, beerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> handlerBeerDelete(@PathVariable("beerId") UUID id) {

        if (Boolean.TRUE.equals(beerService.beerDelete(id))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        throw new NotFoundException();
    }
}
