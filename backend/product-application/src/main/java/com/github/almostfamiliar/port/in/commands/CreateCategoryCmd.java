package com.github.almostfamiliar.port.in.commands;

import java.math.BigInteger;

public record CreateCategoryCmd(String name, BigInteger parentId) {
}
