package mvcrest.spring6mvcrest.model;

import jakarta.validation.constraints.*;
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

    @NotBlank
    @NotNull
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;

    @NotBlank @NotNull @Positive
    private Integer quantityOnHand;

    @NotBlank @NotNull @Positive
    private BigDecimal price;

    @FutureOrPresent
    private LocalDateTime createdDate;

    @FutureOrPresent
    private LocalDateTime updatedDate;
}
