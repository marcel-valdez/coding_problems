package chapter1;

import java.lang.*;
import java.util.*;

class UniqueCharsIdentifier {
  // Cometi el error de no preguntarme sobre el dominio del input
  // hay un numero limitado de caracteres, por lo tanto, pude haber
  // mantenido un simple array donde si ya me encontre un dado caracter
  // le pongo true, y si me encuentro otro con true, cancelo el algoritmo
  // y regreso false
  public boolean hasUniqueChars(String str) {
    if(str == null) {
      throw new RuntimeException("str is null");
    }

    if(str.length() == 0) {
      return true;
    }

    if(str.length() > 256) { // Assuming we only allow for the 256 ASCII characters
      return false;
    }

    for(int i = 0; i < str.length() - 1; i++) {
      char current = str.charAt(i);
      for(int j = i+1; j < str.length(); j++) {
        char compared = str.charAt(j);
        if(current == compared) {
          return false;
        }
      }
    }

    return true;
  }
}


class StringReverser {
  public String reverse(String input) {
    if(input == null || input.length() == 0) { return input; }
    char[] reversedChars = new char[input.length()];
    int forwards = 0;
    int backwards = input.length() - 1;
    do {
      reversedChars[forwards] = input.charAt(backwards);
      reversedChars[backwards] = input.charAt(forwards);
    } while (++forwards <= --backwards);

    return new String(reversedChars);
  }
}

class StringPermutationChecker {
  // runtime: O(a.length() + b.length() + b.length())
  // memory: O(a.length() + b.length())
  public boolean check(String a, String b) {
    if(a == null || b == null) {
      return a == b;
    }

    if(a.length() != b.length()) {
      return false;
    }

    if(a.length() == 0) {
      return b.length() == 0;
    }

    // runtime: O(a.length())
    // memory: O(a.length())
    HashMap<Character, Integer> charMapA = buildCharMap(a);
    // runtime: O(b.length())
    // memory: O(a.length())
    HashMap<Character, Integer> charMapB = buildCharMap(b);
    // runtime: O(a.length())
    for(Map.Entry entry : charMapA.entrySet()) {
      if(!entry.getValue().equals(charMapB.get(entry.getKey()))) {
        return false;
      }
    }

    return true;
  }

  // runtime: O(str.length())
  // memory: O(str.length())
  public HashMap<Character, Integer> buildCharMap(String str) {
    HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
    for(int i = 0; i < str.length(); i++) {
      Integer count = charMap.get(str.charAt(i));
      if(count == null) {
        count = 1;
      } else {
        count = count + 1;
      }

      charMap.put(str.charAt(i), count);
    }

    return charMap;
  }
}

class SpaceReplacer {

  private static final int SHIFT_BY = 2;

  public void replace(char[] input, int length) {
    if(length > input.length) {
      throw new RuntimeException("length is greater than input.length");
    }

    if(length == 0) {
      return;
    }

    // this guy should create the new indices based on found spaces
    int[] shiftedIndices = buildShiftedIndices(input, length);
    // move characters from right to left
    shiftCharacters(input, length, shiftedIndices);
  }

  private int[] buildShiftedIndices(char[] input, int length) {
    int[] shiftedIndices = new int[length];
    int spaceCount = 0;
    for(int i = 0; i < length; i++) {
      shiftedIndices[i] = i + (spaceCount * SHIFT_BY);
      if(input[i] == ' ') {
        spaceCount++;
      }
    }

    return shiftedIndices;
  }

  private void shiftCharacters(char[] input, int length, int[] shiftedIndices) {
    for(int i = length - 1; i >= 0; i--) {
      if(input[i] == ' ') {
        input[shiftedIndices[i]] = '%';
        input[shiftedIndices[i]+1] = '2';
        input[shiftedIndices[i]+2] = '0';
      } else {
        input[shiftedIndices[i]] = input[i];
      }
    }
  }
}

class StringCompressor {
  public String compress(String input) {
    if(input == null) {
      throw new RuntimeException("input is null.");
    }

    if(input.length() < 3) {
      return input;
    }

    // Assuming that sb implements growing table algorithm
    StringBuilder sb = new StringBuilder(input.length() / 2);
    int currentCount = 1;
    char current = input.charAt(0);
    sb.append(current);
    for(int i = 1; i < input.length(); i++) {
      if (input.charAt(i) == current) {
        currentCount++;
      } else {
        // makes sure that compression is always better than raw string
        sb.append(currentCount);
        currentCount = 1;
        current = input.charAt(i);
        sb.append(current);
      }
    }

    sb.append(currentCount);

    String compressed = sb.toString();
    if(compressed.length() >= input.length()) {
      return input;
    } else {
      return compressed;
    }
  }
}

class Matrix90DegreeRotator {
  public void rotate(byte[][][] pixels) {
    int rowsToRotate = (int) Math.ceil(pixels.length / 2.0);
    boolean isOdd = (pixels.length & 0x1) == 0x1;
    int colsToRotate = isOdd ? rowsToRotate - 1 : rowsToRotate;

    for(int i = 0; i < rowsToRotate; i++) {
      for(int j = 0; j < colsToRotate; j++) {
        rotatePixel(pixels, i, j);
      }
    }
  }

