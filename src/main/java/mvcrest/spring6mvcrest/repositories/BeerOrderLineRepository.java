package mvcrest.spring6mvcrest.repositories;

import mvcrest.spring6mvcrest.entities.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, UUID> {
}
