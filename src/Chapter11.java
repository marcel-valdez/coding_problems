package chapter11;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.util.*;
import java.util.stream.*;

import structures.*;

class SortedArrayMerger {

  public void merge(int[] a, int[] b) {
    if(a == null) { throw new RuntimeException("a should not be null."); }
    if(b == null) { throw new RuntimeException("b should not be null."); }
    if(a.length <= b.length) {
      throw new RuntimeException(
        "a's length (" + a.length + ") must be greater than b's length (" + b.length + ")."
      );
    }

    int ai = a.length - b.length - 1, bi = b.length - 1;
    for(int ti = a.length - 1; bi >= 0; ti--) {
      if(ai >= 0 && a[ai] >= b[bi])
        a[ti] = a[ai--];
      else
        a[ti] = b[bi--];
    }
  }
}

class RotatedArraySearcher {
  // sample:
  // array=[2, 3, 4, 5, 1] number=2
  // how to quickly find this number 3
  // I can go to the start
  // check 2 then the middle 4 and the end 1
  // check on which side the -valid- range is
  // in this case 2 - 4
  // if our number is in that range, look for it there
  // if our number is not there, then do the same but now
  // with the numbers on the other side
  public boolean contains(int[] rotatedArray, int number) {
    return contains(rotatedArray, number, 0, rotatedArray.length);
  }

  private boolean contains(int[] rotatedArray, int number, int startIndex, int length) {
    if(length == 0) {
      return false;
    }

    if(length == 1) {
      return rotatedArray[startIndex] == number;
    }

    if(length == 2) {
      return rotatedArray[startIndex] == number || rotatedArray[startIndex + 1] == number;
    }

    int middleIndex = startIndex + (length / 2);

    int leftRangeStart = rotatedArray[startIndex];
    int leftRangeEnd = rotatedArray[middleIndex];


    int rightRangeStart = rotatedArray[middleIndex + 1];
    int rightRangeEnd = rotatedArray[startIndex + length - 1];

    if(leftRangeStart <= leftRangeEnd) { // left is ordered
      if(number >= leftRangeStart && number <= leftRangeEnd) { // our number is in the left range
        return contains(rotatedArray, number, startIndex, middleIndex - startIndex + 1);
      } else { // our number is in the right range
        return contains(
          rotatedArray,
          number,
          middleIndex + 1,
          length - middleIndex - 1
        );
      }
    } else { // right is ordered
      if(number >= rightRangeStart && number <= rightRangeEnd) { // our number is in the right range
        return contains(
          rotatedArray,
          number,
          middleIndex + 1,
          length - middleIndex - 1
        );
      } else { // our number is in the left range
        return contains(rotatedArray, number, startIndex, middleIndex - startIndex + 1);
      }
    }
  }
}


// Write a method to sort an array of strings so that all the anagrams are next to each other
// Questions:
// 1. This means simply to put all words that are considered palindromes next to each other.
class SortPalindromesFirst {

  public String[] sortPalindromes(String[] input) {
    String[] output = new String[input.length];
    groupPalindromes(input, output);
    return output;
  }

  private void groupPalindromes(String[] inputs, String[] outputs) {
    int topDown = outputs.length - 1;
    int bottomUp = 0;
    for(String input : inputs) {
      if(isPalindrome(input)) {
        outputs[bottomUp] = input;
        bottomUp++;
      } else {
        outputs[topDown] = input;
        topDown--;
      }
    }
  }

  private boolean isPalindrome(String input) {
    int forwards = 0;
    int backwards = input.length() - 1;
    while(forwards < backwards) {
      if(input.charAt(forwards) != input.charAt(backwards)) {
        return false;
      }

      forwards++;
      backwards--;
    }

    return true;
  }
}

