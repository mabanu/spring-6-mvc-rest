package mvcrest.spring6mvcrest.service;

import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.BeerDTO;
import mvcrest.spring6mvcrest.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceNoJpaImpl implements BeerService {
    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceNoJpaImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    public Page<BeerDTO> beerPage(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(beerMap.values()));
    }

    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return Optional.of(beerMap.get(id));
    }

    public BeerDTO savedNewBeer(BeerDTO beerDTO) {
        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    public Optional<BeerDTO> beerUpdate(UUID id, BeerDTO beerDTO) {
        BeerDTO existing = beerMap.get(id);
        existing.setBeerName(beerDTO.getBeerName());
        existing.setPrice(beerDTO.getPrice());
        existing.setUpc(beerDTO.getUpc());
        existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        return Optional.of(existing);
    }

    public Optional<BeerDTO> beerPatch(UUID id, BeerDTO beerDTO) {
        BeerDTO existing = beerMap.get(id);

        if (StringUtils.hasText(beerDTO.getBeerName())){
            existing.setBeerName(beerDTO.getBeerName());
        }

        if (beerDTO.getBeerStyle() != null) {
            existing.setBeerStyle(beerDTO.getBeerStyle());
        }

        if (beerDTO.getPrice() != null) {
            existing.setPrice(beerDTO.getPrice());
        }

        if (beerDTO.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        if (StringUtils.hasText(beerDTO.getUpc())) {
            existing.setUpc(beerDTO.getUpc());
        }

        return Optional.of(existing);
    }

    public Boolean beerDelete(UUID id) {
        beerMap.remove(id);

        return true;
    }
}
