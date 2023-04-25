package mvcrest.spring6mvcrest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.mappers.BeerMapper;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.model.BeerStyle;
import mvcrest.spring6mvcrest.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@Primary
@AllArgsConstructor
public class BeerServiceImpl implements BeerService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDTO> beerPage(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                  Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName)) {
            beerPage = listBeerByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageRequest) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
    }

    Page<Beer> listBeerByName(String beerName, Pageable pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
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
        }, () -> atomicReference.set(Optional.empty()));

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