// If you had a 20 GB file with a string on each line, and were asked to
// sort it, how would you do it?
// 0. Assuming I can use about 1 GB of memory.
// 1. Divide the 20 GB into 40 different sections of 500 Mb each
// 2. Load each section of 500 Mb and sort it, then write it back to the file in their
//    corresponding section.
// 3. Do a multiway mergesort, where we find the largest strings of each
//    section, and whatever we find, we put it in a new sorted file, so we just
//    keep appending to the new sorted file using the largest string of each
//    section, never keeping in memory more than 40 strings.


class WhitespacesStringArraySearch {
  public int indexOf(String[] array, String key) {
    int lo = 0;
    int hi = array.length - 1;
    int length = hi - lo + 1;
    while(length >= 1) {
      DEBUG.println("hi: %s, lo: %s, length: %s", hi, lo, length);
      if(length == 1) {
        if(!array[hi].equals(key)) {
          return -1;
        } else {
          return hi;
        }
      }

      int middle = findMiddle(array, lo, lo + (length / 2));
      DEBUG.println("middle: %s", middle);
      if(middle == -1) {
        if(length <= 2) {
          return -1;
        }
         // all whitespaces to the left
        lo = lo + (length / 2) + 1;
      } else {
        String middleKey = array[middle];
        int comparison = key.compareTo(middleKey);
        DEBUG.println("middleKey: %s comparison: %s ", middleKey, comparison);
        if(comparison == 0) {
          return middle;
        } else if(comparison < 0) {
          hi = lo + (length / 2) - 1;
        } else {
          lo = middle + 1;
        }
      }

      length = hi - lo + 1;
    }

    return -1;
  }

  private int findMiddle(String[] array, int startIndex, int halfIndex) {
    for(int i = halfIndex; i >= startIndex; i--) {
      if(!array[i].equals("")) {
        return i;
      }
    }

    return -1;
  }
}

interface MatrixFinder {
  MatrixIndex indexOf(int[][] matrix, int key);
}

// [ 1, 3,  4,  9 ]
// [ 2, 6,  7, 10 ]
// [ 5, 8, 11, 12 ]
class BadMatrixFinder implements MatrixFinder {
  private static final Object INC = new Object();
  private static final Object DEC = new Object();
  // According to my implementation, this should also have a binary search
  // over the columns once the range of rows is obtained, this should create
  // the minimal search space for a binary search of the elements to check.
  public MatrixIndex indexOf(int[][] matrix, int key) {
    if(matrix == null) { throw new RuntimeException("matrix cannot be null."); }
    if(matrix.length == 0 || matrix[0].length == 0) {
      throw new RuntimeException("matrix cannot be of size 0");
    }

    if(matrix.length == 1 && matrix[0].length == 1) {
      return matrix[0][0] == key ? index(0, 0) : MatrixIndex.NOT_FOUND;
    }

    int startingEndIndexRow = findLastEndIndexRow(
        matrix, key, 0, matrix.length - 1, 0);
    if(startingEndIndexRow == -1) {
      return MatrixIndex.NOT_FOUND;
    }
    DEBUG.println("------------------");
    int startingStartIndexRow = findLastStartIndexRow(matrix, key, startingEndIndexRow,
        startingEndIndexRow,
        matrix.length - 1,
        matrix.length - 1);

    if(startingStartIndexRow == -1) {
      return MatrixIndex.NOT_FOUND;
    }

    // do binary search on the range of valid rows
    for(int i = startingEndIndexRow; i <= startingStartIndexRow; i++) {
      int col = Arrays.binarySearch(matrix[i], key);
      if(col >= 0) {
        return index(i, col);
      }
    }

    return MatrixIndex.NOT_FOUND;
  }

