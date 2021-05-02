package com.github.almostfamiliar.exception;

public abstract class NotFoundException extends IllegalArgumentException {
  public NotFoundException(String msg) {
    super(msg);
  }
}
