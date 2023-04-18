package mvcrest.spring6mvcrest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.mappers.BeerMapper;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeer() {
        var beers = beerRepository.findAll();
        var beersDtos = new ArrayList<BeerDTO>();

        for (var beer : beers) {
            beersDtos.add(beerMapper.beerToBeerDto(beer));
        }

        return beersDtos;
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Beer service debug beerId: " + id);

        if (beerRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }

        var beer = beerRepository.findById(id).get();

        var beerDto = beerMapper.beerToBeerDto(beer);

        return Optional.of(beerDto);
    }

    @Override
    public BeerDTO savedNewBeer(BeerDTO beerDTO) {
        var beerDtoToBeer = beerMapper.beerDtoToBeer(beerDTO);

        var beerSaved = beerRepository.save(beerDtoToBeer);

        return beerMapper.beerToBeerDto(beerSaved);
    }

    @Override
    public void beerUpdate(UUID id, BeerDTO beerDTO) {

        var beerCheck = beerRepository.findById(id);

        if (beerCheck.isPresent()) {
            var beerUpdate = beerCheck.get();

            beerUpdate.setBeerName(beerDTO.getBeerName());
            beerUpdate.setUpdatedDate(LocalDateTime.now());
            beerUpdate.setBeerStyle(beerDTO.getBeerStyle());
            beerUpdate.setPrice(beerDTO.getPrice());
            beerUpdate.setUpc(beerDTO.getUpc());
            beerUpdate.setQuantityOnHand(beerDTO.getQuantityOnHand());

            beerRepository.save(beerUpdate);
        }
    }

    @Override
    public void beerPatch(UUID id, BeerDTO beerDTO) {

        var beerCheck = beerRepository.findById(id);

        if (beerCheck.isEmpty()) {
            return;
        }

        var beerPatch = beerCheck.get();

        if (StringUtils.hasText(beerDTO.getBeerName())) {
            beerPatch.setBeerName(beerDTO.getBeerName());
        }

        if (StringUtils.hasText(beerDTO.getUpc())) {
            beerPatch.setUpc(beerDTO.getUpc());
        }

        if (beerDTO.getBeerStyle() != null) {
            beerPatch.setBeerStyle(beerDTO.getBeerStyle());
        }

        if (beerDTO.getPrice() != null) {
            beerPatch.setPrice(beerDTO.getPrice());
        }

        if (beerDTO.getQuantityOnHand() != null) {
            beerPatch.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        beerPatch.setUpdatedDate(LocalDateTime.now());

        if (beerDTO.getPrice() != null) {
            beerPatch.setVersion(beerDTO.getVersion());
        }

        beerRepository.save(beerPatch);
    }

    @Override
    public void beerDelete(UUID id) {
        beerRepository.deleteById(id);
    }
}
