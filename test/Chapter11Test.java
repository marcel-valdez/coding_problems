package chapter11;

import static debug.Debugger.DEBUG;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.*;


import java.lang.*;
import java.util.*;
import java.util.stream.*;

import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import structures.*;
import test.*;

public class Chapter11Test extends ChapterTestBase {

  @Test
  public void testSortedArrayMerger() {
    // given
    int[] a = { 1, 3, 5, 0, 0, 0 };
    int[] b = { 2, 4, 6 };
    int[] expected = { 1, 2, 3, 4, 5, 6 };
    SortedArrayMerger merger = new SortedArrayMerger();
    // when
    merger.merge(a, b);
    // then
    areEqual(a, expected);
  }

  @Test
  public void testRotatedArraySearcher() {
    // given
    int[] input = { 2, 3, 4, 5, 1 };
    int[] otherInput = { 9, 10, 6, 6, 6 };

    for(int term : input) {
      checkRotatedArraySearcher_Present(input, term);
      checkRotatedArraySearcher_NotPresent(otherInput, term);
    }

    for(int term : otherInput) {
      checkRotatedArraySearcher_Present(otherInput, term);
      checkRotatedArraySearcher_NotPresent(input, term);
    }
  }

  private void checkRotatedArraySearcher_Present(int[] input, int term) {
    RotatedArraySearcher searcher = new RotatedArraySearcher();
    // when
    boolean actual = searcher.contains(input, term);
    // then
    isTrue(actual);
  }

  private void checkRotatedArraySearcher_NotPresent(int[] input, int term) {
    RotatedArraySearcher searcher = new RotatedArraySearcher();
    // when
    boolean actual = searcher.contains(input, term);
    // then
    isFalse(actual);
  }

  @Test
  public void testSortPalindromesFirst() {
    // given
    String[] input = { "not anagram", "aba", "other", "bab", "otto" };
    String[] expected = { "aba", "bab", "otto", "other", "not anagram" };
    SortPalindromesFirst target = new SortPalindromesFirst();
    // when
    DEBUG.enable();
    String[] actual = target.sortPalindromes(input);
    DEBUG.disable();
    // then
    areEqual(actual, expected);
  }

  @Test
  public void testWhitespacesStringArraySearch() {
    // given
    String[] inputs =
      { "a", "", "", "b", "", "d", "", "e", "f", "n", "r", "", "", "", "z"  };
    String[] other =
      { "j", "", "", "", "l", "m", "o", "w", "x", "", "", "y"  };
    WhitespacesStringArraySearch target = new WhitespacesStringArraySearch();
    // when
    for(int i = 0; i < inputs.length; i++) {
    // then
      if(!inputs[i].equals("")) {
        areEqual("inputs(" + inputs[i] + ")", target.indexOf(inputs, inputs[i]), i);
        areEqual(target.indexOf(other, inputs[i]), -1);
      }
    }

    for(int j = 0; j < other.length; j++) {
      if(!other[j].equals("")) {
        areEqual("other(" + other[j] + ")", target.indexOf(other, other[j]), j);
        areEqual(target.indexOf(inputs, other[j]), -1);
      }
    }
  }

  @Test
  public void testBadMatrixSearch_PairRows() {
    testMatrixSearch_PairRows(new BadMatrixFinder());
  }

  @Test
  public void testCorrectMatrixSearch_PairRows() {
    testMatrixSearch_PairRows(new CorrectMatrixFinder());
  }

  public void testMatrixSearch_PairRows(MatrixFinder finder) {
    // given
    int[][] matrix = {
      {1,  3,  6,  7,  17},
      {2,  5,  8,  11, 18},
      {4,  9,  10, 12, 19},
      {13, 16, 20, 21, 29},
      {14, 24, 25, 26, 30},
      {28, 31, 32, 33, 34}
    };
    // when
    for(int row = 0; row < matrix.length; row++) {
      for(int col = 0; col < matrix[0].length; col++) {
      DEBUG.println("------------------------------");
      DEBUG.println("Searching: " + matrix[row][col]);
    // then
        MatrixIndex index = finder.indexOf(matrix, matrix[row][col]);
        areEqual(
          "Row for matrix["+ row + "][" + col + "](" + matrix[row][col] + ")",
          index,
          MatrixIndex.index(row, col)
        );
      }
    }
  }

  @Test
  public void testBadMatrixSearch_OddRows() {
    testMatrixSearch_OddRows(new BadMatrixFinder());
  }

  @Test
  public void testCorrectMatrixSearch_OddRows() {
    DEBUG.enable();
    testMatrixSearch_OddRows(new CorrectMatrixFinder());
  }

  public void testMatrixSearch_OddRows(MatrixFinder finder) {
    // given
    int[][] matrix = {
      {1,  3,  6,  7,  17},
      {2,  5,  8,  11, 18},
      {13, 16, 20, 21, 29},
      {14, 24, 25, 26, 30},
      {28, 31, 32, 33, 34}
    };
    // when
    for(int row = 0; row < matrix.length; row++) {
      for(int col = 0; col < matrix[0].length; col++) {
      DEBUG.println("------------------------------");
      DEBUG.println("Searching: " + matrix[row][col]);
    // then
        MatrixIndex index = finder.indexOf(matrix, matrix[row][col]);
        areEqual(
          "Row for matrix["+ row + "][" + col + "](" + matrix[row][col] + ")",
          index,
          MatrixIndex.index(row, col)
        );
      }
    }
  }
}
