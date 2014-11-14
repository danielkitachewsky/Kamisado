import kamisado.Position;



public final class Kamisado {

  public static void main(String[] args) {
    Position position = new Position();
    position.SetInitialPosition();
    System.out.println(position.toString());
    System.out.println(position.GenerateLegalMoves().size());
    position = position.MakeMove(0, 0, 1, 0);
    System.out.println(position.toString());
    System.out.println(position.GenerateLegalMoves().size());
    position = position.MakeMove(7, 7, 1, 1);
    System.out.println(position.toString());
    System.out.println(position.GenerateLegalMoves().size());
  }
}
