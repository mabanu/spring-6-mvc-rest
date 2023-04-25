package mvcrest.spring6mvcrest.services;

import mvcrest.spring6mvcrest.model.BeerCSVRecord;
import mvcrest.spring6mvcrest.service.BeerCsvService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerCsvServiceImpTest {

    @Autowired
    BeerCsvService beerCsvService;

    @Test
    void convertCSV() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> recs = beerCsvService.convertCsv(file);

        System.out.println(recs.size());

        assertThat(recs).isNotEmpty();
    }
}
