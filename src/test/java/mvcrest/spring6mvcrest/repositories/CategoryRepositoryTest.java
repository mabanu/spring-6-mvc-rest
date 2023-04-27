package mvcrest.spring6mvcrest.repositories;

import jakarta.transaction.Transactional;
import mvcrest.spring6mvcrest.entities.Beer;
import mvcrest.spring6mvcrest.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer beerTest;

    @BeforeEach
    void setUp() {
        beerTest = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testAddCategory() {
        Category savedCategory = categoryRepository.save(Category.builder()
                        .description("Ales")
                .build());

        beerTest.addCategory(savedCategory);

        Beer savedBeer = beerRepository.save(beerTest);

        System.out.println(savedBeer.getBeerName());
    }
}