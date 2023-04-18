package mvcrest.spring6mvcrest.bootstrap;

import lombok.AllArgsConstructor;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.entities.Customer;
import mvcrest.spring6mvcrest.model.BeerStyle;
import mvcrest.spring6mvcrest.repositories.BeerRepository;
import mvcrest.spring6mvcrest.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        var beer1 = Beer.builder()
                .beerName("beer1")
                .beerStyle(BeerStyle.GOSE)
                .price(new BigDecimal("1.45"))
                .upc("12345")
                .quantityOnHand(500)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        var beer2 = Beer.builder()
                .beerName("beer2")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("2.45"))
                .upc("22345")
                .quantityOnHand(500)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        var beers = new ArrayList<Beer>();
        beers.add(beer1);
        beers.add(beer2);

        beerRepository.saveAll(beers);

        var customer1 = Customer.builder()
                .customerName("customer1")
                .version(1)
                .build();

        var customer2 = Customer.builder()
                .customerName("customer2")
                .version(1)
                .build();

        var customers = new ArrayList<Customer>();
        customers.add(customer1);
        customers.add(customer2);

        customerRepository.saveAll(customers);
    }
}
