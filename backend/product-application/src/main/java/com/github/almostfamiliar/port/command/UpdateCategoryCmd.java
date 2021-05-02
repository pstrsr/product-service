package com.github.almostfamiliar.port.command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public record UpdateCategoryCmd (@NotNull @Min(0) Long id, @NotBlank String name, Optional<Long> parentId){
}
