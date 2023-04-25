package mvcrest.spring6mvcrest.service;

import com.opencsv.bean.CsvToBeanBuilder;
import mvcrest.spring6mvcrest.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

    @Override
    public List<BeerCSVRecord> convertCsv(File csvFile) throws FileNotFoundException {

        return new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                .withType(BeerCSVRecord.class)
                .build().parse();
    }
}
