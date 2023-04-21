package mvcrest.spring6mvcrest.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;

    @NotBlank @NotNull
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;

    @NotNull @Positive
    private Integer quantityOnHand;

    @NotNull @Positive
    private BigDecimal price;

    @FutureOrPresent
    private LocalDateTime createdDate;

    @FutureOrPresent
    private LocalDateTime updatedDate;
}
