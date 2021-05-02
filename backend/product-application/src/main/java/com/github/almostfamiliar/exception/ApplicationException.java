package com.github.almostfamiliar.exception;

public abstract class ApplicationException extends IllegalStateException {

  public ApplicationException(String msg, Exception e) {
    super(msg, e);
  }

  public ApplicationException(String msg) {
    super(msg);
  }
}