  private static int findLastEndIndexRow(
    int[][] matrix, int key, int half, int prev, int prevValid) {

    DEBUG.println("findLastEndIndexRow(half: %s, prev: %s prevValid: %s)", half, prev, prevValid);
    int found = -1;
    if(half >= 0 && half < matrix.length && half != prev) {
      int delta = -1;
      if(matrix[half][matrix[half].length - 1] >= key) {
        prevValid = half;
        // then we want to increase the range by half
        DEBUG.println("INCREASE RANGE");
        delta = half - ((Math.abs(half - prev) + 1) / 2);
      } else {
        // then we want to decrease the range by half
        DEBUG.println("DECREASE RANGE");
        delta = half + ((Math.abs(half - prev) + 1) / 2);
      }

      if(delta != prevValid && delta != prev && delta != half) {
        found = findLastEndIndexRow(matrix, key, delta, half, prevValid);
      }
    }

    if(found == -1) {
      if(half >= 0 && half < matrix.length && matrix[half][matrix[half].length - 1] >= key) {
        found = half;
      } else if(matrix[prevValid][matrix[prevValid].length - 1] >= key) {
        found = prevValid;
      }
    }

    DEBUG.println("findLastEndIndexRow(half: %s, prev: %s prevValid: %s)=%s", half, prev, prevValid, found);
    return found;
  }

  private static int findLastStartIndexRow(
    int[][] matrix, int key, int min, int half, int prev, int prevValid) {
    DEBUG.println("findLastStartIndexRow(half:%s, prev:%s, prevValid:%s)", half, prev, prevValid);
    int found = -1;
    if(half >= min && half < matrix.length && half != prev) {
      int delta = -1;
      if(matrix[half][0] <= key) {
        prevValid = half;
        // then increase the range by going to smaller numbers
        DEBUG.println("INCREASE RANGE");
        delta = half + ((Math.abs(half - prev) + 1) / 2);
      } else {
        // then decrease the range by going to bigger numbers
        DEBUG.println("DECREASE RANGE");
        delta = half - ((Math.abs(half - prev) + 1) / 2);
      }

      if(delta != prevValid && delta != prev && delta != half) {
        found = findLastStartIndexRow(matrix, key, min, delta, half, prevValid);
      }
    }

    if(found == -1) {
      if(half >= min && half < matrix.length && matrix[half][0] <= key) {
        found = half;
      } else if(matrix[prevValid][0] <= key) {
        found = prevValid;
      }
    }

    DEBUG.println("findLastStartIndexRow(half:%s, prev:%s, prevValid:%s)=%s", half, prev, prevValid, found);
    return found;
  }

  private MatrixIndex index(int row, int col) {
    return MatrixIndex.index(row, col);
  }
}

class CorrectMatrixFinder implements MatrixFinder {
// find row or column where the item is

// [ 0,  1,  3,  8  ]
// [ 2,  4,  6,  13 ]
// [ 5,  7,  9,  14 ]
// [ 10, 11, 15, 16 ]
// Find: 5
// Start: 0,0 (r,c)

// 1. Look for first element int he diagonal that is greater than key (2,2)
// [ X, O, O, O, O ]
// [ O, X, O, O, O ]
// [ O, O, #, O, O ]
// [ O, O, O, O, O ]
// [ O, O, O, O, O ]
// 2. Limit search space to the 2 rectangles formed by the A's and the B's
// [ X, X, A, A, A ]
// [ X, X, A, A, A ]
// [ B, B, #, X, X ]
// [ B, B, X, X, X ]
// [ B, B, X, X, X ]
// 3. Call 2 recursive functions:
//    A) Set the boundaries to rectangle A
//    B) Set the boundaries to rectangle B
//
// Edge Case:
// 2.B We did not find a number in the diagonal greater than the key, but
//     there are more rows below the diagonal.
// [ X, O, O ]
// [ O, X, O ]
// [ O, O, X ]
// [ O, O, O ] #
// [ O, O, O ]
// 3.B Then automatically exclude the recangle above from the search
//     space, and only search on the left rectangle.
// [ X, X, X ]
// [ X, X, X ]
// [ X, X, X ]
// [ B, B, B ] #
// [ B, B, B ]
  public MatrixIndex indexOf(int[][] matrix, int key) {
    if(get(matrix, 0, 0) > key) {
      return MatrixIndex.NOT_FOUND;
    }

    if(get(matrix, matrix.length - 1, matrix[0].length - 1) < key) {
      return MatrixIndex.NOT_FOUND;
    }

    return indexOf(matrix, key, 0, 0, matrix.length, matrix[0].length);
  }


