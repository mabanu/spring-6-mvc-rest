package mvcrest.spring6mvcrest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.mappers.BeerMapper;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@Primary
@AllArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeer() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Beer service debug beerId: " + id);

        return Optional.ofNullable(beerMapper
                .beerToBeerDto(beerRepository
                        .findById(id)
                        .orElse(null)));
    }

    @Override
    public BeerDTO savedNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
    }

    @Override
    public Optional<BeerDTO> beerUpdate(UUID id, BeerDTO beerDTO) {

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(beerUpdate -> {
            beerUpdate.setBeerName(beerDTO.getBeerName());
            beerUpdate.setUpdatedDate(LocalDateTime.now());
            beerUpdate.setBeerStyle(beerDTO.getBeerStyle());
            beerUpdate.setPrice(beerDTO.getPrice());
            beerUpdate.setUpc(beerDTO.getUpc());
            beerUpdate.setQuantityOnHand(beerDTO.getQuantityOnHand());

            atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(beerUpdate))));
        }, () ->  atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }

    @Override
    public Optional<BeerDTO> beerPatch(UUID id, BeerDTO beerDTO) {

        var beerCheck = beerRepository.findById(id);

        if (beerCheck.isEmpty()) {
            return Optional.empty();
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

        return Optional.of(beerMapper.beerToBeerDto(beerRepository.save(beerPatch)));
    }

    @Override
    public Boolean beerDelete(UUID id) {

        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}