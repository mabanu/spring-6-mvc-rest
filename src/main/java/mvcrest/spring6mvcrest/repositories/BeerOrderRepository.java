package mvcrest.spring6mvcrest.repositories;

import mvcrest.spring6mvcrest.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {
}
