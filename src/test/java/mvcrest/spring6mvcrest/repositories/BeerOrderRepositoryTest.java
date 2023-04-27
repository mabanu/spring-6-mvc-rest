package mvcrest.spring6mvcrest.repositories;

import jakarta.transaction.Transactional;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.entities.BeerOrder;
import mvcrest.spring6mvcrest.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer customerTest;

    Beer beerTest;

    @BeforeEach
    void setUp() {
        customerTest = customerRepository.findAll().get(0);
        beerTest = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void beerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("test ref")
                .customer(customerTest)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        System.out.println(savedBeerOrder);
    }
}