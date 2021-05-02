package com.github.almostfamiliar.port.command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

public record CreateCategoryCmd(@NotBlank String name, Optional<@Min(0) Long> parentId) {
}
