package com.github.almostfamiliar.port.command;


import com.github.almostfamiliar.validators.Currency;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateProductCmd (
         @NotBlank String name,
         String description,
         @DecimalMin(value = "0.00", inclusive = false)
         @Digits(integer = Integer.MAX_VALUE, fraction = 2)
         @NotNull
         BigDecimal price,
         @Currency String currency,
         @NotEmpty List<@Min(0) Long> categories
){

}
