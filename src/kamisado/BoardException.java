package kamisado;

public class BoardException extends RuntimeException {

  public BoardException() {
  }

  public BoardException(String gripe) {
    super(gripe);
  }

  private static final long serialVersionUID = 1L;
}
