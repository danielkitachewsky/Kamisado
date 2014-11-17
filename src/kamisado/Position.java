package kamisado;

import java.util.ArrayList;

public class Position {
  public Position(int dimension) {
    this.dimension = dimension;
    squares = new int[dimension * dimension];
    board = Board.GetBoard(dimension);
    cellWidth = ("" + dimension).length() + 1;  // To hold player and color
  }

  public Position() {
    this(8);
  }

  public Position(Position other) {
    this(other.dimension);
    int dimensionSquared = dimension * dimension;
    for (int i = 0; i < dimensionSquared; ++i) {
      this.squares[i] = other.squares[i];
    }
    this.playerToPlay = other.playerToPlay;
    this.colorToPlay = other.colorToPlay;
    this.forbiddenColor = other.forbiddenColor;
  }

  public void SetInitialPosition() {
    for (int column = 0; column < dimension; ++column) {
      Set(0, column, board.GetColor(0, column));  // White pieces
      for (int row = 1; row < dimension - 1; ++row) {
        Set(row, column, EMPTY);
      }
      // Black pieces
      Set(dimension - 1, column, board.GetColor(dimension - 1, column) + dimension);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    AddHorizontalLine(builder);
    for (int row = dimension - 1; row >= 0; --row) {
      builder.append('|');
      for (int column = 0; column < dimension; ++column) {
        int squareOccupant = Get(row, column);
        String colorString;
        if (squareOccupant == EMPTY) {
          builder.append('.');
          colorString = "" + board.GetColor(row, column);
        } else {
          builder.append(squareOccupant < dimension ? 'W' : 'B');
          colorString = "" + ColorOf(squareOccupant);
        }
        builder.append(colorString);
        for (int i = colorString.length() + 1; i < cellWidth; ++i) {
          builder.append('.');
        }
        builder.append('|');
      }
      builder.append('\n');
      AddHorizontalLine(builder);
    }
    return builder.toString();
  }
  
  public GameResult GetGameResult() {
    return gameResult;
  }

  // Does not handle the pass move (in fact, attempting the pass move will
  // throw).
  // Does not check legality of move (that's handled in GenerateLegalMoves).
  public Position MakeMove(int beginRow, int beginColumn, int endRow, int endColumn)
          throws IllegalMoveException {
    Position result = new Position(this);
    int currentPiece = Get(beginRow, beginColumn);
    if (currentPiece == EMPTY) {
      throw new IllegalMoveException("Moving from empty square (" + beginRow + "," + beginColumn
              + ") in\n" + this.toString());
    }
    if (PlayerOf(currentPiece) == BLACK && playerToPlay == WHITE) {
      throw new IllegalMoveException("Moving black piece at (" + beginRow + "," + beginColumn
              + ") when it's white to play in\n" + this.toString());
    }
    if (PlayerOf(currentPiece) == WHITE && playerToPlay == BLACK) {
      throw new IllegalMoveException("Moving white piece at (" + beginRow + "," + beginColumn
              + ") when it's black to play in\n" + this.toString());
    }
    if (colorToPlay != ANY_COLOR && ColorOf(currentPiece) != colorToPlay) {
      throw new IllegalMoveException("Moving wrong colored piece (" + beginRow + "," + beginColumn
              + ") in\n" + this.toString());
    }
    if (Get(endRow, endColumn) != EMPTY) {
      throw new IllegalMoveException("Moving to non-empty square (" + endRow + "," + endColumn
              + ") in\n" + this.toString());
    }
    result.Set(endRow, endColumn, currentPiece);
    result.Set(beginRow, beginColumn, EMPTY);
    if (playerToPlay == WHITE && endRow == dimension - 1) {
      result.gameResult = GameResult.WHITE_WINS;
    } else if (playerToPlay == BLACK && endRow == 0) {
      result.gameResult = GameResult.BLACK_WINS;
    }
    result.playerToPlay = 1 - playerToPlay;
    result.colorToPlay = ColorOf(currentPiece);
    result.forbiddenColor = NO_COLOR;
    return result;
  }

  public ArrayList<Position> GenerateLegalMoves() {
    ArrayList<Position> result = new ArrayList<Position>();
    if (gameResult != GameResult.ONGOING) {
      return result;
    }
    if (colorToPlay == ANY_COLOR) {
      // Normally happens only on first move.
      for (int color = 0; color < dimension; ++color) {
        Position coloredPosition = new Position(this);
        coloredPosition.colorToPlay = color;
        coloredPosition.forbiddenColor = NO_COLOR;
        result.addAll(coloredPosition.GenerateLegalMoves());
      }
      return result;
    }

    Square activePiece = FindActivePiece();
    int newColor = ColorAt(activePiece.row, activePiece.column);
    if (playerToPlay == WHITE) {
      for (int i = activePiece.row + 1; i < dimension; ++i) {
        if (Get(i, activePiece.column) == EMPTY) {
          result.add(MakeMove(activePiece.row, activePiece.column, i, activePiece.column));
        } else {
          break;
        }
      }
      int i = activePiece.row + 1;
      int j = activePiece.column + 1;
      while (i < dimension && j < dimension) {
        if (Get(i, j) == EMPTY) {
          result.add(MakeMove(activePiece.row, activePiece.column, i, j));
          ++i;
          ++j;
        } else {
          break;
        }
      }
      i = activePiece.row + 1;
      j = activePiece.column - 1;
      while (i < dimension && j >= 0) {
        if (Get(i, j) == EMPTY) {
          result.add(MakeMove(activePiece.row, activePiece.column, i, j));
          ++i;
          --j;
        } else {
          break;
        }
      }
    } else /* playerToPlay == BLACK */{
      for (int i = activePiece.row - 1; i >= 0; --i) {
        if (Get(i, activePiece.column) == EMPTY) {
          result.add(MakeMove(activePiece.row, activePiece.column, i, activePiece.column));
        } else {
          break;
        }
      }
      int i = activePiece.row - 1;
      int j = activePiece.column + 1;
      while (i >= 0 && j < dimension) {
        if (Get(i, j) == EMPTY) {
          result.add(MakeMove(activePiece.row, activePiece.column, i, j));
          --i;
          ++j;
        } else {
          break;
        }
      }
      i = activePiece.row - 1;
      j = activePiece.column - 1;
      while (i >= 0 && j >= 0) {
        if (Get(i, j) == EMPTY) {
          result.add(MakeMove(activePiece.row, activePiece.column, i, j));
          --i;
          --j;
        } else {
          break;
        }
      }
    }

    if (result.isEmpty()) {
      // We're in the stalemate situation, so the only legal move is passing.
      if (newColor == forbiddenColor) {
        // Mutual stalemate, game is drawn.
        return result;
      }
      Position passedPosition = new Position(this);
      passedPosition.playerToPlay = 1 - this.playerToPlay;
      passedPosition.forbiddenColor = this.colorToPlay;
      passedPosition.colorToPlay = newColor;
      ArrayList<Position> afterPassedMoves = passedPosition.GenerateLegalMoves();
      if (afterPassedMoves.isEmpty()) {
        passedPosition.gameResult = GameResult.DRAW;
      }
      result.add(passedPosition);
    }
    return result;
  }

  public int Get(int row, int column) throws BoardException {
    if (row < 0 || row >= dimension) {
      throw new BoardException("Row out of bounds: " + row + " vs " + dimension);
    }
    if (column < 0 || column >= dimension) {
      throw new BoardException("Column out of bounds: " + column + " vs " + dimension);
    }
    return squares[row * dimension + column];
  }

  public void Set(int row, int column, int value) throws BoardException {
    if (row < 0 || row >= dimension) {
      throw new BoardException("Row out of bounds: " + row + " vs " + dimension);
    }
    if (column < 0 || column >= dimension) {
      throw new BoardException("Column out of bounds: " + column + " vs " + dimension);
    }
    squares[row * dimension + column] = value;
  }

  private void AddHorizontalLine(StringBuilder builder) {
    builder.append('+');
    for (int column = 0; column < dimension; ++column) {
      for (int i = 0; i < cellWidth; ++i) {
        builder.append('-');
      }
      builder.append('+');
    }
    builder.append('\n');
  }

  private class Square {
    public Square(int row, int column) {
      this.row = row;
      this.column = column;
    }

    public int row = 0;
    public int column = 0;
  }

  private Square FindActivePiece() {
    if (colorToPlay == ANY_COLOR) {
      throw new BoardException("FindActivePiece called when no color is active.");
    }
    for (int row = 0; row < dimension; ++row) {
      for (int column = 0; column < dimension; ++column) {
        if (IsActivePiece(row, column)) {
          return new Square(row, column);
        }
      }
    }
    throw new BoardException("Found no piece of color " + colorToPlay + " in\n" + this.toString());
  }

  boolean IsActivePiece(int row, int column) {
    if (colorToPlay == ANY_COLOR) {
      return false;
    }
    int currentPiece = Get(row, column);
    if (currentPiece == EMPTY) {
      return false;
    }
    if (playerToPlay != PlayerOf(currentPiece)) {
      return false;
    }
    return ColorOf(currentPiece) == colorToPlay;
  }

  private int ColorAt(int row, int column) {
    int currentPiece = Get(row, column);
    if (currentPiece == EMPTY) {
      return ANY_COLOR;
    }
    return ColorOf(currentPiece);
  }

  private int ColorOf(int piece) {
    return piece % dimension;
  }

  private int PlayerOf(int piece) throws BoardException {
    if (piece == EMPTY) {
      throw new BoardException("Taking player of empty square.");
    }
    return piece < dimension ? WHITE : BLACK;
  }

  private int dimension = 0;
  private final Board board;
  private int[] squares;
  private int playerToPlay = WHITE;
  private GameResult gameResult = GameResult.ONGOING;
  private int colorToPlay = ANY_COLOR;
  private int forbiddenColor = -1;  // To break mutual stalemate.
  private int cellWidth = 1;  // For display

  // Don't change values as algorithms depend on them.
  private static final int EMPTY = -1;
  private static final int ANY_COLOR = -1;
  // Synonym but reads better in some places.
  private static final int NO_COLOR = -1;
  
  private static final int WHITE = 0;
  private static final int BLACK = 1;
  
  public enum GameResult {
    ONGOING,
    WHITE_WINS,
    BLACK_WINS,
    DRAW;
  }
}
