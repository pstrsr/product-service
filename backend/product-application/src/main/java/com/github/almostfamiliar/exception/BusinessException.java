package com.github.almostfamiliar.exception;

public abstract class BusinessException extends IllegalStateException {

  public BusinessException(String msg, Exception e) {
    super(msg, e);
  }

  BusinessException(String msg) {
    super(msg);
  }
}
