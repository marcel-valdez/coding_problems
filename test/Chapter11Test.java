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
}
