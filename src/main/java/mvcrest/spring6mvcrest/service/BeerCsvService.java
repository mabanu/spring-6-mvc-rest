package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCsv( File csvFile ) throws FileNotFoundException;
}