  private void rotatePixel(byte[][][] pixels, int x, int y) {
    byte[] pixel1 = pixels[x][y];
    Point pixel1Pos = rotate(x, y, pixels.length);

    byte[] pixel2 = pixels[pixel1Pos.x][pixel1Pos.y];
    Point pixel2Pos = rotate(pixel1Pos, pixels.length);

    byte[] pixel3 = pixels[pixel2Pos.x][pixel2Pos.y];
    Point pixel3Pos = rotate(pixel2Pos, pixels.length);

    byte[] pixel4 = pixels[pixel3Pos.x][pixel3Pos.y];

    setPixel(pixels, pixel1, pixel1Pos);
    setPixel(pixels, pixel2, pixel2Pos);
    setPixel(pixels, pixel3, pixel3Pos);
    //System.out.println("(" + pixel3Pos.x + "," + pixel3Pos.y + ")->(" + x + "," + y + ")");
    setPixel(pixels, pixel4, x, y);
    //System.out.println("DONE WITH ("+x+","+y+")");
  }

  private void setPixel(byte[][][] pixels, byte[] pixel, Point point) {
    setPixel(pixels, pixel, point.x, point.y);
  }

  private void setPixel(byte[][][] pixels, byte[] pixel, int x, int y) {
    pixels[x][y] = pixel;
  }

  private Point rotate(Point point, int side) {
    return rotate(point.x, point.y, side);
  }

  private Point rotate(int x, int y, int side) {
    //System.out.println("(" + x + "," + y + ")" + "->" + "(" + y + "," + (side -x-1) + ")");
    return new Point(y, side - x - 1);
  }

  private static class Point {
    final int x;
    final int y;
    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}

/**
 * Mi solución no fue de máxima eficiencia en memoria, porque no necesitaba guardar
 * todas las posiciones donde había 0's, solamente necesitaba saber que columnas
 * y renglones serían 0's.
 */
class MatrixClearer {
  public void clearZeros(byte[][] matrix) {
    if(matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
      throw new RuntimeException("Invalid matrix");
    }

    // run: O(MxN) space: O(M+N)
    ZeroMarked zeros = markZeros(matrix);
    // run: O(MxN)
    clearZeros(matrix, zeros);
  }

  private static void clearZeros(byte[][] matrix, ZeroMarked zeros) {
    for(int row = 0; row < matrix.length; row++) {
      for(int col = 0; col < matrix[0].length; col++) {
        if(zeros.rows[row] || zeros.cols[col]) {
          matrix[row][col] = 0;
        }
      }
    }
  }

  // O(MxN)
  private ZeroMarked markZeros(byte[][] matrix) {
    ZeroMarked zeros = new ZeroMarked(matrix.length, matrix[0].length);

    for(int row = 0; row < matrix.length; row++) {
      if(matrix[row] == null || matrix[row].length == 0) {
        throw new RuntimeException("Invalid matrix");
      }

      for(int col = 0; col < matrix[0].length; col++) {
        zeros.cols[col] |= zeros.rows[row] |= matrix[row][col] == 0;
      }
    }

    return zeros;
  }

  private static class ZeroMarked {
    final boolean[] rows;
    final boolean[] cols;

    public ZeroMarked(int m, int n) {
      rows = new boolean[m];
      cols = new boolean[n];
    }
  }

  private void clearColumnAndRow(byte[][] matrix, int row, int column) {
    // clear row
    for(int i = 0; i < matrix.length; i++) {
      matrix[row][i] = 0;
    }

    for(int i = 0; i < matrix[row].length; i++) {
      matrix[i][column] = 0;
    }
  }
}

class StringRotationChecker {
  public boolean isRotation(String input, String rotation) {
    if(input == null || rotation == null) {
      return false;
    }

    if(input.length() != rotation.length()) {
      return false;
    }

    if(input.length() <= 1) {
      return input.equals(rotation);
    }

    int shift = findShift(input, rotation);
    if(shift == -1) {
      return false;
    }

    String shifted = shift(input, shift);
    return rotation.indexOf(shifted) != -1;
  }

  private int findShift(String input, String rotation) {
    // first find the index of the pivot on the rotation
    char pivotChar = input.charAt(0);
    int shift = -1;
    for(int i = 0; i < rotation.length(); i++) {
      if(pivotChar == rotation.charAt(i)) {
        boolean matched = true;
        for(
          int j = 1, k = i+1;
          matched && j < input.length() && k < rotation.length();
          j++, k++) {
          matched = input.charAt(j) == rotation.charAt(k);
        }

        if(matched) {
        // calculate by how much we have to shift
          shift = i;
          break;
        }
      }
    }

    return shift;
  }

  private String shift(String input, int shift) {
    if(shift == 0) {
      return input;
    }

    char[] shifted = new char[input.length()];
    for(int i = 0; i < shifted.length; i++) {
      int source = i + shift;
      if(source >= shifted.length) {
        source = source - shifted.length;
      }

      shifted[source] = input.charAt(i);
    }

    return new String(shifted);
  }
}
