package org.sagebionetworks.web.shared.exceptions;

/**
 * Thrown when authentication fails.
 *
 * @author jmhill
 *
 */
public class IllegalArgumentException extends Exception {

  public IllegalArgumentException() {
    super();
  }

  public IllegalArgumentException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalArgumentException(String message) {
    super(message);
  }

  public IllegalArgumentException(Throwable cause) {
    super(cause);
  }
}
