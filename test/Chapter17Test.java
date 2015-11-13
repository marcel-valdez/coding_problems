package chapter17;

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

public class Chapter17Test extends ChapterTestBase {
  @Test
  public void testInlineNumberSwap() {
    // given
    int[] a = { 1, 2, 5, 8, 17, 32, 65, 128, 255, 512, 1023 };
    int[] b = { 1, 3, 4*3, 9*3, 16*3, 31*3, 64*3, 127*3, 256*3, 511*3, 1022*3 };

    for(int i = 0; i < a.length; i++) {
      PairOfNumbers numbers = new PairOfNumbers(a[i], b[i]);
      InlineNumberSwapper swapper = new InlineNumberSwapper();
      // when
      swapper.swap(numbers);
      // then
      areEqual(numbers.first, b[i]);
      areEqual(numbers.second, a[i]);
    }
  }
}
