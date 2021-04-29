package com.github.almostfamiliar.domain;

import java.math.BigDecimal;

public record Money (BigDecimal amount, String currency){ }
