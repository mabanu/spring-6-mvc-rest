package mvcrest.spring6mvcrest.bootstrap;

import mvcrest.spring6mvcrest.repositories.BeerRepository;
import mvcrest.spring6mvcrest.repositories.CustomerRepository;
import mvcrest.spring6mvcrest.service.BeerCsvService;
import mvcrest.spring6mvcrest.service.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Rollback
    @Transactional
    @Test
    void TestRun() throws Exception {
        bootstrapData.run((String) null);

        assertThat(beerRepository.count()).isEqualTo(2413L);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}
