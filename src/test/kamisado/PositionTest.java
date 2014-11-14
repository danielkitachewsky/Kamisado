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
}
