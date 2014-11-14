package test.kamisado;

import kamisado.Board;

import org.junit.Assert;
import org.junit.Test;

public class BoardTest {

  @Test
  public void BoardColors() {
    Board board = Board.GetBoard(8);
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        Assert.assertEquals(j, board.GetColor(i, (i + j * (2 * i + 1)) % 8));
      }
    }
  }
}
