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
