package test.kamisado;

import kamisado.Position;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

  @Test
  public void PositionString() {
    Position position = new Position();
    Assert.assertEquals(26 * 17, position.toString().length());
  }

  @Test
  public void PositionMoves() {
    Position position = new Position();
    position.SetInitialPosition();
    Assert.assertEquals(102, position.GenerateLegalMoves().size());
    position = position.MakeMove(0, 0, 1, 0);
    Assert.assertEquals(13, position.GenerateLegalMoves().size());
    position = position.MakeMove(7, 7, 0, 0);
    Assert.assertEquals(0, position.GenerateLegalMoves().size());
  }
}
