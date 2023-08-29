package com.yuyaogc.lowcode.engine.exception;

public class EngineException extends RuntimeException {

  private static final long serialVersionUID = 5224696788505678598L;

  public EngineException() {
    super();
  }

  public EngineException(String message) {
    super(message);
  }

  public EngineException(String message, Throwable cause) {
    super(message, cause);
  }

  public EngineException(Throwable cause) {
    super(cause);
  }

}