  // it is guaranteed that the key is 'within' the range of numbers of the matrix
  private MatrixIndex indexOf(
      int[][] matrix, int key, int startRow, int startCol, int boundaryRow, int boundaryCol) {
    DEBUG.println("indexOf(key:%s, start:(%s,%s), boundaries: (%s,%s))",
      key, startRow, startCol, boundaryRow, boundaryCol);
    // look for first element in the diagonal that is greater than key
    if(boundaryRow - startRow <= 0 || boundaryCol - startCol <= 0) {
      return MatrixIndex.NOT_FOUND;
    }

    if(boundaryRow - startRow == 1) {
      DEBUG.println("single row search");
      int col = Arrays.binarySearch(matrix[startRow], key);
      if(col >= 0) {
        return index(startRow, col);
      } else {
        return MatrixIndex.NOT_FOUND;
      }
    }

    if(boundaryCol - startCol == 1) {
      DEBUG.println("single column search");
      // TODO: Use binary search
      int row = -1;
      for(int i = 0; i < boundaryRow; i++) {
        if(matrix[i][startCol] == key) {
          row = i;
          break;
        }

        if(matrix[i][startCol] > key) {
          break;
        }
      }

      if(row >= 0 && row < boundaryRow) {
        return index(row, startCol);
      } else {
        return MatrixIndex.NOT_FOUND;
      }
    }

    int diagonalRow = startRow;
    int diagonalCol = startCol;
    // TODO: use binary search to search on the diagonal
    for(; diagonalRow < boundaryRow && diagonalCol < boundaryCol; diagonalRow++, diagonalCol++) {
      if(get(matrix, diagonalRow, diagonalCol) >= key) {
        break;
      }
    }

    MatrixIndex index = MatrixIndex.NOT_FOUND;
    DEBUG.println("diagonal: (%s, %s)", diagonalRow, diagonalCol);
    if(diagonalCol != boundaryCol) {
      if(get(matrix, diagonalRow, diagonalCol) == key) {
        return index(diagonalRow, diagonalCol);
      }

    // split
    // search on rectangle above diagonal spot
      int aboveStartRow = startRow; // should start first item of search
      int aboveStartCol = diagonalCol; // should start at diagonal col
      int aboveBoundaryRow = diagonalRow; // decreasse boundary row
      int aboveBoundaryCol = boundaryCol; // should not exceed current boundary
      DEBUG.println("Search above");
      index = indexOf(matrix, key, aboveStartRow, aboveStartCol, aboveBoundaryRow, aboveBoundaryCol);
    }

    if(diagonalRow != boundaryRow && index == MatrixIndex.NOT_FOUND) {
      // search on rectangle to the left of diagonal spot
      int leftStartRow = diagonalRow;
      int leftStartCol = startCol;
      int leftBoundaryRow = boundaryRow;
      int leftBoundaryCol = diagonalCol;
      DEBUG.println("Search to the left");
      return indexOf(matrix, key, leftStartRow, leftStartCol, leftBoundaryRow, leftBoundaryCol);
    } else {
      return index;
    }
  }

  private int get(int[][] matrix, int row, int col) {
    return matrix[row][col];
  }

  private MatrixIndex index(int row, int col) {
    return MatrixIndex.index(row, col);
  }
}

class MatrixIndex {
  public static final MatrixIndex NOT_FOUND = index(-1, -1);
  int col;
  int row;
  private MatrixIndex(int row, int col) { this.row = row; this.col = col; }

  public static MatrixIndex index(int row, int col) {
    return new MatrixIndex(row, col);
  }

  public boolean equals(Object other) {
    if(!(other instanceof MatrixIndex)) {
      return false;
    }

    MatrixIndex index = (MatrixIndex) other;
    return index.row == row && index.col == col;
  }

  public String toString() {
    return "(" + row + ", " + col + ")";
  }
}
