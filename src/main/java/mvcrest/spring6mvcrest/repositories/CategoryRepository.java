package mvcrest.spring6mvcrest.repositories;

import mvcrest.spring6mvcrest.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
