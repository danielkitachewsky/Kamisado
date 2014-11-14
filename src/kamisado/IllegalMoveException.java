package kamisado;

public class IllegalMoveException extends RuntimeException {

  public IllegalMoveException() {
  }

  public IllegalMoveException(String gripe) {
    super(gripe);
  }

  private static final long serialVersionUID = 2L;
}
