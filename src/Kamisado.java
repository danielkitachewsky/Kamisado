import java.util.ArrayList;
import java.util.Random;

import kamisado.Position;



public final class Kamisado {

  public static void main(String[] args) {
    Random random = new Random();
    Position position = new Position();
    position.SetInitialPosition();
    while (position.GetGameResult() == Position.GameResult.ONGOING) {
      ArrayList<Position> moves = position.GenerateLegalMoves();
      position = moves.get(random.nextInt(moves.size()));
    }
    System.out.println(position.toString());
    System.out.println(position.GetGameResult());
//    System.out.println(position.GenerateLegalMoves().size());
  }
}
