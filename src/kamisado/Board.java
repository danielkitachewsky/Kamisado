package kamisado;

import java.util.HashMap;
import java.util.Map;

public class Board {
  public static Board GetBoard(int dimension) {
    if (!boardRepository.containsKey(dimension)) {
      boardRepository.put(dimension, new Board(dimension));
    }
    return boardRepository.get(dimension);
  }
  
  public int GetColor(int row, int column) throws BoardException {
    if (row < 0 || row >= dimension) {
      throw new BoardException("Row out of bounds: " + row + " vs " + dimension);
    }
    if (column < 0 || column >= dimension) {
      throw new BoardException("Column out of bounds: " + column + " vs " + dimension);
    }
    return squareColors[row * dimension + column];
  }

  private Board(int dimension) {
    this.dimension = dimension;
    squareColors = new int[dimension * dimension];
    if (dimension == 8) {
      NormalFill();
    }
  }
  
  private void SetColor(int row, int column, int color) throws BoardException {
    if (row < 0 || row >= dimension) {
      throw new BoardException("Row out of bounds: " + row + " vs " + dimension);
    }
    if (column < 0 || column >= dimension) {
      throw new BoardException("Column out of bounds: " + column + " vs " + dimension);
    }
    squareColors[row * dimension + column] = color;
  }

  // Fills the squares with a normal pattern.
  // Only works for 8.
  // TODO generalize.
  private void NormalFill() {
    for (int row = 0; row < dimension; ++row) {
      for (int column = 0; column < dimension; ++column) {
        int color = ((column - row) * (2 * row + 1)) % dimension;
        if (color < 0) {
          color += dimension;
        }
        SetColor(row, column, color);
      }
    }
  }

  private int dimension = 0;
  private int[] squareColors;
  private static Map<Integer, Board> boardRepository = new HashMap<Integer, Board>();
}
